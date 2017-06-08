package com.pfariasmunoz.indensales.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.DbContract;
import com.pfariasmunoz.indensales.data.FirebaseDb;
import com.pfariasmunoz.indensales.data.models.IndenUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import timber.log.Timber;

public class EditUserActivity extends AppCompatActivity {

    @BindView(R.id.img_user_bg)
    ImageView mProfileBackgroundImageView;
    @BindView(R.id.img_user_profile_pic)
    CircleImageView mUserProfileImageView;
    @BindView(R.id.et_user_name)
    EditText mUserNameEditText;
    @BindView(R.id.et_user_email)
    EditText mUserEmailEditText;
    @BindView(R.id.et_user_rut)
    EditText mUserRutEditText;
    @BindView(R.id.et_user_phone)
    EditText mUserPhoneEditText;
    @BindView(R.id.sp_user_role)
    Spinner mUserRoleSpinner;

    private String mUserId;
    private ValueEventListener mUserListener;
    private Query mUserQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        ButterKnife.bind(this);

        mUserId = getIntent().getStringExtra("user_id");
        mUserQuery = FirebaseDb.sUsers.child(mUserId);
        setupUserListener();
    }

    private void setupUserListener() {
        mUserListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getValue() != null) {
                    IndenUser user = dataSnapshot.getValue(IndenUser.class);
                    bindUserData(user);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mUserQuery.addListenerForSingleValueEvent(mUserListener);
    }

    private void bindUserData(IndenUser user) {
        if (user.getPhotoUrl() != null) {
            Glide.with(this).load(user.getPhotoUrl()).into(mProfileBackgroundImageView);
        }
        if (user.getPhotoUrl() != null) {
            Glide.with(this).load(user.getPhotoUrl()).into(mUserProfileImageView);
        }
        mUserNameEditText.setHint(user.getNombre());
        mUserEmailEditText.setHint(user.getEmail());
        mUserRutEditText.setHint(user.getRut());
        mUserPhoneEditText.setHint(user.getTelefono());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUserListener != null) {
            mUserQuery.removeEventListener(mUserListener);
        }
    }
}
