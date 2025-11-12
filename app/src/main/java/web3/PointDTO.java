package web3;

import java.io.Serializable;
import java.math.BigDecimal;

public class PointDTO implements Serializable {
    private BigDecimal x ;
    private BigDecimal y;
    private BigDecimal r;
    boolean hit;

    PointDTO(BigDecimal x, BigDecimal y, BigDecimal r, boolean hit) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.hit = hit;
    }
}
