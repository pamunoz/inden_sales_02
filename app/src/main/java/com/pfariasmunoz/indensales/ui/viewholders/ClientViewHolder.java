package com.pfariasmunoz.indensales.ui.viewholders;

import android.provider.UserDictionary;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.models.Client;
import com.pfariasmunoz.indensales.ui.activities.MainActivity;
import com.pfariasmunoz.indensales.utils.TextHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ClientViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tv_client_name)
    TextView mNameTextView;
    @BindView(R.id.tv_client_rut)
    TextView mRutTextView;
    @BindView(R.id.bt_add_sale)
    Button mAddSaleButton;
    @BindView(R.id.cb_is_client_added)
    CheckBox mIsClientAddedCheckBox;


    public ClientViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }


    public void bind(Client client) {
        if (client.nombre != null) {
            String clientName = TextHelper.capitalizeFirestLetter(client.nombre);
            mNameTextView.setText(clientName);
        }
        if (client.rut != null) {
            mRutTextView.setText(client.rut);
        }
    }


    @OnClick(R.id.imbt_client_location)
    public void showMap() {
        Toast.makeText(itemView.getContext(), "funcion no disponible", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.imbt_edit_client)
    public void goToEditClientActivity() {
        Toast.makeText(itemView.getContext(), "funcion no disponible", Toast.LENGTH_SHORT).show();
    }

    public Button getAddSaleButton() {
        return mAddSaleButton;
    }

    public CheckBox getIsClientAddedCheckBox() {
        return mIsClientAddedCheckBox;
    }
}
