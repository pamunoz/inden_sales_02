package com.pfariasmunoz.indensales.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.pfariasmunoz.indensales.R;

import timber.log.Timber;

public class EditUserActivity extends AppCompatActivity {

    private String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        mUserId = getIntent().getStringExtra("user_id");
        Timber.i("The user id is: " + mUserId);
    }
}
