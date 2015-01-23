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
        Arrays.sort(pixels);
        return pixels[pixels.length / 2];
    }
}

