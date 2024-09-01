package Framework.Helpers;

public class Complex {

    /**
     * The real part of the complex number
     */
    private double re;

    /**
     * The imaginary part of the complex number.
     */
    private double im;

    /**
     * Create a new Complex object given the real and imaginary part of the complex number.
     * @param real the real part of the number
     * @param imaginary the imaginary part of the number
     */
    public Complex(double real, double imaginary) {
        this.re = real;
        this.im = imaginary;
    }

    /**
     * Compute the absolute value/magnitude of the complex number
     * @return the absolute value/magnitude
     */
    public double abs() {
        return Math.sqrt(Math.pow(re, 2) + Math.pow(im, 2));
    }

    /**
     * Get the real part of the complex number
     * @return the real part of the number
     */
    public double re() {
        return this.re;
    }

    /**
     * Get the imaginary part of the complex number
     * @return the imaginary part of the number
     */
    public double im() {
        return this.im;
    }

    /**
     * String representation
     * @return the string representation of the Complex object.
     */
    public String toString() {

        return String.format("%.2f+%.4fj", this.re, this.im);
    }



}
