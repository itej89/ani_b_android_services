package Framework.Helpers;

import java.util.Arrays;

public class Chunk {

    public static int[ ][ ] chunkify(int[ ] fft, double windowSize, double samplingRate) {

        int _windowSize = (int) (windowSize * samplingRate);

        int numWindows = fft.length / _windowSize;

        int[ ][ ] ffts = new int[numWindows][ ];

        for (int i = 0; i < numWindows; i++) {

            int start = i * _windowSize;

            int end = (i + 1) * _windowSize;

            int[ ] f = Arrays.copyOfRange(fft, start, end);

            ffts[i] = f;

        }

        return ffts;

    }
}
