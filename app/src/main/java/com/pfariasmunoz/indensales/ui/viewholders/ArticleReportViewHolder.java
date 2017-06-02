package com.pfariasmunoz.indensales.ui.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.models.ArticleSale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Pablo Farias on 02-06-17.
 */

public class ArticleReportViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_article_report_description)
    TextView mArticleReportDescriptionTextView;
    @BindView(R.id.tv_article_report_quantity)
    TextView mArticleReportAmountTextView;
    @BindView(R.id.tv_article_report_total)
    TextView mArticleReportTotalTextView;


    public ArticleReportViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(ArticleSale articleSale) {
        mArticleReportAmountTextView.setText(articleSale.cantidad);
        mArticleReportDescriptionTextView.setText(articleSale.descripcion);
        mArticleReportTotalTextView.setText(String.valueOf(articleSale.total));
    }
}
