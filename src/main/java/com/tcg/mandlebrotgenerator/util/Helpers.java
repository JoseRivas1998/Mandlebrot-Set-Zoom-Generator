package com.tcg.mandlebrotgenerator.util;

public class Helpers {

    public static double map(double value, double start1, double stop1, double start2, double stop2) {
        return start2 + (stop2 - start2) * ((value - start1) / (stop1 - start1));
    }

    public static ComplexNumber mapPixel(int x, int y, int pixelWidth, int pixelHeight, double centerX, double centerY, double realSize) {
        double hRealSize = realSize * 0.5f;
        double real = map(x, 0, pixelWidth, centerX - hRealSize, centerX + hRealSize);
        double complex = map(y, 0, pixelHeight, centerY - hRealSize, centerY + hRealSize);
        return ComplexNumber.of(real, complex);

    }

}
