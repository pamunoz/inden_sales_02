package com.pfariasmunoz.indensales.ui.viewholders;

import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.FbContract.UserEntry;
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
    @BindView(R.id.ib_popup_menu)
    ImageButton mPopupMenuButton;

    public UserViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(IndenUser user) {
        if (user.getPhotoUrl() != null) {
            Glide.with(itemView.getContext()).load(user.getPhotoUrl()).into(mUserPhotoImageView);
        } else {
            mUserPhotoImageView.setImageResource(R.drawable.ic_account_circle_black_24dp);
            mUserPhotoImageView.setColorFilter(ContextCompat.getColor(itemView.getContext(),R.color.color_light_grey_primary));
        }
        if (user.getNombre() != null) {
            String userName = TextHelper.capitalizeFirestLetter(user.getNombre());
            mUserNameTextView.setText(userName);
        } else {
            mUserNameTextView.setVisibility(View.INVISIBLE);
        }
        if (user.getRut() != null) {
            mUserRutTextView.setText(user.getRut());
        } else {
            mUserRutTextView.setVisibility(View.INVISIBLE);
        }
        if (user.getEmail() != null) {
            String userEmail = user.getEmail().toLowerCase();
            mUserEmailTextView.setText(userEmail);
        }
        if (user.getTelefono() != null) {
            mUserPhoneTextView.setText(user.getTelefono());
        }
        if (user.getRol() != null) {
            String userRole = getUserRole(user.getRol());
            mUserRolTextView.setText(userRole);
        }

    }

    private String getUserRole(String role) {
        Resources res = itemView.getContext().getResources();
        String userRole;
        if (TextUtils.equals(role, UserEntry.USER_ROLE_ADMIN)) {
            userRole = res.getString(R.string.user_rol_admin);
        } else if (TextUtils.equals(role, UserEntry.USER_ROLE_SALESCLERK)) {
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

    public ImageButton getPopupMenuButton() {
        return mPopupMenuButton;
    }
}
