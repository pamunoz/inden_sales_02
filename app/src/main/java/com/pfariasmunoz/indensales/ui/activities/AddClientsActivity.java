package com.pfariasmunoz.indensales.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.FirebaseDb;
import com.pfariasmunoz.indensales.data.models.Client;
import com.pfariasmunoz.indensales.ui.viewholders.ClientViewHolder;
import com.pfariasmunoz.indensales.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddClientsActivity extends AppCompatActivity {

    public static final String TAG = AddClientsActivity.class.getSimpleName();

    private ValueEventListener mSalesclerkClientsListener;
    private Query mSalesclerkClientsQuery;
    private String mUserId;

    private Query mClientsQuery;
    private FirebaseRecyclerAdapter<Client, ClientViewHolder> mAdapter;

    @BindView(R.id.rv_add_clients)
    RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clients);

        ButterKnife.bind(this);

        mUserId = getIntent().getStringExtra(Constants.USER_ID_KEY);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mClientsQuery = FirebaseDb.sClientsRef.limitToLast(50);
        setupAdapter(mClientsQuery);
    }

    private void setupAdapter(Query query) {
        mAdapter = new FirebaseRecyclerAdapter<Client, ClientViewHolder>(
                Client.class,
                R.layout.item_client,
                ClientViewHolder.class,
                query
        ) {
            @Override
            protected void populateViewHolder(ClientViewHolder viewHolder, Client model, int position) {
                viewHolder.bind(model);
                viewHolder.getAddSaleButton();
            }
        };
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
    }
}
