package com.pfariasmunoz.indensales.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseIndexRecyclerAdapter;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.FbContract;
import com.pfariasmunoz.indensales.data.FbContract.SaleEntry;
import com.pfariasmunoz.indensales.data.models.Sale;
import com.pfariasmunoz.indensales.ui.AdapterSetter;
import com.pfariasmunoz.indensales.ui.activities.ArticlesInSaleActivity;
import com.pfariasmunoz.indensales.ui.viewholders.SaleViewHolder;
import com.pfariasmunoz.indensales.utils.Constants;
import com.pfariasmunoz.indensales.utils.MathHelper;

import butterknife.BindView;
import timber.log.Timber;

public class SalesFragment extends BaseFragment implements AdapterSetter{

    private Query mSalesQuery;
    private Query mSalesKeys;

    @BindView(R.id.empty_sales_view)
    View mEmptySalesView;

   // FirebaseRecyclerAdapter<Sale, SaleViewHolder> mRecyclerAdapter;
    FirebaseIndexRecyclerAdapter<Sale, SaleViewHolder> mAdapter;


    public SalesFragment() {
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSalesQuery = SaleEntry.sRef;
        mSalesKeys = SaleEntry.sKeysNames.child(getUid());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        setupAdapter(mSalesKeys);

    }

    @Override
    public void setupAdapter(Query keysRef) {
        mAdapter = new FirebaseIndexRecyclerAdapter<Sale, SaleViewHolder>(
                Sale.class,
                R.layout.item_sale,
                SaleViewHolder.class,
                keysRef,
                mSalesQuery
        ) {
            @Override
            protected void populateViewHolder(SaleViewHolder viewHolder, Sale model, final int position) {
                viewHolder.bind(model);
                updateViews();
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

                        DatabaseReference ref = getRef(position);
                        ref.child(SaleEntry.SALE_APROB).setValue(isChecked, FbContract.getPermissionDeniedListener(getActivity()));
                    }
                });
            }

            @Override
            protected void onDataChanged() {
                updateViews();
            }

            @Override
            protected void onCancelled(DatabaseError error) {
                super.onCancelled(error);
                if (error.getCode() == DatabaseError.PERMISSION_DENIED) {
                    showNoAccessView();
                    Timber.i("ERROR CODE: " + error.toString());
                } else if (error.getCode() == DatabaseError.UNAVAILABLE) {
                    updateViews();
                }

            }
        };
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
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
                        showProgressBar();
                        if (MathHelper.isNumeric(newText)) {
                            Query numberQuery = SaleEntry.getSalesKeysByClientRut(newText);
                            setupAdapter(numberQuery);
                        } else {
                            String text = newText.toUpperCase();
                            Query nameQuery = SaleEntry.getSalesKeysByClientName(text);
                            setupAdapter(nameQuery);
                        }
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.swapAdapter(mAdapter, false);
                    } else {
                        mSalesKeys = SaleEntry.sRef.limitToLast(ITEMS_LIMIT);
                        setupAdapter(mSalesKeys);
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.swapAdapter(mAdapter, false);
                    }
                    return false;
                }
            });
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateViews() {
        super.updateViews();
        mEmptySalesView.setVisibility(mAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }
}
