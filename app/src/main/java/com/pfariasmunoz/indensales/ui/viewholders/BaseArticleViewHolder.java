package com.pfariasmunoz.indensales.ui.viewholders;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.models.Article;
import com.pfariasmunoz.indensales.data.models.ArticleSale;
import com.pfariasmunoz.indensales.utils.MathHelper;
import com.pfariasmunoz.indensales.utils.TextHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Pablo Farias on 13-06-17.
 */

public class BaseArticleViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tv_article_code)
    TextView mArticleCodeTextView;
    @BindView(R.id.tv_article_description)
    TextView mArticleDescriptionTextView;
    @BindView(R.id.tv_article_price)
    TextView mArticlePriceTextView;
    @BindView(R.id.tv_article_total_price)
    TextView mArticlesTotalPriceTextView;
    @BindView(R.id.tv_article_amount)
    TextView mArticlesAmountTextView;


    public BaseArticleViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(@NonNull ArticleSale articleSale, @Nullable Article article, @Nullable String code) {
        String stringDescription = TextHelper.capitalizeFirestLetter(articleSale.descripcion);
        long precio = articleSale.total / articleSale.cantidad;
        String stringPrice = String.valueOf(precio);
        String stringArticlePrice = MathHelper.getLocalCurrency(stringPrice);
        mArticlePriceTextView.setText(stringArticlePrice);
        mArticleDescriptionTextView.setText(stringDescription);
        String stringArticleTotalPrice = MathHelper.getLocalCurrency(String.valueOf(articleSale.total));
        mArticlesTotalPriceTextView.setText(stringArticleTotalPrice);
        mArticlesAmountTextView.setText(String.valueOf(articleSale.cantidad));
        mArticlesAmountTextView.setTextSize(24F);
        mArticleCodeTextView.setText(articleSale.idarticulo);
    }
}
