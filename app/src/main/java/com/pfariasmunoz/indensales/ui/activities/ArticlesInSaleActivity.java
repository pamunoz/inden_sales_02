package com.pfariasmunoz.indensales.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.models.ArticleSale;
import com.pfariasmunoz.indensales.ui.viewholders.ArticleViewHolder;

public class ArticlesInSaleActivity extends AppCompatActivity {

    private Query mArticlesInSaleQuery;
    private ValueEventListener mArticlesInSalesListener;
    private FirebaseRecyclerAdapter<ArticleSale, ArticleViewHolder> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles_in_sale);
    }

    private void setUpAdapter(Query query) {
        mAdapter = new FirebaseRecyclerAdapter<ArticleSale, ArticleViewHolder>(
                ArticleSale.class,
                R.layout.item_article_sale,
                ArticleViewHolder.class,
                query
        ) {
            @Override
            protected void populateViewHolder(ArticleViewHolder viewHolder, ArticleSale model, int position) {
                viewHolder.bindArticleInSale(model);
            }
        };

    }
}
