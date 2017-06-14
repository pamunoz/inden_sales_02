package com.pfariasmunoz.indensales.ui.activities;

import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.pfariasmunoz.indensales.data.FbContract;
import com.pfariasmunoz.indensales.data.models.IndenUser;

/**
 * Created by Pablo Farias on 14-06-17.
 */

public class BaseActivity extends AppCompatActivity {
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
