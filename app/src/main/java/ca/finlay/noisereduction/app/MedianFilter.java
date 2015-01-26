package ca.finlay.noisereduction.app;

import android.graphics.Bitmap;
import android.graphics.Rect;

import java.util.Arrays;
import java.util.Observable;

/**
 * Filters pixel by median value of area
 */
public class MedianFilter extends AbstractFilter {

    public MedianFilter(Bitmap original, int window_size) {
        super(original, window_size);
    }

    protected int extractValue(int[] pixels)
    {
        int[] R = new int[pixels.length];
        int[] G = new int[pixels.length];
        int[] B = new int[pixels.length];
        int[] A = new int[pixels.length];

        for (int i=0; i<pixels.length; i++)
        {
            A[i] = (pixels[i] >> 24) & 0xff;
            R[i] = (pixels[i] >> 16) & 0xff;
            G[i] = (pixels[i] >> 8) & 0xff;
            B[i] = (pixels[i]) & 0xff;
        }
        Arrays.sort(A);
        Arrays.sort(R);
        Arrays.sort(G);
        Arrays.sort(B);

        return (A[A.length/2] << 24) | (R[R.length/2] << 16) | (G[G.length/2] << 8) | B[B.length/2];
    }
}

