package web2;
import java.math.BigDecimal;

public class Validator {

    private static BigDecimal bd(double d) {
        return BigDecimal.valueOf(d);
    }
    private static final float MAX_R = 100f;

    public static boolean checkX(BigDecimal x){
        return (x.compareTo(bd(-MAX_R)) >= 0) && (x.compareTo(bd(MAX_R)) <= 0);
    }

    public static boolean checkY(BigDecimal y){
        return (y.compareTo(bd(-MAX_R)) >= 0) && (y.compareTo(bd(MAX_R)) <= 0);
    }

    public static boolean checkR(BigDecimal r){
        return (r.compareTo(bd(1)) >= 0) && (r.compareTo(bd(3)) <= 0);
    }
}
