package ca.finlay.noisereduction.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


public class SettingsActivity extends ActionBarActivity {


    public static final String MEAN_SETTINGS = "MEAN_SETTING";
    public static final String MEDIAN_SETTINGS = "MEDIAN_SETTING";
    public static final String SHARED_PREFERENCES = "SHARED_PREFERENCES";


    private SeekBar _seekMean, _seekMedian;
    private TextView _txtMean, _txtMedian;
    private Button _btnApply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final SharedPreferences sharedPref = this.getApplicationContext().
                getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);

        _seekMean = (SeekBar) findViewById(R.id.seek_mean);
        _seekMedian = (SeekBar) findViewById(R.id.seek_median);
        _txtMean = (TextView) findViewById(R.id.txtMean);
        _txtMedian = (TextView) findViewById(R.id.txtMedian);
        _btnApply = (Button) findViewById(R.id.btnApply);

        _seekMean.setMax(41);
        _seekMean.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress % 2 == 0) progress++;
                if (progress == 1) progress = 3;
                _txtMean.setText("Mean Filter Size: " + progress + "px");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        _seekMean.setProgress(sharedPref.getInt(MEAN_SETTINGS, 3));

        _seekMedian.setMax(41);
        _seekMedian.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress % 2 == 0) progress++;
                if (progress == 1) progress = 3;
                _txtMedian.setText("Median Filter Size: " + progress + "px");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        _seekMedian.setProgress(sharedPref.getInt(MEDIAN_SETTINGS, 3));

        _btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt(MEAN_SETTINGS, _seekMean.getProgress());
                editor.putInt(MEDIAN_SETTINGS, _seekMedian.getProgress());
                editor.commit();
                finish();
            }
        });
    }

}
