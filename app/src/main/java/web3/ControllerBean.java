package web3;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Provider;
import web3.point.Point;
import web3.point.PointDAO;
import web3.util.MathFunctions;
import web3.view.CheckboxView;
import web3.view.SpinnerView;
import web3.view.TextInputView;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

    private List<Point> points = new ArrayList<>();

    private boolean dbMerged = false;

    @PostConstruct
    public void init() {
        checkDbMerge();
    }

    public void mergePointsWithDB() {
        if (!pointDAO.isDBAvailable()) {
            System.err.println("DB not available. Cannot merge points.");
            return;
        }

        if (dbMerged) {
            return;
        }

        List<Point> dbPoints;
        try {
            dbPoints = pointDAO.getAll().collectList().block();
            if (dbPoints == null) dbPoints = new ArrayList<>();
        } catch (Exception e) {
            System.err.println("Error reading from DB: " + e.getMessage());
            return;
        }

        System.out.println("DB has " + dbPoints.size() + " points.");

        Set<Long> dbIds = dbPoints.stream()
                .map(Point::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        for (Point dbPoint : dbPoints) {
            boolean exists = points.stream()
                    .anyMatch(p -> p.getId() != null && p.getId().equals(dbPoint.getId()));
            if (!exists) {
                points.add(dbPoint);
            }
        }

        for (Point point : points) {
            if (point.getId() == null || !dbIds.contains(point.getId())) {
                try {
                    Long newId = pointDAO.add(point).block();
                    point.setId(newId);
                    dbIds.add(newId);
                } catch (Exception e) {
                    System.err.println("Error syncing point to DB: " + e.getMessage());
                }
            }
        }

        dbMerged = true;
        System.out.println("Merged points with DB. Total in-memory: " + points.size());
    }

    public List<Point> getPointsReversed() {
        List<Point> reversed = new ArrayList<>(points);
        Collections.reverse(reversed);
        return reversed;
    }

    void checkDbMerge() {
        if (pointDAO.isDBAvailable()) {
            if (!dbMerged) {
                mergePointsWithDB();
            }
        } else {
            dbMerged = false;
        }
    }

    public List<Point> getPoints() {
        checkDbMerge();
        return points;
    }

    public void clear() {
        checkDbMerge();
        if (pointDAO.isDBAvailable()) {
            try {
                pointDAO.deleteAll().block();
            } catch (Exception e) {
                System.err.println("Failed to clear DB: " + e.getMessage());
            }
        }
        points.clear();
    }

    public void submitPoint(Point point) {
        addPoint(point);
        spinnerView.get().setNumber(0);
        textInputView.get().setInput("");
    }

    public ArrayList<BigDecimal> getEnabledRs() {
        return checkboxView.get().getEnabledR();
    }

    public void addPoint(Point point) {
        checkDbMerge();
        long start = System.nanoTime();

        boolean hit = MathFunctions.hitCheck(point.getX(), point.getY(), point.getR());
        point.setCheck(hit);
        point.setDate(LocalDateTime.now());
        point.setDuration(System.nanoTime() - start);

        if (pointDAO.isDBAvailable()) {
            try {
                Long id = pointDAO.add(point).block();
                point.setId(id);
            } catch (Exception e) {
                System.err.println("Failed to save point to DB: " + e.getMessage());
            }
        }

        points.add(point);
    }
}