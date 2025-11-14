package web3.point;

import java.io.Serializable;
import java.math.BigDecimal;

public class PointDTO implements Serializable {
    final private BigDecimal x ;
    final private BigDecimal y;
    final private BigDecimal r;
    final boolean hit;

    public PointDTO(BigDecimal x, BigDecimal y, BigDecimal r, boolean hit) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.hit = hit;
    }
}
