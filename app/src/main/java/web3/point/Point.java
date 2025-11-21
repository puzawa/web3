package web3.point;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Point {

    private BigDecimal x;
    private BigDecimal y;
    private BigDecimal r;

    private long duration;
    private LocalDateTime date;

    private boolean check;
    private Long id;

    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public Point() {}

    public Point(BigDecimal x, BigDecimal y, BigDecimal r,
                 long duration, LocalDateTime date,
                 boolean check, Long id) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.duration = duration;
        this.date = date;
        this.check = check;
        this.id = id;
    }

    public Point(BigDecimal x, BigDecimal y, BigDecimal r,
                 long duration, LocalDateTime date,
                 boolean check) {
        this(x, y, r, duration, date, check, null);
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

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

    public DateTimeFormatter getFormatter() {
        return formatter;
    }

    public String beautifulDate() {
        return date.format(formatter);
    }

    public String beautifulDuration() {
        return ((duration / 1e6) * 1e6) / 1e6 + " ms";
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
