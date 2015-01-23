package ca.finlay.noisereduction.app;

import android.graphics.Bitmap;
import android.graphics.Rect;

import java.util.Arrays;
import java.util.Observable;

/**
 * Filters pixel by mean value of area
 */
public class MeanFilter extends AbstractFilter {

    public MeanFilter(Bitmap original, int window_size) {
        super(original, window_size);
    }

    protected int extractValue(int[] pixels)
    {
        int R = 0;
        int G = 0;
        int B = 0;
        int A = 0;

        for (int i : pixels)
        {
            A += (i >> 24) & 0xff;
            R += (i >> 16) & 0xff;
            G += (i >> 8) & 0xff;
            B += i & 0xff;
        }
        A /= pixels.length;
        R /= pixels.length;
        G /= pixels.length;
        B /= pixels.length;

        return (A << 24) | (R << 16) | (G << 8) | B;
    }
}

