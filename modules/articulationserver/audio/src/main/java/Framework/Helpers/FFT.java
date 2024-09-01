package Framework.Helpers;

import org.jtransforms.fft.DoubleFFT_1D;

/**
 * FFT.java
 * Implements rfft and rfftfreq methods from numpy.
 * @author Osamu Fujimoto
 */
public class FFT {

    /**
     * Compute the physical layout of the fast fourier transform
     * @param fft the fast fourier transform
     * @return the physical layout of the fast fourier transform
     */
  public static Complex[] rfft(double[] fft) {

        DoubleFFT_1D dblFFT = new DoubleFFT_1D(fft.length);

        dblFFT.realForward(fft);

        double[] re = new double[fft.length/2 + 1];
        double[] im = new double[fft.length/2 + 1];

        int n = fft.length;

        if (n % 2 == 0) {   // n is even

            for (int i = 0; i < n/2; i++) {

                re[i] = fft[2*i];

            }

            for (int i = 1; i < n/2; i++) {

                im[i] = fft[2*i+1];

            }

            re[n/2] = fft[1];

        } else {            // n is odd

            for (int i = 0; i < (n+1)/2; i++) {

                re[i] = fft[2*i];

            }

            for (int i = 1; i < (n-1)/2; i++) {

                im[i] = fft[2*i+1];
            }

            im[(n-1)/2] = fft[1];
        }

        Complex[] complex = new Complex[re.length];

        for (int i = 0; i < complex.length; i++) {

            complex[i] = new Complex(re[i], im[i]);
        }

        return complex;
    }

    /**
     * Return the discrete fourier transform sample frequencies.
     * @param n the window length
     * @param d sample spacing (inverse of the sampling rate)
     * @return array of length n/2 + 1 containing the sample frequencies
     */
    public static double[] rfftfreq(int n, double d) {

        double val = 1.0 / (n * d);

        int N = n / 2 + 1;

        double[ ] r = new double[N];

        for (int i = 0; i < N; i++) {

            r[i] = i * val;

        }

        return r;
    }

    /**
     * Return the discrete fourier transform sample frequencies.
     * @param n the window length
     * @return array of length n/2 + 1 containing the sample frequencies
     */
    public static double[] rfftfreq(int n) {

        return rfftfreq(n, 1.0f);

    }


}
