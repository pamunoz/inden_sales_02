package com.pfariasmunoz.indensales.ui.viewholders;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

    @BindView(R.id.imb_up_arrow)
    ImageButton mAddArticleButton;
    @BindView(R.id.imb_down_arrow)
    ImageButton mSubtractArticleButton;

    public ArticleViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bind(@NonNull ArticleSale articleSale, @Nullable Article article, @Nullable String code) {
        if (article != null) {
            if (article.getDescripcion() != null) {
                String stringDescription = TextHelper.capitalizeFirestLetter(article.getDescripcion());
                mArticleDescriptionTextView.setText(stringDescription);
            }
            if (article.getPrecio() != null) {
                String stringArticlePrice = MathHelper.getLocalCurrency(article.getPrecio());
                mArticlePriceTextView.setText(stringArticlePrice);
            }
        }



        if (code != null) {
            mArticleCodeTextView.setText(code);
        }

        if (articleSale.getTotal() != null) {
            String stringArticleTotalPrice = MathHelper.getLocalCurrency(String.valueOf(articleSale.getTotal()));
            mArticlesTotalPriceTextView.setText(stringArticleTotalPrice);
        }
        if (articleSale.getCantidad() > 0) {
            mArticlesAmountTextView.setText(String.valueOf(articleSale.getCantidad()));
        } else {
            mArticlesAmountTextView.setText(String.valueOf(0));
        }



    }

    public ImageButton getAddArticleButton() {
        return mAddArticleButton;
    }

    public ImageButton getSubtractArticleButton() {
        return mSubtractArticleButton;
    }
}
