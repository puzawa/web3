package web3;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import web3.point.Point;
import web3.point.PointDAO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Named("controllerBean")
@ApplicationScoped
public class ControllerBean implements Serializable {
    private final PointDAO pointDAO = new PointDAO();
    private List<Point> points = pointDAO.findAll();

    public List<Point> getPointsReversed() {
        List<Point> reversed = new ArrayList<>(points);
        Collections.reverse(reversed);
        return reversed;
    }
    public ControllerBean() {
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public void clear() {
        points.clear();
        pointDAO.deleteAll();
    }

    public void addPoint(Point point) {
        points.add(point);
        pointDAO.save(point);
    }
}