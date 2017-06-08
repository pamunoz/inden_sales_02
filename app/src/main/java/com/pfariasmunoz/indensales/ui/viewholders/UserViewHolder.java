package com.pfariasmunoz.indensales.ui.viewholders;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.DbContract;
import com.pfariasmunoz.indensales.data.models.IndenUser;
import com.pfariasmunoz.indensales.utils.TextHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Pablo Farias on 05-06-17.
 */

public class UserViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.imv_user_profile_pic)
    CircleImageView mUserPhotoImageView;
    @BindView(R.id.tv_user_name)
    TextView mUserNameTextView;
    @BindView(R.id.tv_user_rut)
    TextView mUserRutTextView;
    @BindView(R.id.tv_user_email)
    TextView mUserEmailTextView;
    @BindView(R.id.tv_user_phone)
    TextView mUserPhoneTextView;
    @BindView(R.id.tv_user_rol)
    TextView mUserRolTextView;
    @BindView(R.id.bt_add_clients)
    Button mAddClientsToUserButton;
    @BindView(R.id.imb_edit_user)
    ImageButton mEditUserButton;

    public UserViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(IndenUser user) {
        if (user.getPhotoUrl() != null) {
            Glide.with(itemView.getContext()).load(user.getPhotoUrl()).into(mUserPhotoImageView);
        }
        String userName = TextHelper.capitalizeFirestLetter(user.getNombre());
        mUserNameTextView.setText(userName);
        mUserRutTextView.setText(user.getRut());
        String userEmail = user.getEmail().toLowerCase();
        mUserEmailTextView.setText(userEmail);
        mUserPhoneTextView.setText(user.getTelefono());
        String userRole = getUserRole(user.getRole());
        mUserRolTextView.setText(userRole);
    }

    private String getUserRole(int role) {
        Resources res = itemView.getContext().getResources();
        String userRole;
        if (role == DbContract.USER_ROLE_ADMIN) {
            userRole = res.getString(R.string.user_rol_admin);
        } else if (role == DbContract.USER_ROLE_SALESCLERK) {
            userRole = res.getString(R.string.user_rol_salesclerk);
        } else {
            userRole = res.getString(R.string.user_rol_guest);
        }
        return userRole;
    }

    public Button getAddClientsToUserButton() {
        return mAddClientsToUserButton;
    }

    public ImageButton getEditUserButton() {
        return mEditUserButton;
    }
}
