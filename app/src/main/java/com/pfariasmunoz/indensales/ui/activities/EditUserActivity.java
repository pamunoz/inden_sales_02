package com.pfariasmunoz.indensales.ui.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.FbContract.UserEntry;
import com.pfariasmunoz.indensales.data.models.IndenUser;
import com.pfariasmunoz.indensales.ui.ForegroundImageView;
import com.pfariasmunoz.indensales.utils.Constants;
import com.pfariasmunoz.indensales.utils.TextHelper;
import com.satsuware.usefulviews.LabelledSpinner;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class EditUserActivity extends AppCompatActivity
        implements LabelledSpinner.OnItemChosenListener {

    @BindView(R.id.img_user_bg)
    ForegroundImageView mProfileBackgroundImageView;
    @BindView(R.id.img_user_profile_pic)
    CircleImageView mUserProfileImageView;
    @BindView(R.id.et_user_name)
    TextInputEditText mUserNameEditText;
    @BindView(R.id.et_user_email)
    TextInputEditText mUserEmailEditText;
    @BindView(R.id.et_user_rut)
    TextInputEditText mUserRutEditText;
    @BindView(R.id.et_user_phone)
    TextInputEditText mUserPhoneEditText;
    @BindView(R.id.sp_user_role)
    LabelledSpinner mUserRoleSpinner;

    private String mUserId;
    private ValueEventListener mUserListener;
    private Query mUserQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        ButterKnife.bind(this);

        setupSpinner(mUserRoleSpinner);
        mProfileBackgroundImageView.setForegroundResource(R.color.color_dark_grey_primary);
        mUserId = getIntent().getStringExtra(Constants.USER_ID_KEY);
        mUserQuery = UserEntry.sRef.child(mUserId);
        handleInput();
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

    private void setupSpinner(LabelledSpinner spinner) {
        spinner.setItemsArray(R.array.users_roles_array);
        spinner.setOnItemChosenListener(this);
    }

    private void bindUserData(IndenUser user) {
        if (user.getPhotoUrl() != null) {
            Glide.with(this).load(user.getPhotoUrl()).into(mProfileBackgroundImageView);
        } else {
            mProfileBackgroundImageView.setImageResource(R.drawable.ic_person_black_24dp);
            mProfileBackgroundImageView.setColorFilter(ContextCompat.getColor(this,R.color.color_light_grey_primary));
        }
        if (user.getPhotoUrl() != null) {
            Glide.with(this).load(user.getPhotoUrl()).into(mUserProfileImageView);
        }else {
            mUserProfileImageView.setImageResource(R.drawable.ic_account_circle_black_24dp);
            mUserProfileImageView.setColorFilter(ContextCompat.getColor(this,R.color.color_light_grey_primary));
        }
        if (user.getNombre() != null) {
            String name = TextHelper.capitalizeFirestLetter(user.getNombre());
            mUserNameEditText.setText(name);
        }
        if (user.getEmail() != null) {
            mUserEmailEditText.setText(user.getEmail());
        }
        if (user.getRut() != null) {
            mUserRutEditText.setText(user.getRut());
        }
        if (user.getTelefono() != null) {
            mUserPhoneEditText.setText(user.getTelefono());
        }
        if (user.getRol() != null) {
            String userRole = user.getRol();
            int selection;
            switch (userRole) {
                case UserEntry.USER_ROLE_ADMIN:
                    selection = 2;
                    break;
                case UserEntry.USER_ROLE_SALESCLERK:
                    selection = 1;
                    break;
                default:
                    selection = 0;
            }
            mUserRoleSpinner.setSelection(selection);
        }





    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUserListener != null) {
            mUserQuery.removeEventListener(mUserListener);
        }
    }


    @Override
    public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
        final String selectedText = adapterView.getItemAtPosition(position).toString();
        switch (labelledSpinner.getId()) {
            case R.id.sp_user_role:
            UserEntry.getRefUserRole(mUserId).setValue(getRole(position)).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    String firstPartMessage = getResources().getString(R.string.message_spinner_user_role);
                    Toast.makeText(EditUserActivity.this, firstPartMessage + " " + selectedText, Toast.LENGTH_SHORT).show();
                }
            });

                break;
        }

    }

    @Override
    public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {

    }

    /**
     * Entrega el rol del usuario de acuerdo a la posision del adapter del {@link LabelledSpinner}
     * @param position del adapter.
     * @return el valor numerico del rol del usuario.
     */
    private String getRole(int position) {
        String role;
        switch (position) {
            case 2:
                role = UserEntry.USER_ROLE_ADMIN;
                break;
            case 1:
                role = UserEntry.USER_ROLE_SALESCLERK;
                break;
            default:
                role = UserEntry.USER_ROLE_GUEST;
        }
        return role;

    }



    private void handleInput() {
        mUserNameEditText.setOnEditorActionListener(getListener(UserEntry.getRefUserName(mUserId)));

        mUserEmailEditText.setOnEditorActionListener(getListener(UserEntry.getRefUserEmail(mUserId)));
        mUserPhoneEditText.setOnEditorActionListener(getListener(UserEntry.getRefUserPhone(mUserId)));

        mUserRutEditText.setOnEditorActionListener(getListener(UserEntry.getRefUserRut(mUserId)));
    }

    private TextView.OnEditorActionListener getListener(final DatabaseReference ref) {

        return new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(final TextView v, int actionId, KeyEvent event) {

                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    ref.setValue(v.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            String message = getValueSaved(v.getId()) + " " + getResources().getString(R.string.value);;
                            Toast.makeText(EditUserActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    });
                    handled = true;
                }

                return handled;
            }
        };
    }

    private String getValueSaved(int viewId) {
        String valueSaved;
        switch (viewId) {
            case R.id.et_user_name:
                valueSaved = getResources().getString(R.string.label_full_name);
                break;
            case R.id.et_user_email:
                valueSaved = getResources().getString(R.string.label_email);
                break;
            case R.id.et_user_phone:
                valueSaved = getResources().getString(R.string.label_phone);
                break;
            case R.id.et_user_rut:
                valueSaved = getResources().getString(R.string.label_rut);
                break;
            default:
                valueSaved = getResources().getString(R.string.value);
        }
        return valueSaved;
    }
}
