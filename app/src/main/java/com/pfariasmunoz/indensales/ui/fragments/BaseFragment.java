package com.pfariasmunoz.indensales.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.FbContract;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by Pablo Farias on 07-06-17.
 */

public class BaseFragment extends Fragment {

    @BindView(R.id.rv_content)
    RecyclerView mRecyclerView;
    @BindView(R.id.pb_loading_indicator)
    ProgressBar mProgressBar;
    @BindView(R.id.tv_empty_list)
    TextView mEmptyListTextView;

    public static final int ITEMS_LIMIT = 50;

    LinearLayoutManager mLayoutManager;
    ValueEventListener mEmptyListListener;

    public String getUid() {
        return FbContract.getUserId();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view, container, false);
        ButterKnife.bind(this, view);
        showProgressBar();
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void showRecyclerView() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        mEmptyListTextView.setVisibility(View.INVISIBLE);
    }

    private void showProgressBar() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        mEmptyListTextView.setVisibility(View.INVISIBLE);
    }

    public void showEmptyLisText() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        mEmptyListTextView.setVisibility(View.VISIBLE);
    }

    public void setupEmptyListListener(Query query) {
        mEmptyListListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (!dataSnapshot.exists() || dataSnapshot.getChildrenCount() == 0) {
                    showEmptyLisText();
                } else {
                    showRecyclerView();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        query.addValueEventListener(mEmptyListListener);
    }

    public void cleanupEmptyListListener(Query query) {
        if (mEmptyListListener != null) {
            query.removeEventListener(mEmptyListListener);
        }
    }
}
