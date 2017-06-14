package com.pfariasmunoz.indensales.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import com.pfariasmunoz.indensales.R;

/**
 * Created by Pablo Farias on 07-06-17.
 */

public class SearchableActivity extends BaseActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
