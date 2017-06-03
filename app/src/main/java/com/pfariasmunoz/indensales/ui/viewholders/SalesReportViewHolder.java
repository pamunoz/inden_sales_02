package com.pfariasmunoz.indensales.ui.viewholders;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.models.Address;
import com.pfariasmunoz.indensales.data.models.Client;
import com.pfariasmunoz.indensales.data.models.Sale;
import com.pfariasmunoz.indensales.data.models.SaleReport;
import com.pfariasmunoz.indensales.ui.activities.ArticlesInSaleActivity;
import com.pfariasmunoz.indensales.utils.MathHelper;
import com.pfariasmunoz.indensales.utils.TextHelper;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SalesReportViewHolder extends RecyclerView.ViewHolder {

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

    public SalesReportViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(SaleReport report) {
        String total = String.valueOf(report.total);
        String stringTotal = MathHelper.getLocalCurrency(total);
        String totalWithoutSimbol = stringTotal.substring(1);
        String stringClientName = TextHelper.capitalizeFirestLetter(report.nombre_cliente);
        String stringAddress = TextHelper.capitalizeFirestLetter(report.direccion);
        mTotalSalePriceTextView.setText(totalWithoutSimbol);
        mClientNameSaleTextView.setText(stringClientName);
        mClientRutSaleTextView.setText(report.rut_cliente);
        mClientAddressSaleTextView.setText(stringAddress);
        String date = TextHelper.formatDate(report.timestamp);
        String time = TextHelper.formatTime(report.timestamp);
        mDateSaleTextView.setText(date);
        mTimeSaleTextView.setText(time);
        mAprobSaleStateSwitch.setChecked(report.aprob);
        if (report.aprob) {
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
