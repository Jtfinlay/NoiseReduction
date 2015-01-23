package ca.finlay.noisereduction.app;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;

import java.util.List;
import java.util.Observable;

/**
 * Abstract object for filter application
 */
public abstract class AbstractFilter extends Observable {

    protected Bitmap _orig;
    protected int _window_size;

    protected abstract int extractValue(int[] pixels);

    public AbstractFilter(Bitmap original, int window_size)
    {
        _orig = original;
        _window_size = window_size;
    }

    public Bitmap startFilter()
    {
        Bitmap newBitmap = Bitmap.createBitmap(_orig);
        Rect rWindow = new Rect();
        Rect rOrig = new Rect(0, 0, _orig.getWidth(), _orig.getHeight());

        for (int x = 0; x < _orig.getWidth(); x++)
        {
            rWindow.left = x - (_window_size-1)/2;
            rWindow.right = x + (_window_size-1)/2;

            for (int y = 0; y < _orig.getHeight(); y++)
            {
                rWindow.top = y - (_window_size-1)/2;
                rWindow.bottom = y + (_window_size-1)/2;
                rWindow.intersect(rOrig);

                int[] pixels = new int[rWindow.width() * rWindow.height()];
                _orig.getPixels(pixels, 0, rWindow.width(),  rWindow.left, rWindow.top,
                        rWindow.width(), rWindow.height());
                newBitmap.setPixel(x, y, extractValue(pixels));

            }

            if (x % (rOrig.width()/100) == 0)
            {
                setChanged();
                notifyObservers(Double.valueOf(x / (rOrig.width() / 100)));
            }
        }

        return newBitmap;
    }

}
