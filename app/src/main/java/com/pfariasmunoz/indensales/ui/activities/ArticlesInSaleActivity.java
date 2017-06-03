package com.pfariasmunoz.indensales.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.FirebaseDb;
import com.pfariasmunoz.indensales.data.models.ArticleSale;
import com.pfariasmunoz.indensales.ui.viewholders.ArticleViewHolder;
import com.pfariasmunoz.indensales.utils.Constants;
import com.pfariasmunoz.indensales.utils.MathHelper;

public class ArticlesInSaleActivity extends AppCompatActivity {

    private FirebaseRecyclerAdapter<ArticleSale, ArticleViewHolder> mAdapter;
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

        // Adding Toolbar to Main screen
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_articles_sales_content);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator_client_adress);
        mRecyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        mSaleId = getIntent().getStringExtra(Constants.SALE_REPORT_KEY);
        mSaleReportQuery = FirebaseDb.getArticlesSalesBySaleUid(mSaleId, null, null);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(false);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(layoutManager);
        setupAdapter(mSaleReportQuery);
    }

    private void setupAdapter(Query query) {
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
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            SearchView searchView = (SearchView) item.getActionView();
            searchView.setQueryHint(getResources().getString(R.string.search_clients_hint));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if (!TextUtils.isEmpty(newText)) {
                        if (MathHelper.isNumeric(newText)) {
                            String text = newText.toUpperCase();
                            Query codeQuery = FirebaseDb.getArticlesSalesBySaleUid(mSaleId, null, text);
                            setupAdapter(codeQuery);

                        } else {
                            String text = newText.toUpperCase();
                            Query descriptionQuery = FirebaseDb.getArticlesSalesBySaleUid(mSaleId, text, null);
                            setupAdapter(descriptionQuery);
                        }
                        mRecyclerView.swapAdapter(mAdapter, false);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        mSaleReportQuery = FirebaseDb.getArticlesSalesBySaleUid(mSaleId, null, null);
                        setupAdapter(mSaleReportQuery);
                        mRecyclerView.swapAdapter(mAdapter, false);
                        mAdapter.notifyDataSetChanged();
                    }
                    return false;
                }
            });
        }

        return super.onOptionsItemSelected(item);
    }
}
