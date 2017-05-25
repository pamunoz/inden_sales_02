package com.pfariasmunoz.indensales.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.FirebaseDb;
import com.pfariasmunoz.indensales.data.models.Address;
import com.pfariasmunoz.indensales.ui.viewholders.AddressViewHolder;
import com.pfariasmunoz.indensales.utils.Constants;
import com.pfariasmunoz.indensales.utils.MathHelper;

public class ClientAddressesActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private String mClientId;
    private Query mAddressQuery;
    private FirebaseRecyclerAdapter<Address, AddressViewHolder> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_addresses);

        // Adding Toolbar to Main screen
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_addresses_content);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator_client_adress);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        mClientId = getIntent().getStringExtra(Constants.CLIENT_ID_KEY);
        mAddressQuery = FirebaseDb.getClientAddresByClientId(mClientId);
        setupAdapter(mAddressQuery);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(false);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration divider = new DividerItemDecoration(this, layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(divider);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setupAdapter(Query query) {
        mAdapter = new FirebaseRecyclerAdapter<Address, AddressViewHolder>(
                Address.class,
                R.layout.item_address,
                AddressViewHolder.class,
                query
        ) {
            @Override
            protected void populateViewHolder(AddressViewHolder viewHolder, Address model, final int position) {
                viewHolder.bind(model);
                viewHolder.getRootView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ClientAddressesActivity.this, CreateSaleActivity.class);
                        intent.putExtra(Constants.CLIENT_ID_KEY, mClientId);
                        intent.putExtra(Constants.ADDRESS_ID_KEY, getRef(position).getKey());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                        startActivity(intent);
                        finish();
                    }
                });
                updateProgresBar();
            }
        };
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAdapter.cleanup();
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
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_search) {
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

                        } else {
                            String text = newText.toUpperCase();
                            Query nameQuery = FirebaseDb.getClientAddresByClientIdAndSearch(mClientId, text);
                            setupAdapter(nameQuery);
                        }
                        mRecyclerView.swapAdapter(mAdapter, false);

                        mAdapter.notifyDataSetChanged();


                    }
                    return false;
                }
            });
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateProgresBar() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }
}
