package com.pfariasmunoz.indensales.data;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.pfariasmunoz.indensales.data.models.Sale;

/**
 * Created by Pablo Farias on 22-06-17.
 */

public class Analytics {

    public static void logEventMakeSale(Context context, Sale sale) {
        Bundle saleInfo = new Bundle();
        saleInfo.putString(Analytics.PARAM.USER_ID, sale.idvendedor);
        saleInfo.putString(Analytics.PARAM.CLIENT_ID, sale.idcliente);
        saleInfo.putLong(Analytics.PARAM.TOTAL_SALE, sale.total);


        // Firebase analytics
        FirebaseAnalytics.getInstance(context).logEvent(Analytics.EVENT.MAKE_SALE, saleInfo);

    }


    public static class PARAM {
        public static final String USER_ID = "id_usuario";
        public static final String CLIENT_ID = "id_cliente";
        public static final String TOTAL_SALE = "total_venta";

    }

    public static class EVENT {
        public static final String MAKE_SALE = "venta";

    }
}
