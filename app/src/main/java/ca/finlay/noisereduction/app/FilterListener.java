package ca.finlay.noisereduction.app;

import android.graphics.Bitmap;

/**
 * Interface for interacting with Asynchronous Task
 */
public interface FilterListener {

    public void progressUpdate(double value);
    public void onComplete(Bitmap result);
}
