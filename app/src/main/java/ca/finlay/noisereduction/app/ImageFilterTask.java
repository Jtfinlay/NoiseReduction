package ca.finlay.noisereduction.app;

import android.graphics.Bitmap;
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

        int width = orig.getWidth();
        int height = orig.getHeight();

        Bitmap newBitmap = Bitmap.createBitmap(orig);

        int left, right, bottom, top;
        for (int x = 0; x < width; x++)
        {
            left = x - (window_len-1)/2;
            right = x + (window_len-1)/2;
            left = left < 0 ? 0 : left;
            right = right > width ? width : right;

            for (int y = 0; y < height; y++)
            {
                top = y - (window_len-1)/2;
                bottom = y + (window_len-1)/2;
                top = top < 0 ? 0 : top;
                bottom = bottom > height ? height : bottom;
                int window_size = (bottom-top)*(right-left);

                int[] pixels = new int[window_size];
                orig.getPixels(pixels, 0, right-left,  left, top, right-left, bottom-top);
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

            if (x % (width/100) == 0)
                publishProgress(Double.valueOf(x/(width/100)));
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
        return 0;
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
