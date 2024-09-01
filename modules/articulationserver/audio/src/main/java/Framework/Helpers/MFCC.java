/*
    Turn Librosa Mfcc feature into Java code.
	Parameters are set to the librosa default for the purpose of android demo.
	The FFT code is taken from org.ioe.tprsa.audio.feature.
 */


/**
 * Mel-Frequency Cepstrum Coefficients.
 *
 * @author ChiaChun FU
 *
 */

package Framework.Helpers;

import android.util.Log;


import java.util.ArrayList;
import java.util.Arrays;

public class MFCC {

	private  final  static int n_mfcc_Valid = 13;
	private final static int       n_mfcc       		= 20;
	private final static double    fMin                 = 0.0;
	private final static int       n_fft                = 512;
	private final static int       hop_length           = 800;
	private final static int	   n_mels               = 20;

	private final static double    sampleRate           = 16000.0;
	private final static double    fMax                 = sampleRate;

FFT fft = new FFT();
	double[][] melBasis = melFilter();
	 double[][] dctBasis = dctFilter(n_mfcc, n_mels);

	public float[] process(double[] doubleInputBuffer) {
		final double[][] mfccResult = dctMfcc(doubleInputBuffer);
		return finalshape(mfccResult);
	}

//MFCC into 1d
	private float[] finalshape(double[][] mfccSpecTro){
		float[] finalMfcc = new float[n_mfcc_Valid];
		int k = 0;
		for (int i = 0; i < mfccSpecTro[0].length; i++){
			for (int j = 0; j < n_mfcc_Valid; j++){
				finalMfcc[k] = (float) mfccSpecTro[j][i];
				k = k+1;
			}
		}
		return finalMfcc;
	}

//DCT to mfcc, librosa
	private double[][] dctMfcc(double[] y){
		double[][] power = melSpectrogram(y);



		final double[][] specTroGram = powerToDb(power);

//		String data = "[ ";
//		for(int i=0; i<specTroGram.length;i++)
//		{
//			data += specTroGram[i][0]+" ";
//		}
//		data +=" ]";
//		Log.v("spdb", "spdb======> " + data);
//

		double[][] mfccSpecTro = new double[n_mfcc][specTroGram[0].length];
		for (int i = 0; i < n_mfcc; i++){
			for (int j = 0; j < specTroGram[0].length; j++){
				for (int k = 0; k < specTroGram.length; k++){
				 	mfccSpecTro[i][j] += dctBasis[i][k]*specTroGram[k][j];
				}
			}
		}
		double power_sum = 0;
		for(int i=0; i<power.length; i++)
		{
			for(int j=0; j<power[0].length;j++)
			{
				power_sum += power[i][j];
			}
		}

		power_sum = Math.log(power_sum);

		mfccSpecTro[0][0] = power_sum;

		return mfccSpecTro;
	}


//mel spectrogram, librosa
	private double[][] melSpectrogram(double[] y){

		double[][] spectro = stftMagSpec(y);


		double[][] melS = new double[melBasis.length][spectro[0].length];
		for (int i = 0; i < melBasis.length; i++){

			for (int j = 0; j < spectro[0].length; j++){
				for (int k = 0; k < melBasis[0].length; k++){
					 melS[i][j] += melBasis[i][k]*spectro[k][j];
				}
			}
		}
		return melS;
	}


	private double[][] stftMagSpec(double[] y)
	{
		double[] dataaStripped = new double[n_fft];
		for(int i=0; i<dataaStripped.length; i++)
		{
			dataaStripped[i] = y[i];
		}
		Complex[] elements = FFT.rfft(dataaStripped);

		double[][] pwr  = new double[elements.length][1];
		for(int i=0; i< pwr.length; i++)
		{
			pwr[i][0] = ((elements[i].re() * elements[i].re())+(elements[i].im() * elements[i].im()))/n_fft;
		}

		return  pwr;

	}





//power to db, librosa
	private double[][] powerToDb(double[][] melS){
		double[][] log_spec = new double[melS.length][melS[0].length];
		for (int i = 0; i < melS.length; i++){
			for (int j = 0; j < melS[0].length; j++){
					log_spec[i][j]=Math.log(melS[i][j]);

			}
		}

		return log_spec;
	}

//dct, librosa
	private double[][] dctFilter(int n_filters, int n_input){
		double[][] basis = new double[n_input][n_filters];
		for (int k = 0; k < n_input; k++){
			for (int n = 0; n < n_filters; n++){
				if(k==0)
				{
					basis[k][n] = 2 * Math.cos((Math.PI/n_filters)*(n+0.5)*(k))* Math.sqrt(1/(4.0*n_filters));

				}
				else {
					basis[k][n] = 2 * Math.cos((Math.PI / n_filters) * (n + 0.5) * (k)) * Math.sqrt(1 / (2.0 * n_filters));
				}
			}
		}
		return basis;
	}

	private double[][] melFilter(){
		//Create a Filterbank matrix to combine FFT bins into Mel-frequency bins.
		// Center freqs of each FFT bin
		final double[] fftFreqs = fftFreq();
		//'Center freqs' of mel bands - uniformly spaced between limits
		final double[] melF = melFreq(n_mels+2);
		final int[] grid_indices = new int[melF.length];
		for(int i=0; i<melF.length; i++)
		{
			grid_indices[i] = (int)(melF[i]*257/sampleRate);
		}


		final int[] grid_indices_minus_one = new int[melF.length];
		for(int i=0; i<melF.length; i++)
		{
			grid_indices_minus_one[i] = (grid_indices[0]-1)+grid_indices[i];
		}


		int offset = 0;
		final int[] correct_grid_indices = new int[melF.length];
		for(int i=0; i<melF.length; i++)
		{
			offset = Math.max(0, offset+grid_indices_minus_one[i]+1-grid_indices[i]);
			correct_grid_indices[i] = grid_indices[i] + offset;
		}

		//[1234567]->[[123][234][345][456][567]]
		int chop_size = 3;
		final int[][] chopped_correct_grid_indices = new int[melF.length-(chop_size-1)][chop_size];

		for(int i=0; i< melF.length-(chop_size-1); i++)
		{
			chopped_correct_grid_indices[i][0] = correct_grid_indices[i];
			chopped_correct_grid_indices[i][1] = correct_grid_indices[i+1];
			chopped_correct_grid_indices[i][2] = correct_grid_indices[i+2];
		}

		double[][] banks = new double[melF.length-(chop_size-1)][257];

		for(int i=0; i< melF.length-(chop_size-1); i++)
		{
			double lower_spacing = 1.0/(chopped_correct_grid_indices[i][1]-chopped_correct_grid_indices[i][0]);
			double upper_spacing = 1.0/(chopped_correct_grid_indices[i][2]-chopped_correct_grid_indices[i][1]);

			for(int j=0;j<257;j++)
			{

				banks[i][j] = 0;
				if(j==chopped_correct_grid_indices[i][0])
				{
					for(j=chopped_correct_grid_indices[i][0];j<chopped_correct_grid_indices[i][1];j++)
					{
						banks[i][j] = (j-chopped_correct_grid_indices[i][0])*lower_spacing;
					}
				}
				if(j==chopped_correct_grid_indices[i][1])
				{
					for(j=chopped_correct_grid_indices[i][1];j<chopped_correct_grid_indices[i][2];j++)
					{
						banks[i][j] = 1-((j-chopped_correct_grid_indices[i][1])*upper_spacing);
					}
				}

			}
		}
		return banks;

		//need to check if there's an empty channel somewhere
	}



//fft frequencies, librosa
	private double[] fftFreq() {
		//Alternative implementation of np.fft.fftfreqs
		double[] freqs = new double[1+n_fft/2];
		for (int i = 0; i < 1+n_fft/2; i++){
				freqs[i] = 0 + (sampleRate/2)/(n_fft/2) * i;
		}
		return freqs;
	}

//mel frequencies, librosa
	private double[] melFreq(int numMels) {
		//'Center freqs' of mel bands - uniformly spaced between limits
		double[] LowFFreq = new double[1];
		double[] HighFFreq = new double[1];
		LowFFreq[0] = fMin;
		HighFFreq[0] = fMax;
		final double[] melFLow    = freqToMel(LowFFreq);
    	final double[] melFHigh   = freqToMel(HighFFreq);
		double[] mels = new double[numMels];
		for (int i = 0; i < numMels; i++) {
			mels[i] = melFLow[0] + (melFHigh[0] - melFLow[0]) / (numMels-1) * i;
		}

		return melToFreq(mels);

	}



	private double[] melToFreq(double[] mels) {
		double[] freqs = new double[mels.length];

		for (int i = 0; i < mels.length; i++) {
			freqs[i] = 700. * (Math.exp(mels[i] / 1127.) - 1.);
		}

		return freqs;
	}

	protected double[] freqToMel(double[] freqs) {
		double[] mels = new double[freqs.length];
		for (int i = 0; i < freqs.length; i++) {
			mels[i] = 1127. * Math.log(1. + freqs[i] / 700.);
		}
		return mels;
	}


// log10
	private double log10(double value) {
		return Math.log(value) / Math.log(10);
	}
}
