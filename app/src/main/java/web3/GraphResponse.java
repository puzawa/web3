package web3;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

public class GraphResponse implements Serializable {
    private ArrayList<PointDTO> points;
    private BigDecimal maxR;
    GraphResponse(ArrayList<PointDTO> points, BigDecimal maxR) {
        this.points = points;
        this.maxR = maxR;
    }
}
