package com.pfariasmunoz.indensales.ui.viewholders;

import android.support.v7.widget.RecyclerView;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.models.Article;
import com.pfariasmunoz.indensales.data.models.ArticleSale;
import com.pfariasmunoz.indensales.utils.MathHelper;
import com.pfariasmunoz.indensales.utils.TextHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleViewHolder extends BaseArticleViewHolder {

//    @BindView(R.id.iv_article_image)
//    ImageView mArticleImageView;
    @BindView(R.id.tv_article_total_price)
    TextView mArticlesTotalPriceTextView;
    @BindView(R.id.tv_article_amount)
    TextView mArticlesAmountTextView;
    @BindView(R.id.imb_up_arrow)
    ImageButton mAddArticleButton;
    @BindView(R.id.imb_down_arrow)
    ImageButton mSubtractArticleButton;

    public ArticleViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(Article article, ArticleSale articleSale, String code) {
        String stringDescription = TextHelper.capitalizeFirestLetter(article.descripcion);
        mArticleDescriptionTextView.setText(stringDescription);
        String stringArticlePrice = MathHelper.getLocalCurrency(article.precio);
        mArticlePriceTextView.setText(stringArticlePrice);
        mArticleCodeTextView.setText(code);
        String stringArticleTotalPrice = MathHelper.getLocalCurrency(String.valueOf(articleSale.total));
        mArticlesTotalPriceTextView.setText(stringArticleTotalPrice);
        mArticlesAmountTextView.setText(String.valueOf(articleSale.cantidad));
    }

    public void bindArticleInSale(ArticleSale articleSale) {
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
        mSubtractArticleButton.setVisibility(View.GONE);
        mAddArticleButton.setVisibility(View.GONE);
        mArticleCodeTextView.setText(articleSale.idarticulo);


    }

    public ImageButton getAddArticleButton() {
        return mAddArticleButton;
    }

    public ImageButton getSubtractArticleButton() {
        return mSubtractArticleButton;
    }
}
