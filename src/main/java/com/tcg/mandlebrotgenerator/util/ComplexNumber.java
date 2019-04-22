package com.tcg.mandlebrotgenerator.util;

import java.util.Objects;

public final class ComplexNumber implements Comparable<ComplexNumber> {

    private final double real;
    private final double complex;

    private ComplexNumber(double real, double complex) {
        this.real = real;
        this.complex = complex;
    }

    private ComplexNumber(ComplexNumber number) {
        this.real = number.real;
        this.complex = number.complex;
    }

    private ComplexNumber(double a, boolean isReal) {
        if (isReal) {
            this.real = a;
            this.complex = 0f;
        } else {
            this.real = 0;
            this.complex = a;
        }
    }

    public static ComplexNumber of(double real, double complex) {
        return new ComplexNumber(real, complex);
    }

    public static ComplexNumber of(ComplexNumber complexNumber) {
        return new ComplexNumber(complexNumber);
    }

    public static ComplexNumber ofReal(double a) {
        return new ComplexNumber(a, 0);
    }

    public static ComplexNumber ofComplex(double a) { return new ComplexNumber(0, a); }

    public static ComplexNumber fromPolar(double abs, double arg) {
        return new ComplexNumber(abs * Math.cos(arg), abs * Math.sin(arg));
    }

    public static ComplexNumber zero() {
        return new ComplexNumber(0, 0);
    }

    public ComplexNumber conjugate() {
        return new ComplexNumber(this.real, -this.complex);
    }

    public boolean isReal() {
        return Double.compare(this.complex, 0) == 0;
    }

    public double abs() {
        return Math.sqrt(this.real * this.real + this.complex * this.complex);
    }

    public double arg() {
        return Math.atan2(this.complex, this.real);
    }

    public ComplexNumber copy() {
        return new ComplexNumber(this.real, this.complex);
    }

    public ComplexNumber add(ComplexNumber complexNumber) {
        double newReal = this.real + complexNumber.real;
        double newComplex = this.complex + complexNumber.complex;
        return new ComplexNumber(newReal, newComplex);
    }

    public ComplexNumber add(double real) {
        return new ComplexNumber(this.real + real, this.complex);
    }

    public ComplexNumber minus(ComplexNumber complexNumber) {
        double newReal = this.real - complexNumber.real;
        double newComplex = this.complex - complexNumber.complex;
        return new ComplexNumber(newReal, newComplex);
    }

    public ComplexNumber minus(double real) {
        return new ComplexNumber(this.real - real, this.complex);
    }

    public ComplexNumber mult(ComplexNumber complexNumber) {
        double newReal = (this.real * complexNumber.real) - (this.complex * complexNumber.complex);
        double newComplex = (this.real * complexNumber.complex) + (this.complex * complexNumber.real);
        return new ComplexNumber(newReal, newComplex);
    }

    public ComplexNumber scale(double real) {
        return new ComplexNumber(this.real * real, this.complex * real);
    }

    public ComplexNumber divide(ComplexNumber complexNumber) {
        ComplexNumber num = this.mult(complexNumber.conjugate());
        ComplexNumber denom = complexNumber.mult(complexNumber.conjugate());
        return num.div(denom.abs());
    }

    public ComplexNumber div(double real) {
        return new ComplexNumber(this.real / real, this.complex / real);
    }

    public ComplexNumber pow(double pow) {
        double abs = Math.pow(this.abs(), pow);
        double theta = pow * this.arg();
        return ComplexNumber.fromPolar(abs, theta);
    }

    public ComplexNumber root(double root) {
        return this.pow(1f / root);
    }

    public ComplexNumber exp() {
        return new ComplexNumber(
                Math.exp(this.real) * Math.cos(this.complex),
                Math.exp(this.real) * Math.sin(this.complex)
        );
    }

    public ComplexNumber sin() {
        return new ComplexNumber(
                Math.sin(this.real) * Math.cosh(this.complex),
                Math.cos(this.real) * Math.sinh(this.complex)
        );
    }

    public ComplexNumber cos() {
        return new ComplexNumber(
                Math.cos(this.real) * Math.cosh(this.complex),
                -Math.sin(this.real) * Math.sinh(this.complex)
        );
    }

    public ComplexNumber tan() {
        return sin().divide(cos());
    }

    public double getReal() {
        return real;
    }

    public double getComplex() {
        return complex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComplexNumber that = (ComplexNumber) o;
        return Double.compare(that.real, real) == 0 &&
                Double.compare(that.complex, complex) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(real, complex);
    }

    @Override
    public String toString() {
        if (Double.compare(real, 0) == 0) {
            if (Double.compare(complex, 0) == 0) {
                return "0";
            } else {
                return String.format("%fi", this.complex);
            }
        }
        if (Double.compare(complex, 0) == 0) {
            if (Double.compare(real, 0) == 0) {
                return "0";
            } else {
                return String.format("%f", this.real);
            }
        }
        return String.format(
                "%f%c%fi",
                this.real,
                (Double.compare(this.complex, 0) > 0) ? '+' : '-',
                Math.abs(this.complex)
        );
    }

    @Override
    public int compareTo(ComplexNumber o) {
        return Double.compare(this.abs(), o.abs());
    }


}
