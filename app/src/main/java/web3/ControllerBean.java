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
        final int CHUNK_SIZE = 10_000;

        return Mono.defer(() -> {
            if (pendingQueue.isEmpty()) return Mono.empty();

            List<Point> all = new ArrayList<>();
            Point p;
            while ((p = pendingQueue.poll()) != null) {
                all.add(p);
            }

            if (all.isEmpty()) return Mono.empty();

            List<List<Point>> chunks = new ArrayList<>();
            for (int i = 0; i < all.size(); i += CHUNK_SIZE) {
                chunks.add(all.subList(i, Math.min(i + CHUNK_SIZE, all.size())));
            }

            return Flux.fromIterable(chunks)
                    .concatMap(chunk -> sendChunk(chunk)
                            .then(Mono.delay(Duration.ofMillis(200)))
                    )
                    .then();
        });
    }

    private Mono<Void> sendChunk(List<Point> chunk) {
        return pointDAO.addAll(chunk)
                .doOnNext(ids -> {
                    for (int i = 0; i < chunk.size(); i++) {
                        chunk.get(i).setId(ids.get(i));
                        System.out.println("Synced point to DB. ID: " + ids.get(i));
                    }
                })
                .then()
                .onErrorResume(e -> {
                    pendingQueue.addAll(chunk);
                    System.err.println("DB batch sync failed (will retry later): " + e.getMessage());
                    return Mono.empty();
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

        SseServlet.broadcast("");

    }

    public void clear() {
        synchronized (pointsLock) {
            points.clear();
        }
        pendingQueue.clear();

        if (pointDAO.isDBAvailable()) {
            pointDAO.deleteAll()
                    .subscribeOn(Schedulers.boundedElastic())
                    .subscribe();
        }
        SseServlet.broadcast("");
    }

    public List<Point> getPoints() {
        synchronized (pointsLock) {
            return points;
        }
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