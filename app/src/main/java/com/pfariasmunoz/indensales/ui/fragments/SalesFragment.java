package com.pfariasmunoz.indensales.ui.fragments;

import android.content.Intent;
import android.graphics.Color;
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
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseIndexRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.FirebaseDb;
import com.pfariasmunoz.indensales.data.models.SaleReport;
import com.pfariasmunoz.indensales.ui.AdapterSetter;
import com.pfariasmunoz.indensales.ui.activities.ArticlesInSaleActivity;
import com.pfariasmunoz.indensales.ui.viewholders.SalesReportViewHolder;
import com.pfariasmunoz.indensales.utils.Constants;
import com.pfariasmunoz.indensales.utils.MathHelper;

import timber.log.Timber;

public class SalesFragment extends BaseFragment implements AdapterSetter{

    private Query mSalesQuery;
    private String mUserId;
    private Query mSalesKeys;

   // FirebaseRecyclerAdapter<SaleReport, SalesReportViewHolder> mRecyclerAdapter;
    FirebaseIndexRecyclerAdapter<SaleReport, SalesReportViewHolder> mAdapter;


    public SalesFragment() {
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUserId = FirebaseDb.getUserId();
        mSalesQuery = FirebaseDb.sSalesRef;
        mSalesKeys = FirebaseDb.getDatabase().getReference("ventas-usuario-nombre-cliente").child(mUserId);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        setupAdapter(mSalesKeys);

    }

    @Override
    public void setupAdapter(Query keysRef) {
//        mRecyclerAdapter = new FirebaseRecyclerAdapter<SaleReport, SalesReportViewHolder>(
//                SaleReport.class,
//                R.layout.item_sale,
//                SalesReportViewHolder.class,
//                query) {
//
//
//            @Override
//            protected void populateViewHolder(final SalesReportViewHolder viewHolder, final SaleReport model, final int position) {
//
//                viewHolder.bind(model);
//                showRecyclerView();
//                viewHolder.getArticlesInSalesButton().setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent startArticlesDetails = new Intent(v.getContext(), ArticlesInSaleActivity.class);
//                        startArticlesDetails.putExtra(Constants.SALE_REPORT_KEY, getRef(position).getKey());
//                        startActivity(startArticlesDetails);
//                    }
//                });
//                viewHolder.getAprobSaleStateSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                        String aprob = "aprob";
//                        getRef(position).child(aprob).setValue(isChecked);
//                        FirebaseDb.sSalesRef.child(getRef(position).getKey()).child(.aprob).setValue(isChecked);
//                    }
//                });
//
//            }
//
//
//
//
//        };
//        mRecyclerView.setAdapter(mRecyclerAdapter);
        mAdapter = new FirebaseIndexRecyclerAdapter<SaleReport, SalesReportViewHolder>(
                SaleReport.class,
                R.layout.item_sale,
                SalesReportViewHolder.class,
                keysRef,
                mSalesQuery
        ) {
            @Override
            protected void populateViewHolder(SalesReportViewHolder viewHolder, SaleReport model, final int position) {
                viewHolder.bind(model);
                Timber.i("the report is aout " + model.nombre_cliente);
                showRecyclerView();
                viewHolder.getArticlesInSalesButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent startArticlesDetails = new Intent(v.getContext(), ArticlesInSaleActivity.class);
                        startArticlesDetails.putExtra(Constants.SALE_REPORT_KEY, getRef(position).getKey());
                        startActivity(startArticlesDetails);
                    }
                });
                viewHolder.getAprobSaleStateSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        String aprob = "aprob";
                        getRef(position).child(aprob).setValue(isChecked);
                        //FirebaseDb.sSalesRef.child(getRef(position).getKey()).child(aprob).setValue(isChecked);
                    }
                });
            }
        };
        mRecyclerView.setAdapter(mAdapter);

        setupEmptyListListener(keysRef);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
        cleanupEmptyListListener(mSalesKeys);
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
                            setupAdapter(numberQuery);
                        } else {
                            String text = newText.toUpperCase();
                            Query nameQuery = FirebaseDb.getSaleReportByClientName(text);
                            setupAdapter(nameQuery);
                        }
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.swapAdapter(mAdapter, false);
                    } else {
                        mSalesQuery = FirebaseDb.sSaleReportRef.child(mUserId).limitToLast(ITEMS_LIMIT);
                        setupAdapter(mSalesQuery);
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
