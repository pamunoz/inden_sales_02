package com.pfariasmunoz.indensales.data;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.pfariasmunoz.indensales.data.models.ArticleSale;
import com.pfariasmunoz.indensales.data.models.Sale;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by Pablo Farias on 22-06-17.
 */

public class Analytics {

    public static void logEventMakeSale(Context context, Sale sale) {
        Bundle params = new Bundle();
        params.putString(Param.USER_ID, sale.idvendedor);
        params.putString(Param.CLIENT_ID, sale.idcliente);
        params.putLong(Param.TOTAL_SALE, sale.total);

        // Firebase analytics
        FirebaseAnalytics.getInstance(context).logEvent(Event.MAKE_SALE, params);
    }

    public static void logEventArticlesSold(Context context, Map<String, ArticleSale> articlesForSale) {
        Iterator it = articlesForSale.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            ArticleSale articleSale = (ArticleSale) pair.getValue();
            String articleKey = (String) pair.getKey();
            Bundle params = new Bundle();
            params.putString(Param.ARTICLE_DESCRIPTION, articleSale.getDescripcion());
            params.putString(Param.ARTICLE_ID, articleSale.getIdarticulo());
            params.putInt(Param.ARTICLE_QUANTITY, articleSale.getCantidad());
            params.putLong(Param.ARTICLE_PRICE, articleSale.getTotal());

            // Save every article sale with the sale report as its parent node
            FirebaseAnalytics.getInstance(context).logEvent(Event.ARTICLE_SOLD, params);
            it.remove(); // avoids a ConcurrentModificationException
        }


    }

    private static class Param {
        public static final String USER_ID = "id_usuario";
        public static final String CLIENT_ID = "id_cliente";
        public static final String TOTAL_SALE = "total_venta";
        public static final String ARTICLE_ID = "articulo_id";
        public static final String ARTICLE_QUANTITY = "articulo_cantidad";
        public static final String ARTICLE_PRICE = "articulo_total";
        public static final String ARTICLE_DESCRIPTION = "articulo_descripción";


    }

    private static class Event {
        public static final String MAKE_SALE = "venta";
        public static final String ARTICLE_SOLD = "articulo_vendido";


    }
}
