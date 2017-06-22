package com.pfariasmunoz.indensales.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.pfariasmunoz.indensales.data.FbContract;
import com.pfariasmunoz.indensales.data.models.IndenUser;

/**
 * Created by Pablo Farias on 14-06-17.
 */

public class BaseActivity extends AppCompatActivity {

    private DatabaseReference mUserReference;
    private ValueEventListener mUsersListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public String getUid() {
        return FbContract.getUserId();
    }

    public IndenUser getIndenUser(FirebaseUser user) {
        return FbContract.getIndenUser(user);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mUsersListener != null) {
            mUserReference.removeEventListener(mUsersListener);
        }
    }

    public void writeNewUserIfNeeded() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            mUserReference = FbContract.UserEntry.sRef.child(getUid());
            mUsersListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        IndenUser indenUser = getIndenUser(user);
                        mUserReference.setValue(indenUser);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mUserReference.addListenerForSingleValueEvent(mUsersListener);
        }


    }
}
