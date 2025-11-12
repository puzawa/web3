package web3;

import java.io.Serializable;
import java.util.ArrayList;

public class PointsDTO implements Serializable {
    private ArrayList<PointDTO> points;

    PointsDTO(ArrayList<PointDTO> points) {
        this.points = points;
    }
}
