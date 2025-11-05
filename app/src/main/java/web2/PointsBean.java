package web2;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named("pointsBean")
@SessionScoped
public class PointsBean implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final List<TableRow> history = new ArrayList<>();

    public void add(TableRow row) {
        history.add(row);
    }

    public List<TableRow> getHistory() {
        return new ArrayList<>(history); // возвращаем копию
    }
}
