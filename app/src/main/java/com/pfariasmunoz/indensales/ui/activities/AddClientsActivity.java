package com.pfariasmunoz.indensales.ui.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.FirebaseDb;
import com.pfariasmunoz.indensales.data.models.Client;
import com.pfariasmunoz.indensales.ui.AdapterSetter;
import com.pfariasmunoz.indensales.ui.viewholders.ClientViewHolder;
import com.pfariasmunoz.indensales.utils.Constants;
import com.pfariasmunoz.indensales.utils.MathHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddClientsActivity extends SearchableActivity implements AdapterSetter {

    private ValueEventListener mUserClientsListener;
    private Query mUserClientsQuery;
    private String mUserId;

    private Query mQuery;
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
        mQuery = FirebaseDb.sClientsRef.limitToLast(50);
        setupAdapter(mQuery);

    }

    @Override
    public void setupAdapter(Query query) {
        mAdapter = new FirebaseRecyclerAdapter<Client, ClientViewHolder>(
                Client.class,
                R.layout.item_client,
                ClientViewHolder.class,
                query
        ) {
            @Override
            protected void populateViewHolder(final ClientViewHolder viewHolder, final Client model, final int position) {
                viewHolder.bind(model);
                viewHolder.getAddSaleButton().setVisibility(View.INVISIBLE);
                final CheckBox checkBox = viewHolder.getIsClientAddedCheckBox();
                final DatabaseReference refName = FirebaseDb.sClientsRefKeysName.child(mUserId).child(getRef(position).getKey());
                final DatabaseReference refRut = FirebaseDb.sClientsRefKeysRut.child(mUserId).child(getRef(position).getKey());
                checkBox.setChecked(mClientIdList.contains(getRef(position).getKey()));

                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (checkBox.isChecked()) {
                            refName.setValue(model.nombre);
                            refRut.setValue(model.rut);
                            mClientIdList.add(getRef(position).getKey());
                            checkBox.setChecked(true);
                        } else {
                            for (int i = 0; i < mClientIdList.size(); i++) {
                                if (mClientIdList.get(i).equals(getRef(position).getKey())) {
                                    mClientIdList.remove(i);
                                }
                            }
                            refName.removeValue();
                            refRut.removeValue();
                            checkBox.setChecked(false);
                        }
                    }
                });

            }
        };
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setupKeysListener() {
        mUserClientsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mClientIdList.clear();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            SearchView searchView = (SearchView) item.getActionView();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if (!TextUtils.isEmpty(newText)) {
                        if (MathHelper.isNumeric(newText)) {
                            Query rutQueryKeys = FirebaseDb.getClientsRutQuery(newText);
                            setupAdapter(rutQueryKeys);
                        } else {
                            String text = newText.toUpperCase();
                            Query nameQueryKeys = FirebaseDb.getClientsNameQuery(text);
                            setupAdapter(nameQueryKeys);
                        }
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.swapAdapter(mAdapter, false);

                    } else {
                        setupAdapter(mQuery);
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.swapAdapter(mAdapter, false);
                    }
                    return false;
                }
            });
        }

        return super.onOptionsItemSelected(item);
    }
}
