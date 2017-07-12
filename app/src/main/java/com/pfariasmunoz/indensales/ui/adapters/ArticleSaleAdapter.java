package com.pfariasmunoz.indensales.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.models.Article;
import com.pfariasmunoz.indensales.data.models.ArticleSale;
import com.pfariasmunoz.indensales.ui.activities.CreateSaleActivity;
import com.pfariasmunoz.indensales.ui.viewholders.ArticleViewHolder;
import com.pfariasmunoz.indensales.utils.MathHelper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class ArticleSaleAdapter extends RecyclerView.Adapter<ArticleViewHolder> {

    Context mContext;
    // New Data
    private List<ArticleSale> mArticleSaleList = new ArrayList<>();
    private List<Article> mArticleList = new ArrayList<>();
    private List<String> mArticlesKeys = new ArrayList<>();
    private Query mQuery;
    private ValueEventListener mEventListener;
    private boolean mIsBeingSearchByWord = false;
    private boolean mIsBeingSearchByCode = false;

    // Values for updating the activity views
    private long mTotalPrice = 0;
    private int mTotalAmount = 0;
    private double mTotalPriceWithDiscount;
    private Map<String, ArticleSale> mArticlesForSale = new HashMap<>();


    public ArticleSaleAdapter(Context context, Query articlesQuery) {
        // Initialize current data
        this.mContext = context;
        this.mQuery = articlesQuery;
        mEventListener = setUpListener();
        mQuery.addValueEventListener(mEventListener);
    }

    public ArticleSaleAdapter(Context context,
                              Query articlesQuery,
                              List<ArticleSale> articleSales,
                              List<Article> articleList,
                              List<String> articlesKeys,
                              boolean isBeingSearchByWord,
                              boolean isBeingSearchByCode) {
        mIsBeingSearchByWord = isBeingSearchByWord;
        mIsBeingSearchByCode = isBeingSearchByCode;
        // Initialize current data
        mArticleSaleList.addAll(articleSales);
        mArticleList.addAll(articleList);
        mArticlesKeys.addAll(articlesKeys);
        this.mContext = context;
        this.mQuery = articlesQuery;
        mEventListener = setUpListener();
        mQuery.addValueEventListener(mEventListener);
    }


    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.item_article_for_sale;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new ArticleViewHolder(view);
    }

    private ValueEventListener setUpListener() {
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Article article = snapshot.getValue(Article.class);
                    String articleKey = snapshot.getKey();
                    addArticleKeyAndEmptyArticleSale(article, articleKey);
                    notifyDataSetChanged();
                }


                setToalPriceAndAmount();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        return listener;
    }

    public void cleanup() {
        if (mEventListener != null) {
            mQuery.removeEventListener(mEventListener);
        }

    }

    @Override
    public void onBindViewHolder(final ArticleViewHolder holder, int position) {

        final ArticleSale articleSale = mArticleSaleList.get(position);
        final Article article = mArticleList.get(position);
        final String articleKey = mArticlesKeys.get(position);
        String code = mArticlesKeys.get(position);
        int yellow = R.color.yellow;
        int white = R.color.white;

        if (mArticleSaleList.get(position).getCantidad() > 0) {
            holder.itemView.setBackgroundResource(yellow);
        } else {
            holder.itemView.setBackgroundResource(white);
        }

        holder.bind(articleSale, article, code);
        holder.getAddArticleButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int amount = articleSale.getCantidad() + 1;
                long total = Long.valueOf(article.getPrecio().trim()) * amount;
                double totalWithDiscount = MathHelper.getTotalPriceWithDeiscount(article, amount);
                ArticleSale articleForSale = new ArticleSale(
                        amount,
                        article.getDescripcion(),
                        articleKey,
                        null,
                        Long.valueOf(article.getPrecio()),
                        total,
                        totalWithDiscount);
                holder.itemView.setBackgroundColor(Color.argb(240, 216, 23, 0));
                mArticleSaleList.set(holder.getAdapterPosition(), articleForSale);
                notifyDataSetChanged();
                setToalPriceAndAmount();
            }
        });
        holder.getSubtractArticleButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (articleSale.getCantidad() <= 1) {
                    ArticleSale articleForSale = new ArticleSale(
                            0, article.getDescripcion(), articleKey,
                            null,
                            Long.valueOf(article.getPrecio()), 0, 0);

                    mArticleSaleList.set(holder.getAdapterPosition(), articleForSale);
                } else {
                    int amount = articleSale.getCantidad() - 1;
                    long total = Long.valueOf(article.getPrecio()) * amount;
                    double totalWithDiscount = MathHelper.getTotalPriceWithDeiscount(article, amount);
                    ArticleSale articleForSale = new ArticleSale(amount,
                            article.getDescripcion(),
                            articleKey,
                            null,
                            Long.valueOf(article.getPrecio()),
                            total,
                            totalWithDiscount);
                    mArticleSaleList.set(
                            holder.getAdapterPosition(),
                            articleForSale);

                }
                notifyDataSetChanged();
                setToalPriceAndAmount();
            }
        });

        if (mContext instanceof CreateSaleActivity) {
            ((CreateSaleActivity) mContext).updateProgressView();
        }
    }

    @Override
    public int getItemCount() {
        return (null != mArticleList ? mArticleList.size() : 0);
    }

    private Context getContext() {
        return mContext;
    }

    public Map<String, ArticleSale> getArticlesForSale() {
        boolean areListEqual = mArticlesKeys.size() == mArticleSaleList.size();
        if (areListEqual) {
            for (int i = 0; i < mArticlesKeys.size(); i++) {
                for (int j = 0; j < mArticleSaleList.size(); j++) {
                    ArticleSale sale = mArticleSaleList.get(i);
                    String key = mArticlesKeys.get(i);
                    if (sale.getCantidad() > 0) {
                        mArticlesForSale.put(key, sale);
                    }
                }
            }
        }
        return (mArticlesForSale.size() > 0 ? mArticlesForSale : null);
    }

    private void setToalPriceAndAmount() {
        mTotalPriceWithDiscount = 0;
        mTotalPrice = 0;
        mTotalAmount = 0;
        if (getArticlesForSale() != null) {
            Iterator it = getArticlesForSale().entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                ArticleSale sale = (ArticleSale) pair.getValue();
                mTotalPrice += sale.getTotal();
                mTotalAmount += sale.getCantidad();
                mTotalPriceWithDiscount += sale.getTotal_con_descuento();
                it.remove(); // avoids a ConcurrentModificationException
            }
        }
        ((CreateSaleActivity) getContext()).setTotals(mTotalPrice, mTotalPriceWithDiscount, mTotalAmount);
    }

    public void setTotalPriceWithDiscount(long totalPriceWithDiscount) {



        mTotalPriceWithDiscount = totalPriceWithDiscount;
    }

    public double getTotalPriceWithDiscount(int discountPercent) {
        double discountPercentDouble = 100.0 - (double) discountPercent;
        double totalWithClientDiscount = mTotalPriceWithDiscount * discountPercentDouble / 100.0;

        return  totalWithClientDiscount;
    }


    public long getTotalPrice() {
        return mTotalPrice;
    }

    public List<ArticleSale> getArticleSaleList() {
        return mArticleSaleList;
    }

    public List<Article> getArticleList() {
        return mArticleList;
    }

    public List<String> getArticlesKeys() {
        return mArticlesKeys;
    }

    private int isReapeated(String key) {
        int counter = 0;
        for (int i = 0; i < mArticlesKeys.size(); i++) {
            if (key.equals(mArticlesKeys.get(i))) counter++;
        }
        return counter;
    }

    public int getTotalAmount() {
        return mTotalAmount;
    }

    private void addArticleKeyAndEmptyArticleSale(Article article, String articleKey) {
        if (mIsBeingSearchByWord) {
            int itemsOnTop = 10;
            List<String> newKeys = new ArrayList<>();
            List<Article> newArticles = new ArrayList<>();
            List<ArticleSale> newArticleSales = new ArrayList<>();
            while (itemsOnTop > 0) {
                if (newKeys.size() > 1) {
                    if (!newKeys.contains(articleKey.trim())) {
                        newArticles.add(article);
                        ArticleSale articleSale = new ArticleSale(0, article.getDescripcion(), articleKey, null, 0L);
                        newArticleSales.add(articleSale);
                        newKeys.add(articleKey);
                    }

                } else {
                    newArticles.add(article);
                    ArticleSale articleSale = new ArticleSale(0, article.getDescripcion(), articleKey, null, 0L);
                    newArticleSales.add(articleSale);
                    newKeys.add(articleKey);
                }
                itemsOnTop--;
            }
            mArticleList.addAll(0, newArticles);
            mArticleSaleList.addAll(0, newArticleSales);
            mArticlesKeys.addAll(0, newKeys);
            mIsBeingSearchByWord = false;
            for (int i = 0; i < mArticlesKeys.size(); i++) {
                if (mArticleSaleList.get(i).getCantidad() == 0 && isReapeated(mArticlesKeys.get(i)) > 1) {
                    mArticlesKeys.remove(i);
                    mArticleSaleList.remove(i);
                    mArticleList.remove(i);
                }
                notifyDataSetChanged();
            }
        } else if (mIsBeingSearchByCode) {
            if (!mArticlesKeys.contains(articleKey.trim())) {
                mArticleList.add(0, article);
                ArticleSale articleSale = new ArticleSale(0, article.getDescripcion(), articleKey, null, 0L);
                mArticleSaleList.add(0, articleSale);
                mArticlesKeys.add(0, articleKey);
            }

            mIsBeingSearchByCode = false;
        } else {
            mArticleList.add(article);
            ArticleSale articleSale = new ArticleSale(0, article.getDescripcion(), articleKey, null, 0L);
            mArticleSaleList.add(articleSale);
            mArticlesKeys.add(articleKey);
        }
    }
}


