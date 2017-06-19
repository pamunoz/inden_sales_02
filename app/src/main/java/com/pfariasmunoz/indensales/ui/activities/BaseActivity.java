package com.pfariasmunoz.indensales.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.pfariasmunoz.indensales.data.FbContract;
import com.pfariasmunoz.indensales.data.models.IndenUser;

/**
 * Created by Pablo Farias on 14-06-17.
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public String getUid() {
        return FbContract.getUserId();
    }

    public DatabaseReference getDb() {
        return FbContract.sDbRef;
    }

    public IndenUser getIndenUser(FirebaseUser user) {
        return FbContract.getIndenUser(user);
    }
}
