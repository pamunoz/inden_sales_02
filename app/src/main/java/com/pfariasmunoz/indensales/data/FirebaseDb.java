package com.pfariasmunoz.indensales.data;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class FirebaseDb {

    public static FirebaseDatabase mDatabase;
    public static FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase;
    }
    public static final DatabaseReference sArticlesRef = getDatabase().getReference(DbContract.ARTICLES_ND);
    public static final DatabaseReference sClientAdressRef = getDatabase().getReference(DbContract.CLIENT_ADDRESS_ND);

    public static final DatabaseReference sClientsRef = getDatabase().getReference(DbContract.CLIENTS_ND);
    public static final DatabaseReference sSaleReportRef = getDatabase().getReference(DbContract.SALE_REPORTS);
    public static final DatabaseReference sArticlesSalesRef = getDatabase().getReference(DbContract.ARTICLES_SALE_ND);
    public static final DatabaseReference sSalesRef = getDatabase().getReference(DbContract.SALES_ND);

    public static String getUserId() {
        String userId = "unknown";
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userId = user.getUid();
        }
        return userId;
    }


    public static Query getClientsNameQuery(String newName) {
        String endtext = newName + "\uf8ff";
        return sClientsRef.orderByChild(DbContract.CLIENT_NAME_KEY).startAt(newName).endAt(endtext);
    }

    public static Query getClientsRutQuery(String newRut) {
        String endtext = newRut + "\uf8ff";
        return sClientsRef.orderByChild(DbContract.CLIENT_RUT_KEY).startAt(newRut).endAt(endtext);
    }

    public static Query getArticlesCodeQuery(String newCode) {
        String endtext = newCode + "\uf8ff";
        return sArticlesRef.orderByKey().startAt(newCode).endAt(endtext);
    }

    public static Query getArticlesDescriptionQuery(String newDescription) {
        String endtext = newDescription + "\uf8ff";
        return sArticlesRef.orderByChild(DbContract.ARTICLES_DESCRIPTION_KEY).startAt(newDescription).endAt(endtext);
    }

    public static Query getSaleReportByClientName(String clientName) {
        String endtext = clientName + "\uf8ff";
        return sSaleReportRef.child(getUserId()).orderByChild(DbContract.SALE_REPORT_CLIENT_NAME_FD).startAt(clientName).endAt(endtext);
    }

    public static Query getSaleReportByClientRut(String clientRut) {
        String endtext = clientRut + "\uf8ff";
        return sSaleReportRef.child(getUserId()).orderByChild(DbContract.SALE_REPORT_CLIENT_RUT_FD).startAt(clientRut).endAt(endtext);
        //return sSaleReportRef.orderByChild(DbContract.SALE_REPORT_CLIENT_NAME_FD).startAt(clientRut).endAt(endtext);
    }

    public static Query getClientAddresByClientId(String clientId) {
        return FirebaseDb.sClientAdressRef.child(clientId);
    }

    public static Query getArticlesSalesBySaleUid(String saleUid) {
        return FirebaseDb.sArticlesSalesRef.child(saleUid);
    }

    public static Query getClientAddresByClientIdAndSearch(String clientId, String newAddress) {
        String endText = newAddress + "\uf8ff";
        return FirebaseDb.sClientAdressRef.child(clientId).orderByChild(DbContract.CLIENT_ADDRESS_AD_FD).startAt(newAddress).endAt(endText);
    }

}
