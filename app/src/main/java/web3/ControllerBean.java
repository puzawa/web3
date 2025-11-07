package web3;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@Named("controllerBean")
@ApplicationScoped
public class ControllerBean implements Serializable {
    private final PointDAO pointDAO = new PointDAO();
    private List<Point> points = pointDAO.findAll();

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

    public String getPointsAsJson() throws JsonProcessingException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            return mapper.writeValueAsString(points);
        } catch (Exception e) {
            return "[]";
        }
    }
}