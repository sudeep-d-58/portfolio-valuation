// MathUtils.java
package com.portfolio.util;

public class MathUtils {

    /**
     * Approximation of the cumulative distribution function for the standard normal distribution.
     * This is used in the Black-Scholes formula for option pricing.
     */
    public static double normalCDF(double x) {
        // Constants for the approximation
        double a1 = 0.31938153;
        double a2 = -0.356563782;
        double a3 = 1.781477937;
        double a4 = -1.821255978;
        double a5 = 1.330274429;
        double p = 0.2316419;
        double c = 0.39894228;

        if (x >= 0.0) {
            double t = 1.0 / (1.0 + p * x);
            return 1.0 - c * Math.exp(-x * x / 2.0) * t *
                    (t * (t * (t * (t * a5 + a4) + a3) + a2) + a1);
        } else {
            return 1.0 - normalCDF(-x);
        }
    }

    /**
     * Calculate the normal probability density function.
     */
    public static double normalPDF(double x) {
        return Math.exp(-0.5 * x * x) / Math.sqrt(2.0 * Math.PI);
    }
}