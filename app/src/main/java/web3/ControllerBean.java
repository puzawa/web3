package web3;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Provider;
import web3.point.Point;
import web3.point.PointDAO;
import web3.view.CheckboxView;
import web3.view.SpinnerView;
import web3.view.TextInputView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Named("controllerBean")
@ApplicationScoped
public class ControllerBean implements Serializable {

    @Inject
    private Provider<CheckboxView> checkboxView;
    @Inject
    private Provider<SpinnerView> spinnerView;
    @Inject
    private Provider<TextInputView> textInputView;

    private final PointDAO pointDAO = new PointDAO();
    private List<Point> points = pointDAO.findAll();


    public List<Point> getPointsReversed() {
        List<Point> reversed = new ArrayList<>(points);
        Collections.reverse(reversed);
        return reversed;
    }
    public List<Point> getPoints() {
        return points;
    }

    public ControllerBean() {
    }


    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public void clear() {
        pointDAO.deleteAll();
        points.clear();
    }

    public void submitPoint(Point point) {
        addPoint(point);
        spinnerView.get().setNumber(0);
        textInputView.get().setInput("");
    }
    public void addPoint(Point point) {
        pointDAO.save(point);
        points.add(point);
    }
}