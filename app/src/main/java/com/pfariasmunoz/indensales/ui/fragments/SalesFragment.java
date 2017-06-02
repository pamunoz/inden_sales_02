package com.pfariasmunoz.indensales.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.FirebaseDb;
import com.pfariasmunoz.indensales.data.models.SaleReport;
import com.pfariasmunoz.indensales.ui.activities.ArticlesInSaleActivity;
import com.pfariasmunoz.indensales.ui.viewholders.SalesReportViewHolder;
import com.pfariasmunoz.indensales.utils.Constants;
import com.pfariasmunoz.indensales.utils.MathHelper;

public class SalesFragment extends Fragment {

    public static final String TAG = SalesFragment.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private Query mSalesQuery;
    private ProgressBar mProgressBar;
    private TextView mEmptyListMessage;
    private String mUserId;

    FirebaseRecyclerAdapter<SaleReport, SalesReportViewHolder> mRecyclerAdapter;
    private ValueEventListener mEmptyStateListener;


    public SalesFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recycler_view, container, false);
        return rootView;
    }

    private void setUpAdapter(Query query) {
        mRecyclerAdapter = new FirebaseRecyclerAdapter<SaleReport, SalesReportViewHolder>(
                SaleReport.class,
                R.layout.item_sale,
                SalesReportViewHolder.class,
                query) {


            @Override
            protected void populateViewHolder(SalesReportViewHolder viewHolder, SaleReport model, final int position) {

                viewHolder.bind(model);
                mProgressBar.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                viewHolder.getArticlesInSalesButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent startArticlesDetails = new Intent(v.getContext(), ArticlesInSaleActivity.class);
                        startArticlesDetails.putExtra(Constants.SALE_REPORT_KEY, getRef(position).getKey());
                        startActivity(startArticlesDetails);
                    }
                });
            }




        };
        mRecyclerView.setAdapter(mRecyclerAdapter);

        mEmptyStateListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                
                if (dataSnapshot.getChildrenCount() == 0) {
                    mEmptyListMessage.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.GONE);
                } else {
                    mEmptyListMessage.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        query.addValueEventListener(mEmptyStateListener);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mEmptyListMessage = (TextView) view.findViewById(R.id.tv_empty_list);

        mUserId = FirebaseDb.getUserId();
        mSalesQuery = FirebaseDb.sSaleReportRef.child(mUserId).limitToLast(30);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_content);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mProgressBar =(ProgressBar) view.findViewById(R.id.pb_loading_indicator);
        mProgressBar.setVisibility(View.VISIBLE);

        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        setUpAdapter(mSalesQuery);



    }

    @Override
    public void onPause() {
        super.onPause();
        mRecyclerAdapter.cleanup();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRecyclerAdapter.cleanup();
        if (mEmptyStateListener != null) {
            mSalesQuery.removeEventListener(mEmptyStateListener);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
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
                            Query numberQuery = FirebaseDb.getSaleReportByClientRut(newText);
                            setUpAdapter(numberQuery);
                        } else {
                            String text = newText.toUpperCase();
                            Query nameQuery = FirebaseDb.getSaleReportByClientName(text);
                            setUpAdapter(nameQuery);
                        }
                        mRecyclerAdapter.notifyDataSetChanged();
                        mRecyclerView.swapAdapter(mRecyclerAdapter, false);
                    } else {
                        mSalesQuery = FirebaseDb.sSaleReportRef.child(mUserId).limitToLast(30);
                        setUpAdapter(mSalesQuery);
                        mRecyclerAdapter.notifyDataSetChanged();
                        mRecyclerView.swapAdapter(mRecyclerAdapter, false);
                    }
                    return false;
                }
            });
        }

        return super.onOptionsItemSelected(item);
    }

}
