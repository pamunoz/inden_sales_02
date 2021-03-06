package com.pfariasmunoz.indensales.ui.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
import com.github.aakira.expandablelayout.ExpandableWeightLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.Analytics;
import com.pfariasmunoz.indensales.data.FbContract.ClientEntry;
import com.pfariasmunoz.indensales.data.FbContract.ArticleEntry;
import com.pfariasmunoz.indensales.data.FbContract.AddressEntry;
import com.pfariasmunoz.indensales.data.FbContract.SaleEntry;
import com.pfariasmunoz.indensales.data.models.Address;
import com.pfariasmunoz.indensales.data.models.Article;
import com.pfariasmunoz.indensales.data.models.ArticleSale;
import com.pfariasmunoz.indensales.data.models.Client;
import com.pfariasmunoz.indensales.data.models.Sale;
import com.pfariasmunoz.indensales.ui.adapters.ArticleSaleAdapter;
import com.pfariasmunoz.indensales.utils.Constants;
import com.pfariasmunoz.indensales.utils.MathHelper;
import com.pfariasmunoz.indensales.utils.TextHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

import static android.support.design.widget.BottomSheetBehavior.STATE_COLLAPSED;
import static android.support.design.widget.BottomSheetBehavior.STATE_EXPANDED;

public class CreateSaleActivity extends SearchableActivity {

    @BindView(R.id.tv_client_name)
    TextView mClientNameTextView;
    @BindView(R.id.tv_client_rut)
    TextView mClientRutTextView;
    @BindView(R.id.tv_client_address)
    TextView mClientAddressTextView;
    @BindView(R.id.tv_client_phone)
    TextView mClientPhoneTextView;
    @BindView(R.id.tv_sale_total_price)
    TextView mTotalPriceSaleTextView;

    @BindView(R.id.img_toggle_image)
    ImageView mToggleImageView;
    @BindView(R.id.rv_numbers)
    RecyclerView mRecyclerView;
    @BindView(R.id.pb_loading_indicator_sales)
    ProgressBar mProgressBar;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.bottom_sheet)
    LinearLayout mExpandableLayout;

    BottomSheetBehavior mBottomSheetBehavior;

    private String mClientId;
    private String mClientAddressId;
    private String mClientName;
    private String mClientRut;
    private String mClientAddress;
    private int mClientDiscountPercent;

    private ValueEventListener mClientListener;
    private ValueEventListener mClientAddressListener;

    private DatabaseReference mClientQuery;
    private DatabaseReference mClientAddressQuery;
    private Query mArticlesQuery;

    private ArticleSaleAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_sale);
        ButterKnife.bind(this);

        mToggleImageView.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);

        mBottomSheetBehavior = BottomSheetBehavior.from(mExpandableLayout);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case STATE_COLLAPSED:
                        mToggleImageView.setImageResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                        break;

                    case STATE_EXPANDED:
                        mToggleImageView.setImageResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                        break;
                }

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });


        // Remueve el overdraw de esta activity
        getWindow().setBackgroundDrawable(null);

        // Adding Toolbar to Main screen
        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }



        // Initialize Firebase components
        mClientId = getIntent().getStringExtra(Constants.CLIENT_ID_KEY);
        mClientAddressId = getIntent().getStringExtra(Constants.ADDRESS_ID_KEY);
        mClientDiscountPercent = 0;

        // Initialize the queries
        mClientQuery = ClientEntry.sRef.child(mClientId);
        mClientAddressQuery = AddressEntry.sRef.child(mClientAddressId);
        mArticlesQuery = ArticleEntry.sRef.limitToFirst(30);

        mAdapter = new ArticleSaleAdapter(this, mArticlesQuery);

        mProgressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(false);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);
        setTotals(0, 0, 0);
        attachReadListeners();

    }

    private void attachReadListeners() {
        mClientListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getValue() != null) {
                    Client client = dataSnapshot.getValue(Client.class);
                    mClientName = client.nombre;
                    mClientRut = client.rut;
                    mClientDiscountPercent = client.descuento != null ? Integer.valueOf(client.descuento) : 0;
                    String discount = client.descuento;
                    String stringName = TextHelper.capitalizeFirestLetter(mClientName);
                    mClientNameTextView.setText(stringName);
                    mClientRutTextView.setText(mClientRut);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mClientAddressListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot.exists() && dataSnapshot.getValue() != null) {


                    Address address = dataSnapshot.getValue(Address.class);
                    String longAddress = address.direccion + ", " + address.comuna + ", " + address.ciudad;
                    String stringLongAddress = TextHelper.capitalizeFirestLetter(longAddress);
                    mClientAddress = stringLongAddress;
                    mClientAddressTextView.setText(stringLongAddress);
                    mClientPhoneTextView.setText(address.telefono);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mClientQuery.addListenerForSingleValueEvent(mClientListener);
        mClientAddressQuery.addListenerForSingleValueEvent(mClientAddressListener);
    }

    @OnClick(R.id.bt_create_sale)
    public void createSale() {
        Map<String, ArticleSale> articlesForSale = mAdapter.getArticlesForSale();
        //Map<String, ArticleSale> articlesForSaving = mAdapter.getArticlesForSale();
        if (articlesForSale != null) {
            long currentTimeInMillis = System.currentTimeMillis();

            Sale sale = new Sale(
                    false,
                    mClientAddressId,
                    mClientId,
                    getUid(),
                    mClientName,
                    mClientRut,
                    mAdapter.getTotalPrice(),
                    mAdapter.getTotalPriceWithDiscount(mClientDiscountPercent),
                    currentTimeInMillis,
                    mClientAddress);

            DatabaseReference saleRef = SaleEntry.sRef.push();

            saleRef.setValue(sale);

            String saleUid = saleRef.getKey();



            DatabaseReference mySaleNameRef = SaleEntry.sKeysNames.child(getUid()).child(saleUid);
            mySaleNameRef.setValue(sale.getNombre_cliente());

            DatabaseReference mySaleRutRef = SaleEntry.sKeysRuts.child(getUid()).child(saleUid);
            mySaleRutRef.setValue(sale.getRut_cliente());

            Iterator it = articlesForSale.entrySet().iterator();
            while (it.hasNext()) {

                Map.Entry pair = (Map.Entry)it.next();
                ArticleSale articleSale = (ArticleSale) pair.getValue();
                String articleKey = (String) pair.getKey();
                // Save every article sale with the sale report as its parent node

                DatabaseReference articleSaleRef = SaleEntry.sArticlesSalesRef.child(saleUid).push();
                articleSaleRef.setValue(articleSale);
                it.remove(); // avoids a ConcurrentModificationException
            }

            // Save sale data to firebase Analytics
            Analytics.logEventMakeSale(this, sale);
            // Save each article data to firebase Analytics

            Analytics.logEventArticlesSold(this, articlesForSale);

            Intent saleSuccessIntent = new Intent(CreateSaleActivity.this, MainActivity.class);
            setResult(Activity.RESULT_OK, saleSuccessIntent);
            finish();
        } else {
            String message = getResources().getString(R.string.no_article_added_message);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.btn_see_current_sales)
    public void seeCurrentArticlesSales() {
        updateAdapter(mArticlesQuery, false, false);
    }

    @OnClick(R.id.bt_reset_sale)
    public void resetSales() {
        mArticlesQuery = ArticleEntry.sRef.limitToFirst(100);

        mAdapter = new ArticleSaleAdapter(this, mArticlesQuery);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.swapAdapter(mAdapter, false);
    }

    @OnClick(R.id.ll_expand)
    public void toggleBottomSheet() {
        int bottomSheetState = mBottomSheetBehavior.getState();
        switch (bottomSheetState) {
            case STATE_COLLAPSED:
                mBottomSheetBehavior.setState(STATE_EXPANDED);
                break;
            case STATE_EXPANDED:
                mBottomSheetBehavior.setState(STATE_COLLAPSED);
                break;
        }
    }

    /**
     * This method update the adapter with a new query with the condition if the action
     * is searching, the other is putting the articles added to the top of the list.
     * @param query the new query provided
     * @param isBeingSearch the check if the user is searching
     */
    private void updateAdapter(Query query, boolean isBeingSearch, boolean isBeingSearchByCode) {
        List<String> currentKeys = new ArrayList<>();
        List<ArticleSale> currentArticlesSales = new ArrayList<>();
        List<Article> currentArticles = new ArrayList<>();
        for (int i = 0; i < mAdapter.getArticlesKeys().size(); i++) {
            if (mAdapter.getArticleSaleList().get(i).getCantidad() > 0) {
                currentKeys.add(mAdapter.getArticlesKeys().get(i));
                currentArticlesSales.add(mAdapter.getArticleSaleList().get(i));
                currentArticles.add(mAdapter.getArticleList().get(i));
            }
        }

        mAdapter = new ArticleSaleAdapter(this, query, currentArticlesSales, currentArticles, currentKeys, isBeingSearch, isBeingSearchByCode);
        mRecyclerView.swapAdapter(mAdapter, false);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mAdapter.cleanup();
        detachReadListeners();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
        detachReadListeners();
    }

    public void updateProgressView() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.action_search:
                SearchView searchView = (SearchView) item.getActionView();
                searchView.setQueryHint(getResources().getString(R.string.search_articles_hint));
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {

                        if (!TextUtils.isEmpty(newText)) {
                            if (MathHelper.isNumeric(newText)) {
                                Query query = ArticleEntry.getArticlesCodeQuery(newText).limitToFirst(30);
                                updateAdapter(query, false, true);
                            } else {
                                String text = newText.toUpperCase();
                                Query query = ArticleEntry.getArticlesDescriptionQuery(text);
                                updateAdapter(query, true, false);
                            }
                            mAdapter.notifyDataSetChanged();
                        }
                        if (TextUtils.isEmpty(newText)) {
                            updateAdapter(mArticlesQuery, false, false);
                        }
                        return false;
                    }
                });
                return true;
            case android.R.id.home:
                warningSaleCancellation();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        warningSaleCancellation();
    }

    private void warningSaleCancellation() {
        String title = getResources().getString(R.string.title_sale_cancellation_dialog);
        String messagePart1 = getResources().getString(R.string.message_sale_cancellation_dialgo_1);
        String messagePart2 = getResources().getString(R.string.message_sale_cancellation_dialgo_2);
        String yes = getResources().getString(R.string.yes);
        String no = getResources().getString(R.string.no);

        if (mAdapter.getTotalAmount() > 0) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(title)
                    .setMessage(messagePart1 + String.valueOf(mAdapter.getTotalAmount()) + messagePart2)
                    .setPositiveButton(yes, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(CreateSaleActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }

                    })
                    .setNegativeButton(no, null)
                    .show();
        } else {
            Intent intent = new Intent(CreateSaleActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

    }

    private void detachReadListeners() {
        if (mClientListener != null) {
            mClientQuery.removeEventListener(mClientListener);
        }
        if (mClientAddressListener != null) {
            mClientAddressQuery.removeEventListener(mClientAddressListener);
        }
    }

    public void setTotals(long totalPrice, double totalPriceWithDiscount, int totalAmount) {
        long totalWithDiscount = (long) totalPriceWithDiscount;
        String stringTotalWithDiscount = MathHelper.getLocalCurrency(String.valueOf(totalWithDiscount));
        String stringTotal = MathHelper.getLocalCurrency(String.valueOf(totalPrice));
        mTotalPriceSaleTextView.setText(stringTotalWithDiscount);
    }

}
