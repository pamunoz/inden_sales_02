package com.pfariasmunoz.indensales.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.FirebaseDb;
import com.pfariasmunoz.indensales.data.models.Article;
import com.pfariasmunoz.indensales.data.models.ArticleSale;
import com.pfariasmunoz.indensales.ui.viewholders.ArticleReportViewHolder;
import com.pfariasmunoz.indensales.ui.viewholders.ArticleViewHolder;
import com.pfariasmunoz.indensales.utils.Constants;

public class ArticlesInSaleActivity extends AppCompatActivity {

    public static final String TAG = ArticlesInSaleActivity.class.getSimpleName();

    private Query mArticlesInSaleQuery;
    private ValueEventListener mArticlesInSalesListener;
    private FirebaseRecyclerAdapter<ArticleSale, ArticleReportViewHolder> mAdapter;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private String mSaleId;

    private Query mSaleReportQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles_in_sale);


        String activityTitle = getResources().getString(R.string.client_address_activity_title);
        setTitle(activityTitle);

//        // Adding Toolbar to Main screen
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        }

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_articles_sales_content);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator_client_adress);
        mRecyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        mSaleId = getIntent().getStringExtra(Constants.SALE_REPORT_KEY);
        mSaleReportQuery = FirebaseDb.getArticlesSalesBySaleUid(mSaleId);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(false);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(layoutManager);
        setupAdapter(mSaleReportQuery);
    }

    private void setupAdapter(Query query) {
        FirebaseRecyclerAdapter<ArticleSale, ArticleViewHolder> adapter = new FirebaseRecyclerAdapter<ArticleSale, ArticleViewHolder>(
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
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
    }
}
