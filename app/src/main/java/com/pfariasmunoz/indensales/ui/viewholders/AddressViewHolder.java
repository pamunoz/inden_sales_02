package com.pfariasmunoz.indensales.ui.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.models.Address;
import com.pfariasmunoz.indensales.utils.TextHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Pablo Farias on 28-04-17.
 */

public class AddressViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_client_address)
    TextView mClientAddressTextView;
    @BindView(R.id.tv_client_phone)
    TextView mClientPhoneTextView;
    @BindView(R.id.tv_client_zone)
    TextView mClientZoneTextView;
    private View mRootView;

    public AddressViewHolder(View itemView) {
        super(itemView);
        mRootView = itemView;
        ButterKnife.bind(this, mRootView);
    }

    public void bind(Address address) {
        if (address.direccion != null && address.comuna != null && address.ciudad != null) {
            String address1 = TextHelper.capitalizeFirestLetter(address.direccion);
            String address2 = TextHelper.capitalizeFirestLetter(address.comuna);
            String address3 = TextHelper.capitalizeFirestLetter(address.ciudad);
            String longAddress = address1 + ", " + address2 + ", " + address3 + ".";
            mClientAddressTextView.setText(longAddress);
        }
        if (address.telefono != null) {
            mClientPhoneTextView.setText(address.telefono);
        }
        if (address.zona != null) {
            mClientZoneTextView.setText(address.zona);
        }
    }

    public View getRootView() {
        return mRootView;
    }
}
