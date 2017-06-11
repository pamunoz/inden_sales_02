package com.pfariasmunoz.indensales.ui.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ProgressBar;

import com.firebase.ui.database.FirebaseIndexRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.FirebaseDb;
import com.pfariasmunoz.indensales.data.models.Client;
import com.pfariasmunoz.indensales.ui.AdapterSetter;
import com.pfariasmunoz.indensales.ui.activities.MainActivity;
import com.pfariasmunoz.indensales.ui.viewholders.ClientViewHolder;
import com.pfariasmunoz.indensales.utils.MathHelper;

import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClientsFragment extends BaseFragment implements AdapterSetter {

    //private FirebaseRecyclerAdapter<Client, ClientViewHolder> mAdapter;
    private FirebaseIndexRecyclerAdapter<Client, ClientViewHolder> mAdapter;
    private Query mQuery;
    private Query mKeysRef;
    private Query mAddressQuery;
    private ValueEventListener mAddressListener;

    public ClientsFragment() {
        // Required empty public constructor
    }



    // This event is triggered soon after onCreateView().
    // onViewCreated() is only called if the view returned from onCreateView() is non-null.
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(mRecyclerView.getContext(), mLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mQuery = FirebaseDb.sClientsRef;
        mKeysRef = FirebaseDb.sClientsRefKeysByName;
        setupAdapter(mKeysRef);
    }


    @Override
    public void setupAdapter(Query queryKeys) {
        mAdapter = new FirebaseIndexRecyclerAdapter<Client, ClientViewHolder>(
                Client.class,
                R.layout.item_client,
                ClientViewHolder.class,
                queryKeys,
                mQuery
        ) {
            @Override
            protected void populateViewHolder(ClientViewHolder viewHolder, Client model, final int position) {
                viewHolder.bind(model);
                viewHolder.getIsClientAddedCheckBox().setVisibility(View.GONE);

                showRecyclerView();
                viewHolder.getAddSaleButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        mAddressQuery = FirebaseDb.getDatabase().getReference("direcciones-por-cliente").child(getRef(position).getKey());
                        mAddressListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getChildrenCount() < 2) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        String addressKey = snapshot.getKey();
                                        ((MainActivity)getActivity()).startSale(getRef(position).getKey(), addressKey);
                                    }

                                } else {
                                    ((MainActivity)getActivity()).startSale(getRef(position).getKey(), null);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        };
                        mAddressQuery.addListenerForSingleValueEvent(mAddressListener);

                    }
                });
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        setupEmptyListListener(queryKeys);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
        if (mAddressListener != null) {
            mAddressQuery.removeEventListener(mAddressListener);
        }
        cleanupEmptyListListener(mKeysRef);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_search) {
            SearchView searchView = (SearchView) item.getActionView();
            searchView.setQueryHint(getActivity().getResources().getString(R.string.search_clients_hint));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if (!TextUtils.isEmpty(newText)) {
                        if (MathHelper.isNumeric(newText)) {
                            Query rutQueryKeys = FirebaseDb.getUserClientsByRut(newText);
                            setupAdapter(rutQueryKeys);
                        } else {
                            String text = newText.toUpperCase();
                            Query nameQueryKeys = FirebaseDb.getUserClientsByName(text);
                            setupAdapter(nameQueryKeys);
                        }
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.swapAdapter(mAdapter, false);

                    } else {
                        mQuery = FirebaseDb.sClientsRef;
                        setupAdapter(mKeysRef);
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
