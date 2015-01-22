package ca.finlay.noisereduction.app;

import android.graphics.Bitmap;

/**
 * Created by James on 1/21/2015.
 */
public interface FilterListener {

    public void progressUpdate(double value);
    public void onComplete(Bitmap result);
}
