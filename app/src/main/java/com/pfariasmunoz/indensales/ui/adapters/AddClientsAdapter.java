package com.pfariasmunoz.indensales.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.models.Client;
import com.pfariasmunoz.indensales.ui.viewholders.ArticleViewHolder;
import com.pfariasmunoz.indensales.ui.viewholders.ClientViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pablo Farias on 06-06-17.
 */

public class AddClientsAdapter extends RecyclerView.Adapter<ClientViewHolder> {
    public static final String TAG = AddClientsAdapter.class.getSimpleName();

    private List<String> mCurrentClientsKeysList = new ArrayList<>();
    private List<String> mNewClientsKeysList = new ArrayList<>();
    private List<Client> mClientList = new ArrayList<>();
    private List<Boolean> mBooleanList = new ArrayList<>();

    private Query mCurrentClientsQuery;
    private Query mNewClientsQuery;

    private ValueEventListener mCurrentClientsListener;
    private ValueEventListener mNewClientsListener;

    public AddClientsAdapter(Query currentClientsQuery, Query newClientsQuery) {
        mCurrentClientsQuery = currentClientsQuery;
        mNewClientsQuery = newClientsQuery;
        setupListeners();
    }

    private void setupListeners() {

        // Add the listener of the clients to add to the user
        mNewClientsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Client client = snapshot.getValue(Client.class);
                        final String newClientKey = snapshot.getKey();
                        mClientList.add(client);


                        // Add the listener for the current clients of the user
                        mCurrentClientsListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        String key = snapshot.getKey();
                                        if (TextUtils.equals(key, newClientKey)) {
                                            mBooleanList.add(true);
                                        } else {
                                            mBooleanList.add(false);
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        };
                        mCurrentClientsQuery.addValueEventListener(mCurrentClientsListener);
                        ///////// end of current keys

                        mNewClientsKeysList.add(snapshot.getKey());
                        notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mNewClientsQuery.addValueEventListener(mNewClientsListener);

    }

    @Override
    public ClientViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.item_client;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new ClientViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ClientViewHolder holder, int position) {
        Client client = mClientList.get(position);
        holder.itemView.setBackgroundColor(Color.alpha(0));
        boolean isCurrent = mBooleanList.get(position);
        if (isCurrent) {
            holder.itemView.setBackgroundColor(Color.CYAN);
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE);
        }
        holder.bind(client);
    }

    @Override
    public int getItemCount() {

        return (mClientList != null ? mClientList.size() : 0);
    }

    public void cleanup() {
        if (mCurrentClientsListener != null) {
            mCurrentClientsQuery.removeEventListener(mCurrentClientsListener);
        }
        if (mNewClientsListener != null) {
            mNewClientsQuery.removeEventListener(mNewClientsListener);
        }
    }
}
