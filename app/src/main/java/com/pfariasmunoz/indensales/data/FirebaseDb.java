package com.pfariasmunoz.indensales.data;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.pfariasmunoz.indensales.data.models.IndenUser;
import com.pfariasmunoz.indensales.utils.Constants;

public class FirebaseDb {
    /**
     * Metodo que entrega una instancia de la base de datos.
     */
    private static FirebaseDatabase mDatabase;
    public static FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase;
    }
    public static String getUserId() {
        String userId = "unknown";
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userId = user.getUid();
        }
        return userId;
    }

    public static IndenUser getIndenUser(FirebaseUser user) {
        String name = user.getDisplayName().trim().toUpperCase();
        String rut = DbContract.EMPTY_STRING_VALUE;
        String email = user.getEmail().trim().toLowerCase();
        String phone = DbContract.EMPTY_STRING_VALUE;
        String photoUrl = user.getPhotoUrl().toString();
        int role = DbContract.USER_ROLE_GUEST;
        return new IndenUser(name, rut, email, phone, photoUrl, role);
    }

    public static final DatabaseReference sDbRef = getDatabase().getReference();

    // Articles db reference
    public static final DatabaseReference sArticlesRef = sDbRef.child(DbContract.ARTICLES_ND);
    public static final DatabaseReference sClientsRef = sDbRef.child(DbContract.CLIENTS_ND);
    public static final DatabaseReference sClientsRefKeysByName = sDbRef.child(DbContract.CLIENTS_NAMES_REF_KEYS).child(getUserId());
    public static final DatabaseReference sClientsRefKeysByRut = sDbRef.child(DbContract.CLIENTS_RUTS_REF_KEYS).child(getUserId());
    public static final DatabaseReference sClientsRefKeysName = sDbRef.child(DbContract.CLIENTS_NAMES_REF_KEYS);
    public static final DatabaseReference sClientsRefKeysRut = sDbRef.child(DbContract.CLIENTS_RUTS_REF_KEYS);
    public static final DatabaseReference sClientAdressRef = sDbRef.child(DbContract.CLIENT_ADDRESS_ND_KEYS);
    public static final DatabaseReference sAddressesRef = sDbRef.child(DbContract.ADDRESSES);
    public static final DatabaseReference sUsers = sDbRef.child(DbContract.USERS_ND);
    public static final DatabaseReference sSaleReportRef = sDbRef.child(DbContract.SALE_REPORTS);
    public static final DatabaseReference sArticlesSalesRef = sDbRef.child(DbContract.ARTICLES_SALE_ND);
    public static final DatabaseReference sSalesRef = sDbRef.child(DbContract.SALES_ND);
    public static final DatabaseReference sSalesKeysNames = sDbRef.child(DbContract.SALES_KEYS_NAMES);
    public static final DatabaseReference sSalesKeysRuts = sDbRef.child(DbContract.SALES_KEYS_RUTS);

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
    public static Query getArticlesSalesBySaleUid(String saleUid, String description, String code ) {
        String endtext;
        if (description != null) {
            endtext = description + "\uf8ff";
            return FirebaseDb.sArticlesSalesRef.child(saleUid).orderByChild(DbContract.ARTICLES_DESCRIPTION_KEY).startAt(description).endAt(endtext);
        } else if (code != null) {
            endtext = code + "\uf8ff";
            return FirebaseDb.sArticlesSalesRef.child(saleUid).orderByChild(DbContract.ARTICLES_UID_KEY).startAt(code).endAt(endtext);
        } else {
            return FirebaseDb.sArticlesSalesRef.child(saleUid);
        }

    }
    public static Query getClientAddresByClientId(String clientId) {
        return FirebaseDb.sClientAdressRef.child(clientId);
    }
    public static Query getUserClientsByName(String name) {
        String endtext = name + "\uf8ff";
        return sClientsRefKeysByName.orderByValue().startAt(name).endAt(endtext);
    }
    public static Query getUserClientsByRut(String rut) {
        String endtext = rut + "\uf8ff";
        return sClientsRefKeysByRut.orderByValue().startAt(rut).endAt(endtext);
    }
    public static Query getClientAddresByClientIdAndSearch(String clientId, String newAddress) {
        String endText = newAddress + "\uf8ff";
        return FirebaseDb.sClientAdressRef.child(clientId).orderByValue().startAt(newAddress).endAt(endText);
    }
    public static Query getClientsByUser(String userId) {
        return getDatabase().getReference(DbContract.CLIENTS_NAMES_REF_KEYS).child(userId);
    }
    public static Query getUserByName(String name) {
        String endText = name + "\uf8ff";
        return sUsers.orderByChild(DbContract.USER_NAME_KEY).startAt(name).endAt(endText);
    }
    public static DatabaseReference getUserRole(String userId) {
        return FirebaseDb.sUsers.child(userId).child(DbContract.USER_ROLE_KEY);
    }
    public static DatabaseReference getUserName(String userId) {
        return FirebaseDb.sUsers.child(userId).child(DbContract.USER_NAME_KEY);
    }
    public static DatabaseReference getUserEmail(String userId) {
        return FirebaseDb.sUsers.child(userId).child(DbContract.USER_EMAIL_KEY);
    }
    public static DatabaseReference getUserPhotoUrl(String userId) {
        return FirebaseDb.sUsers.child(userId).child(DbContract.USER_PHOTOURL_KEY);
    }
    public static DatabaseReference getUserPhone(String userId) {
        return FirebaseDb.sUsers.child(userId).child(DbContract.USER_PHONE_KEY);
    }
    public static DatabaseReference getUserRut(String userId) {
        return FirebaseDb.sUsers.child(userId).child(DbContract.USER_RUT_KEY);
    }

    public static Query getSalesKeysByClientName(String name) {
        String endText = name + "\uf8ff";
        return sSalesKeysNames.child(getUserId()).orderByValue().startAt(name).endAt(endText);
    }

    public static Query getSalesKeysByClientRut(String rut) {
        String endText = rut + "\uf8ff";
        return sSalesKeysRuts.child(getUserId()).orderByValue().startAt(rut).endAt(endText);
    }
}
