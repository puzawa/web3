package web3;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Provider;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import web3.point.Point;
import web3.point.PointDAO;
import web3.util.MathFunctions;
import web3.view.CheckboxView;
import web3.view.SpinnerView;
import web3.view.TextInputView;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

@Named("controllerBean")
@ApplicationScoped
public class ControllerBean implements Serializable {

    @Inject
    private Provider<CheckboxView> checkboxView;
    @Inject
    private Provider<SpinnerView> spinnerView;
    @Inject
    private Provider<TextInputView> textInputView;

    @Inject
    private PointDAO pointDAO;

    private final List<Point> points = new CopyOnWriteArrayList<>();

    private final Queue<Point> pendingQueue = new ConcurrentLinkedQueue<>();

    private Disposable syncTask;

    private final Object pointsLock = new Object();

    @PostConstruct
    public void init() {
        loadFromDb();
        startBackgroundSync();
    }

    private void startBackgroundSync() {
        syncTask = Flux.interval(Duration.ofSeconds(5))
                .onBackpressureDrop()
                .filter(tick -> !pendingQueue.isEmpty())
                .filter(tick -> pointDAO.isDBAvailable())
                .publishOn(Schedulers.boundedElastic())
                .flatMap(tick -> processPendingQueue())
                .subscribe();
    }

    private Mono<Void> processPendingQueue() {
        return Mono.defer(() -> {
            Point p = pendingQueue.peek();
            if (p == null) return Mono.empty();

            return pointDAO.add(p)
                    .doOnNext(newId -> {
                        p.setId(newId);
                        pendingQueue.poll();
                        System.out.println("Synced pending point to DB. ID: " + newId);
                    })
                    .then(Mono.defer(this::processPendingQueue))
                    .onErrorResume(e -> {
                        System.err.println("DB Sync failed (will retry later): " + e.getMessage());
                        return Mono.empty();
                    });
        });
    }

    private void loadFromDb() {
        if (!pointDAO.isDBAvailable()) return;

       pointDAO.getAll()
                .collectList()
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(dbPoints -> {
                    if (dbPoints != null) {
                        Set<Point> pendingSet = new HashSet<>(pendingQueue);
                        synchronized (pointsLock) {
                            points.clear();
                            points.addAll(dbPoints);
                            points.addAll(pendingSet);
                        }
                    }
                });

    }

    public void submitPoint(Point point) {
        addPoint(point);
        spinnerView.get().setNumber(0);
        textInputView.get().setInput("");
    }

    public void addPoint(Point point) {
        long start = System.nanoTime();

        boolean hit = MathFunctions.hitCheck(point.getX(), point.getY(), point.getR());
        point.setCheck(hit);
        point.setDate(LocalDateTime.now());
        point.setDuration(System.nanoTime() - start);

        synchronized (pointsLock) {
            points.add(point);
        }
        pendingQueue.add(point);

        processPendingQueue().subscribeOn(Schedulers.boundedElastic()).subscribe();
    }

    public void clear() {
        points.clear();
        pendingQueue.clear();

        if (pointDAO.isDBAvailable()) {
            pointDAO.deleteAll()
                    .subscribeOn(Schedulers.boundedElastic())
                    .subscribe();
        }
    }

    public List<Point> getPoints() {
        return points;
    }

    public List<Point> getPointsReversed() {
        List<Point> reversed = new ArrayList<>(points);
        Collections.reverse(reversed);
        return reversed;
    }

    public ArrayList<BigDecimal> getEnabledRs() {
        return checkboxView.get().getEnabledR();
    }
}