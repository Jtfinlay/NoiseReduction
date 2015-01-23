package ca.finlay.noisereduction.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

/**
 * Filter creation factory
 */
public class FilterFactory {

        public static MedianFilter createMedianFilter(Context c, Bitmap image)
        {
            SharedPreferences sharedPref = c.
                    getSharedPreferences(SettingsActivity.SHARED_PREFERENCES, Context.MODE_PRIVATE);
            int window_size = sharedPref.getInt(SettingsActivity.MEDIAN_SETTINGS, 3);
            return new MedianFilter(image, window_size);
        }

        public static MeanFilter createMeanFilter(Context c, Bitmap image)
        {
            SharedPreferences sharedPref = c.
                    getSharedPreferences(SettingsActivity.SHARED_PREFERENCES, Context.MODE_PRIVATE);
            int window_size = sharedPref.getInt(SettingsActivity.MEAN_SETTINGS, 3);
            return new MeanFilter(image, window_size);
        }

}
