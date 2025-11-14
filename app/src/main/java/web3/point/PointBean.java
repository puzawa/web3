package web3.point;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import web3.ControllerBean;
import web3.util.MathFunctions;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Named("point")
@SessionScoped
public class PointBean implements Serializable {
    private BigDecimal x = null;
    private BigDecimal y = new BigDecimal("1.0");
    private BigDecimal r = new BigDecimal("1.0");

    @Inject
    private ControllerBean controllerBean;

    public PointBean() {
    }

    public BigDecimal getX() {
        return x;
    }

    public void setX(BigDecimal x) {
        this.x = x;
    }

    public BigDecimal getY() {
        return y;
    }

    public void setY(BigDecimal y) {
        this.y = y;
    }

    public BigDecimal getR() {
        return r;
    }

    public void setR(BigDecimal r) {
        this.r = r;
    }

    public void submit(Boolean hidden) {
        LocalDateTime localDateTime = LocalDateTime.now();

        if (x == null || y == null || r == null) {
            return;
        }
        Point p = new Point();

        p.setX(x);
        p.setY(y);
        p.setR(r);
        p.setDate(localDateTime);

        if (hidden)
            controllerBean.addPoint(p);
        else
            controllerBean.submitPoint(p);
    }
}
