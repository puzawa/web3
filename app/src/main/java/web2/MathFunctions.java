package web2;

import java.math.BigDecimal;

public class MathFunctions {

    public static boolean hitCheck(BigDecimal x, BigDecimal y, BigDecimal r){
        return checkCircle(x, y, r) || checkRectangle(x, y, r) || checkTriangle(x, y, r);
    }

    private static boolean checkCircle(BigDecimal x, BigDecimal y, BigDecimal r){
      //r = r/2; return (x >= -r) && (x <= 0) && (y >= -r) && (y <= 0) && (x*x + y*y <= r*r);
        BigDecimal halfR = r.divide(BigDecimal.valueOf(2));
        BigDecimal zero = BigDecimal.ZERO;

        boolean inBounds = (x.compareTo(halfR.negate()) >= 0) &&
                (x.compareTo(zero) <= 0) &&
                (y.compareTo(halfR.negate()) >= 0) &&
                (y.compareTo(zero) <= 0);

        boolean inCircle = x.multiply(x).add(y.multiply(y)).compareTo(halfR.multiply(halfR)) <= 0;

        return inBounds && inCircle;
    }

    private static boolean checkRectangle(BigDecimal x, BigDecimal y, BigDecimal r){
        //return (x >= 0) && (x <= r/2) && (y >= 0) && (y <= r);
        BigDecimal zero = BigDecimal.ZERO;
        BigDecimal halfR = r.divide(BigDecimal.valueOf(2));

        return (x.compareTo(zero) >= 0) && (x.compareTo(halfR) <= 0) &&
                (y.compareTo(zero) >= 0) && (y.compareTo(r) <= 0);
    }

    private static boolean checkTriangle(BigDecimal x, BigDecimal y, BigDecimal r){
        //return (x >= -r/2) && (x <= 0) && (y >= 0) && (y <= 2*x + r);
        BigDecimal halfR = r.divide(BigDecimal.valueOf(2));
        BigDecimal zero = BigDecimal.ZERO;
        BigDecimal yMax = x.multiply(BigDecimal.valueOf(2)).add(r);

        return (x.compareTo(halfR.negate()) >= 0) && (x.compareTo(zero) <= 0) &&
                (y.compareTo(zero) >= 0) && (y.compareTo(yMax) <= 0);
    }
}
