package web3.util;

import java.math.BigDecimal;

public class MathFunctions {

    public static boolean hitCheck(BigDecimal x, BigDecimal y, BigDecimal r){
        return checkCircle(x, y, r) || checkRectangle(x, y, r) || checkTriangle(x, y, r);
    }

    private static boolean checkCircle(BigDecimal x, BigDecimal y, BigDecimal r){
        BigDecimal halfR = r.divide(BigDecimal.valueOf(2));
        BigDecimal zero = BigDecimal.ZERO;

        boolean inBounds = (x.compareTo(zero) >= 0) &&
                (x.compareTo(halfR) <= 0) &&
                (y.compareTo(zero) >= 0) &&
                (y.compareTo(halfR) <= 0);

        boolean inCircle = x.multiply(x).add(y.multiply(y)).compareTo(halfR.multiply(halfR)) <= 0;

        return inBounds && inCircle;
    }

    private static boolean checkRectangle(BigDecimal x, BigDecimal y, BigDecimal r){
        BigDecimal zero = BigDecimal.ZERO;
        BigDecimal halfR = r.divide(BigDecimal.valueOf(2));

        return (x.compareTo(zero) <= 0) &&
                (x.compareTo(r.negate()) >= 0) &&
                (y.compareTo(zero) >= 0) &&
                (y.compareTo(halfR) <= 0);
    }

    private static boolean checkTriangle(BigDecimal x, BigDecimal y, BigDecimal r){
        BigDecimal halfR = r.divide(BigDecimal.valueOf(2));
        BigDecimal zero = BigDecimal.ZERO;
        BigDecimal yMax = x.multiply(BigDecimal.valueOf(2)).add(r).negate();

        return (x.compareTo(halfR.negate()) >= 0) &&
                (x.compareTo(zero) <= 0) &&
                (y.compareTo(zero) <= 0) &&
                (y.compareTo(yMax) >= 0);

    }
}
