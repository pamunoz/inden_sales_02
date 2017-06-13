package com.pfariasmunoz.indensales.ui.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.pfariasmunoz.indensales.R;

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

    public BaseArticleViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
