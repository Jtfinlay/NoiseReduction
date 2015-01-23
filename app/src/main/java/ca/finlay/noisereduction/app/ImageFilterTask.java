package ca.finlay.noisereduction.app;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import java.util.Observable;
import java.util.Observer;

/**
 * Manages filtering operations asynchronously.
 */
public class ImageFilterTask extends AsyncTask<Object, Double, Bitmap> implements Observer {

    private FilterListener _parent;

    @Override
    protected Bitmap doInBackground(Object... objects) {
        AbstractFilter filter = (AbstractFilter) objects[0];
        _parent = (FilterListener) objects[1];
        filter.addObserver(this);
        return filter.startFilter();
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

    @Override
    public void update(Observable observable, Object data) {
        publishProgress(Double.valueOf((Double) data));
    }
}
