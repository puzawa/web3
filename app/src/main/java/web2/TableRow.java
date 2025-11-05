package web2;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

public class TableRow implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private BigDecimal x;
    private BigDecimal y;
    private BigDecimal r;

    private boolean result;

    public TableRow(BigDecimal x, BigDecimal y, BigDecimal r, boolean result) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.result = result;
    }

    public String getX() {
        return x.toString();
    }

    public String getY() {
        return y.toString();
    }

    public String getR() {
        return r.toString();
    }

    public boolean getResult() {
        return result;
    }
}
