package com.pfariasmunoz.indensales.ui.viewholders;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.models.Sale;
import com.pfariasmunoz.indensales.utils.MathHelper;
import com.pfariasmunoz.indensales.utils.TextHelper;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SaleViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_total_price_sale)
    TextView mTotalSalePriceTextView;
    @BindView(R.id.tv_client_name_sale)
    TextView mClientNameSaleTextView;
    @BindView(R.id.tv_client_rut_sale)
    TextView mClientRutSaleTextView;
    @BindView(R.id.tv_client_address_sale)
    TextView mClientAddressSaleTextView;
    @BindView(R.id.tv_date)
    TextView mDateSaleTextView;
    @BindView(R.id.tv_time)
    TextView mTimeSaleTextView;
//    @BindView(R.id.tv_aprob_sale)
//    TextView mAprobSaleTextView;
    @BindView(R.id.sw_aprob_sale_state)
    Switch mAprobSaleStateSwitch;
    @BindView(R.id.bt_see_articles_in_sale)
    Button mSeeArticlesInSalesButton;

    public SaleViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(Sale report) {
        if (report.getTotal_con_descuento() > 0) {
            long totalWithDiscount = (long) report.getTotal_con_descuento();
            String total = String.valueOf(totalWithDiscount);
            String stringTotal = MathHelper.getLocalCurrency(total);
            mTotalSalePriceTextView.setText(stringTotal);
        } else {
            mTotalSalePriceTextView.setText(String.valueOf(0));
        }
        if (report.getNombre_cliente() != null) {
            String stringClientName = TextHelper.capitalizeFirestLetter(report.getNombre_cliente());
            mClientNameSaleTextView.setText(stringClientName);
        }
        if (report.getDireccion() != null) {
            String stringAddress = TextHelper.capitalizeFirestLetter(report.getDireccion());
            mClientAddressSaleTextView.setText(stringAddress);
        }
        if (report.getRut_cliente() != null) {
            mClientRutSaleTextView.setText(report.getRut_cliente());
        }

        String date = TextHelper.formatDate(report.getTimestamp());
        String time = TextHelper.formatTime(report.getTimestamp());
        mDateSaleTextView.setText(date);
        mTimeSaleTextView.setText(time);
        mAprobSaleStateSwitch.setChecked(report.isAprob());
        if (report.isAprob()) {
            mAprobSaleStateSwitch.setTextColor(Color.GREEN);
        } else {
            mAprobSaleStateSwitch.setTextColor(Color.RED);
        }
    }

    public Button getArticlesInSalesButton() {
        return mSeeArticlesInSalesButton;
    }

    public Switch getAprobSaleStateSwitch() {
        return mAprobSaleStateSwitch;
    }
}
