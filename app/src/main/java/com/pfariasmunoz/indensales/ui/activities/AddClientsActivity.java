package com.pfariasmunoz.indensales.ui.activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.FirebaseDb;
import com.pfariasmunoz.indensales.data.models.Client;
import com.pfariasmunoz.indensales.ui.adapters.AddClientsAdapter;
import com.pfariasmunoz.indensales.ui.viewholders.ClientViewHolder;
import com.pfariasmunoz.indensales.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddClientsActivity extends AppCompatActivity {

    public static final String TAG = AddClientsActivity.class.getSimpleName();

    private ValueEventListener mUserClientsListener;
    private Query mUserClientsQuery;
    private String mUserId;

    private Query mClientsQuery;
    private FirebaseRecyclerAdapter<Client, ClientViewHolder> mAdapter;

    @BindView(R.id.rv_add_clients)
    RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private List<String> mClientIdList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clients);

        ButterKnife.bind(this);

        mUserId = getIntent().getStringExtra(Constants.USER_ID_KEY);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mUserClientsQuery = FirebaseDb.getClientsByUser(mUserId);
        setupKeysListener();
        mClientsQuery = FirebaseDb.sClientsRef;
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
            protected void populateViewHolder(final ClientViewHolder viewHolder, final Client model, final int position) {
                viewHolder.bind(model);
                if (mClientIdList.contains(getRef(position).getKey())) {
                    viewHolder.itemView.setBackgroundColor(Color.CYAN);
                } else {
                    viewHolder.itemView.setBackgroundColor(Color.WHITE);
                }
                viewHolder.getAddSaleButton();

            }
        };
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setupKeysListener() {
        mUserClientsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        mClientIdList.add(snapshot.getKey());
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mUserClientsQuery.addValueEventListener(mUserClientsListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
        if (mUserClientsListener != null) {
            mUserClientsQuery.removeEventListener(mUserClientsListener);
        }
    }

}
