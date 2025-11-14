package web3;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

public class GraphResponse implements Serializable {
    final private ArrayList<PointDTO> points;
    final private BigDecimal maxR;
    final  private ArrayList<BigDecimal>enabledRs;
    GraphResponse(ArrayList<PointDTO> points, BigDecimal maxR, ArrayList<BigDecimal>enabledRs) {
        this.points = points;
        this.maxR = maxR;
        this.enabledRs = enabledRs;
    }
}
