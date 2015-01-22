package ca.finlay.noisereduction.app;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.util.Log;

import java.util.Arrays;
import java.util.logging.Filter;

/**
 * Created by James on 1/14/2015.
 */
public class ImageFilterTask extends AsyncTask<Object, Double, Bitmap> {

    public static final int MEDIAN = 1;
    public static final int MEAN = 2;

    private FilterListener _parent;


    @Override
    protected Bitmap doInBackground(Object... objects) {
        Bitmap orig = (Bitmap) objects[0];
        int window_len = (Integer) objects[1];
        _parent = (FilterListener) objects[2];
        int filter_type = (Integer) objects[3];

        Bitmap newBitmap = Bitmap.createBitmap(orig);
        Rect rWindow = new Rect();
        Rect rOrig = new Rect(0, 0, orig.getWidth(), orig.getHeight());

        for (int x = 0; x < orig.getWidth(); x++)
        {
            rWindow.left = x - (window_len-1)/2;
            rWindow.right = x + (window_len-1)/2;

            for (int y = 0; y < orig.getHeight(); y++)
            {
                rWindow.top = y - (window_len-1)/2;
                rWindow.bottom = y + (window_len-1)/2;
                rWindow.intersect(rOrig);

                int[] pixels = new int[rWindow.width() * rWindow.height()];
                orig.getPixels(pixels, 0, rWindow.width(),  rWindow.left, rWindow.top,
                        rWindow.width(), rWindow.height());
                switch (filter_type)
                {
                    case MEDIAN:
                        newBitmap.setPixel(x, y, extractMedianValue(pixels));
                        break;
                    case MEAN:
                        newBitmap.setPixel(x,y,extractMeanValue(pixels));
                        break;
                }
            }

            if (x % (rOrig.width()/100) == 0)
                publishProgress(Double.valueOf(x/(rOrig.width()/100)));
        }

        return newBitmap;
    }

    protected int extractMedianValue(int[] pixels)
    {
        Arrays.sort(pixels);
        return pixels[pixels.length / 2];
    }

    protected int extractMeanValue(int[] pixels)
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

    @Override
    protected void onProgressUpdate(Double... values)
    {
        _parent.progressUpdate(values[0]);
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        _parent.onComplete(result);
    }

}
