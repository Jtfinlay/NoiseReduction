package ca.finlay.noisereduction.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


/**
 * Main Activity containing the view for the image
 */
public class MainActivity extends ActionBarActivity implements FilterListener {

    private static final int LOAD_ID = 1;

    private Bitmap _bitMap;
    private Button _btnMeanFilter;
    private Button _btnMedianFilter;
    private ImageView _imageView;

    private ImageFilterTask _filter;
    private ProgressDialog _progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _imageView = (ImageView) findViewById(R.id.image_area);
        _btnMeanFilter = (Button) findViewById(R.id.btnMeanFilter);
        _btnMedianFilter = (Button) findViewById(R.id.btnMedianFilter);

        _btnMeanFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyFilter(FilterFactory.createMeanFilter(getApplicationContext(), _bitMap));
            }
        });
        _btnMedianFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                applyFilter(FilterFactory.createMedianFilter(getApplicationContext(),_bitMap));
            }
        });
    }

    private void applyFilter(AbstractFilter filter)
    {
        _filter = new ImageFilterTask();
        _progress = new ProgressDialog(this);
        _progress.setMessage("Applying filter...");
        _progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        _progress.setCancelable(false);
        _filter.execute(filter, this);
        _progress.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.action_settings:
            {
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivityForResult(intent, LOAD_ID);
                return true;
            }
            case R.id.action_load:
            {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, LOAD_ID);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == LOAD_ID && resultCode == Activity.RESULT_OK)
        {
            loadImage(data.getData());
        }
    }

    /**
     * Load image from uri and draw to the ImageView
     * @param data - input stream uri
     */
    private void loadImage(Uri data)
    {
        InputStream stream = null;
        try {
            stream = getContentResolver().openInputStream(data);
            _bitMap = BitmapFactory.decodeStream(stream);

            _imageView.setImageBitmap(_bitMap);
            _btnMeanFilter.setVisibility(View.VISIBLE);
            _btnMedianFilter.setVisibility(View.VISIBLE);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void progressUpdate(double value) {
        _progress.setProgress((int) value);
    }

    @Override
    public void onComplete(Bitmap result) {
        _progress.setMessage("Filter Complete!");
        _progress.dismiss();
        _bitMap = result;
        _imageView.setImageBitmap(_bitMap);
    }
}
