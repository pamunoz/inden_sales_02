package com.pfariasmunoz.indensales.ui.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.FirebaseDb;
import com.pfariasmunoz.indensales.data.models.IndenUser;
import com.pfariasmunoz.indensales.ui.AdapterSetter;
import com.pfariasmunoz.indensales.ui.activities.EditUserActivity;
import com.pfariasmunoz.indensales.ui.activities.MainActivity;
import com.pfariasmunoz.indensales.ui.viewholders.UserViewHolder;
import com.pfariasmunoz.indensales.utils.MathHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class UsersFragment extends BaseFragment implements AdapterSetter{

    private Query mQuery;
    @BindView(R.id.rv_content)
    RecyclerView mRecyclerView;
    @BindView(R.id.pb_loading_indicator)
    ProgressBar mProgressBar;
    private LinearLayoutManager mLayoutManager;
    private FirebaseRecyclerAdapter<IndenUser, UserViewHolder> mAdapter;

    public UsersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mQuery = FirebaseDb.sUsers;
        setupAdapter(mQuery);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void setupAdapter(Query query) {
        mAdapter = new FirebaseRecyclerAdapter<IndenUser, UserViewHolder>(
                IndenUser.class,
                R.layout.item_user,
                UserViewHolder.class,
                query
        ) {
            @Override
            protected void populateViewHolder(UserViewHolder viewHolder, IndenUser model, final int position) {
                viewHolder.bind(model);
                mRecyclerView.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.INVISIBLE);
                viewHolder.getAddClientsToUserButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String userUid = getRef(position).getKey();
                        ((MainActivity)getContext()).addClientsToUser(userUid);
                    }
                });

                viewHolder.getEditUserButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), EditUserActivity.class);
                        intent.putExtra("user_id", getRef(position).getKey());
                        startActivity(intent);
                    }
                });
            }
        };
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mAdapter.cleanup();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
                            setupAdapter(mQuery);
                        } else {
                            String name = newText.toUpperCase();
                            Query nameQuery = FirebaseDb.getUserByName(name);
                            setupAdapter(nameQuery);
                        }
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.swapAdapter(mAdapter, false);

                    } else {
                        mQuery = FirebaseDb.sUsers;
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
