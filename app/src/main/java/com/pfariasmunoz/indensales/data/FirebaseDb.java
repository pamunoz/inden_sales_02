package com.pfariasmunoz.indensales.data;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.pfariasmunoz.indensales.data.models.IndenUser;

public final class FirebaseDb {
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
        String email = user.getEmail().trim().toLowerCase();
        String photoUrl = user.getPhotoUrl().toString();
        int role = UserEntry.USER_ROLE_GUEST;
        return new IndenUser(name, null, email, null, photoUrl, role);
    }

    public static final DatabaseReference sDbRef = getDatabase().getReference();

    /**
     * Articles node
     */
//    public static final DatabaseReference sRef = sDbRef.child(DbContract.ARTICLES_ND);
//
//    public static Query getArticlesCodeQuery(String newCode) {
//        String endtext = newCode + "\uf8ff";
//        return sRef.orderByKey().startAt(newCode).endAt(endtext);
//    }
//    public static Query getArticlesDescriptionQuery(String newDescription) {
//        String endtext = newDescription + "\uf8ff";
//        return sRef.orderByChild(DbContract.ARTICLES_DESCRIPTION_KEY).startAt(newDescription).endAt(endtext);
//    }

    public static class ArticleEntry {
        public static final String ARTICLES_ND = "articulos";
        public static final String ARTICLE_DESCRIPTION = "descripcion";
        public static final DatabaseReference sRef = sDbRef.child(ARTICLES_ND);

        public static Query getArticlesCodeQuery(String newCode) {
            String endtext = newCode + "\uf8ff";
            return sRef.orderByKey().startAt(newCode).endAt(endtext);
        }
        public static Query getArticlesDescriptionQuery(String newDescription) {
            String endtext = newDescription + "\uf8ff";
            return sRef.orderByChild(ARTICLE_DESCRIPTION).startAt(newDescription).endAt(endtext);
        }
    }

    /**
     * Clients node
     */
//    public static final DatabaseReference sRef = sDbRef.child(DbContract.CLIENTS_ND);
//    public static final DatabaseReference sRefKeysName = sDbRef.child(DbContract.CLIENTS_NAMES_REF_KEYS);
//    public static final DatabaseReference sRefKeysRut = sDbRef.child(DbContract.CLIENTS_RUTS_REF_KEYS);
//    public static final DatabaseReference sMyClientsRefKeysByName = sRefKeysName.child(getUserId());
//    public static final DatabaseReference sMyClientsRefKeysByRut = sRefKeysRut.child(getUserId());


    public static class ClientEntry {

        private ClientEntry() {
        }

        public static final String CLIENTS_ND = "clientes";
        public static final String CLIENT_DISCOUNT = "descuento";
        public static final String CLIENT_NAME = "nombre";
        public static final String CLIENT_RUT = "rut";
        // Referencia de key pra busqueda por nombre del cliente
        public static final String CLIENTS_NAMES_REF_KEYS = "clientes-nombre-por-usuario";
        // Referencia de key pra busqueda por nombre del cliente
        public static final String CLIENTS_RUTS_REF_KEYS = "clientes-rut-por-usuario";

        public static final DatabaseReference sRef = sDbRef.child(CLIENTS_ND);
        public static final DatabaseReference sRefKeysName = sDbRef.child(CLIENTS_NAMES_REF_KEYS);
        public static final DatabaseReference sRefKeysRut = sDbRef.child(CLIENTS_RUTS_REF_KEYS);
        public static final DatabaseReference sMyClientsRefKeysByName = sRefKeysName.child(getUserId());
        public static final DatabaseReference sMyClientsRefKeysByRut = sRefKeysRut.child(getUserId());

        public static Query getClientsNameQuery(String newName) {
            String endtext = newName + "\uf8ff";
            return sRef.orderByChild(CLIENT_NAME).startAt(newName).endAt(endtext);
        }
        public static Query getClientsRutQuery(String newRut) {
            String endtext = newRut + "\uf8ff";
            return sRef.orderByChild(CLIENT_RUT).startAt(newRut).endAt(endtext);
        }
        public static Query getMyClientsByName(String name) {
            String endtext = name + "\uf8ff";
            return sMyClientsRefKeysByName.orderByValue().startAt(name).endAt(endtext);
        }
        public static Query getMyClientsByRut(String rut) {
            String endtext = rut + "\uf8ff";
            return sMyClientsRefKeysByRut.orderByValue().startAt(rut).endAt(endtext);
        }
        public static Query getClientsByUser(String userId) {
            return sRefKeysName.child(userId);
        }

        public static DatabaseReference getUserClientsNamesKeysByUserId(String userId) {
            return sRefKeysName.child(userId);
        }

        public static DatabaseReference getUserClientsRutsKeysByUserId(String userId) {
            return sRefKeysRut.child(userId);
        }

        public static Query filterUserClientsByName(String userId, String clientName) {
            String endtext = clientName + "\uf8ff";
            return sRefKeysName.child(userId).orderByValue().startAt(clientName).endAt(endtext);
        }

        public static Query filterUserClientsByRut(String userId, String clientRut) {
            String endtext = clientRut + "\uf8ff";
            return sRefKeysRut.child(userId).orderByValue().startAt(clientRut).endAt(endtext);
        }

    }
//
//    public static Query getClientsNameQuery(String newName) {
//        String endtext = newName + "\uf8ff";
//        return sRef.orderByChild(DbContract.CLIENT_NAME_KEY).startAt(newName).endAt(endtext);
//    }
//    public static Query getClientsRutQuery(String newRut) {
//        String endtext = newRut + "\uf8ff";
//        return sRef.orderByChild(DbContract.CLIENT_RUT_KEY).startAt(newRut).endAt(endtext);
//    }
//
//    public static Query getMyClientsByName(String name) {
//        String endtext = name + "\uf8ff";
//        return sMyClientsRefKeysByName.orderByValue().startAt(name).endAt(endtext);
//    }
//    public static Query getMyClientsByRut(String rut) {
//        String endtext = rut + "\uf8ff";
//        return sMyClientsRefKeysByRut.orderByValue().startAt(rut).endAt(endtext);
//    }
//
//    public static Query getClientsByUser(String userId) {
//        return getDatabase().getReference(DbContract.CLIENTS_NAMES_REF_KEYS).child(userId);
//    }

    /**
     * Addresses db references
     */
//    public static final DatabaseReference sClientAdressRef = sDbRef.child(DbContract.CLIENT_ADDRESS_ND_KEYS);
//    public static final DatabaseReference sAddressesRef = sDbRef.child(DbContract.ADDRESSES);
//
//    public static Query getClientAddresByClientId(String clientId) {
//        return FirebaseDb.sClientAdressRef.child(clientId);
//    }
//
//    public static Query getClientAddresByClientIdAndSearch(String clientId, String newAddress) {
//        String endText = newAddress + "\uf8ff";
//        return FirebaseDb.sClientAdressRef.child(clientId).orderByValue().startAt(newAddress).endAt(endText);
//    }

    public static class AddressEntry {

        public static final String ADDRESS_ND = "direcciones";
        public static final String CLIENTS_ADDRESS_KEYS = "direcciones-por-cliente";

        public static final DatabaseReference sClientsKeysRef = sDbRef.child(CLIENTS_ADDRESS_KEYS);
        public static final DatabaseReference sRef = sDbRef.child(ADDRESS_ND);

        public static Query getClientAddresByClientId(String clientId) {
            return sClientsKeysRef.child(clientId);
        }

        public static Query getClientAddresByClientIdAndSearch(String clientId, String newAddress) {
            String endText = newAddress + "\uf8ff";
            return sClientsKeysRef.child(clientId).orderByValue().startAt(newAddress).endAt(endText);
        }

    }

    /**
     * Users node
     */
//    public static final DatabaseReference sUsers = sDbRef.child(DbContract.USERS_ND);
//
//    public static Query getUserByName(String name) {
//        String endText = name + "\uf8ff";
//        return sUsers.orderByChild(DbContract.USER_NAME_KEY).startAt(name).endAt(endText);
//    }
//
//    public static DatabaseReference getUserRole(String userId) {
//        return FirebaseDb.sUsers.child(userId).child(DbContract.USER_ROLE_KEY);
//    }
//    public static DatabaseReference getUserName(String userId) {
//        return FirebaseDb.sUsers.child(userId).child(DbContract.USER_NAME_KEY);
//    }
//    public static DatabaseReference getUserEmail(String userId) {
//        return FirebaseDb.sUsers.child(userId).child(DbContract.USER_EMAIL_KEY);
//    }
//    public static DatabaseReference getUserPhotoUrl(String userId) {
//        return FirebaseDb.sUsers.child(userId).child(DbContract.USER_PHOTOURL_KEY);
//    }
//    public static DatabaseReference getUserPhone(String userId) {
//        return FirebaseDb.sUsers.child(userId).child(DbContract.USER_PHONE_KEY);
//    }
//    public static DatabaseReference getUserRut(String userId) {
//        return FirebaseDb.sUsers.child(userId).child(DbContract.USER_RUT_KEY);
//    }
//
    public static class UserEntry {

        public static final int USER_ROLE_SALESCLERK = 30;
        public static final int USER_ROLE_ADMIN = 500;
        public static final int USER_ROLE_GUEST = 200;

        public static final String USERS_ND = "usuarios";
        public static final String USER_NAME = "nombre";
        public static final String USER_ROLE = "rol";
        public static final String USER_EMAIL = "email";
        public static final String USER_PHOTO_URL = "photoUrl";
        public static final String USER_PHONE = "telefono";
        public static final String USER_RUT = "rut";


        public static final DatabaseReference sRef = sDbRef.child(USERS_ND);

        public static Query getUserByName(String name) {
            String endText = name + "\uf8ff";
            return sRef.orderByChild(USER_NAME).startAt(name).endAt(endText);
        }

        public static DatabaseReference getRefUserRole(String userId) {
            return sRef.child(userId).child(USER_ROLE);
        }
        public static DatabaseReference getRefUserName(String userId) {
            return sRef.child(userId).child(USER_NAME);
        }
        public static DatabaseReference getRefUserEmail(String userId) {
            return sRef.child(userId).child(USER_EMAIL);
        }
        public static DatabaseReference getRefUserPhotoUrl(String userId) {
            return sRef.child(userId).child(USER_PHOTO_URL);
        }
        public static DatabaseReference getRefUserPhone(String userId) {
            return sRef.child(userId).child(USER_PHONE);
        }
        public static DatabaseReference getRefUserRut(String userId) {
            return sRef.child(userId).child(USER_RUT);
        }

    }

    /**
     * Sales node
     */
//    public static final DatabaseReference sArticlesSalesRef = sDbRef.child(DbContract.ARTICLES_SALE_ND);
//    public static final DatabaseReference sSalesRef = sDbRef.child(DbContract.SALES_ND);
//    public static final DatabaseReference sSalesKeysNames = sDbRef.child(DbContract.SALES_KEYS_NAMES);
//    public static final DatabaseReference sSalesKeysRuts = sDbRef.child(DbContract.SALES_KEYS_RUTS);
//
//
//    public static Query getArticlesSalesBySaleUid(String saleUid, String description, String code ) {
//        String endtext;
//        if (description != null) {
//            endtext = description + "\uf8ff";
//            return FirebaseDb.sArticlesSalesRef.child(saleUid).orderByChild(DbContract.ARTICLES_DESCRIPTION_KEY).startAt(description).endAt(endtext);
//        } else if (code != null) {
//            endtext = code + "\uf8ff";
//            return FirebaseDb.sArticlesSalesRef.child(saleUid).orderByChild(DbContract.ARTICLES_UID_KEY).startAt(code).endAt(endtext);
//        } else {
//            return FirebaseDb.sArticlesSalesRef.child(saleUid);
//        }
//
//    }
//    public static Query getSalesKeysByClientName(String name) {
//        String endText = name + "\uf8ff";
//        return sSalesKeysNames.child(getUserId()).orderByValue().startAt(name).endAt(endText);
//    }
//    public static Query getSalesKeysByClientRut(String rut) {
//        String endText = rut + "\uf8ff";
//        return sSalesKeysRuts.child(getUserId()).orderByValue().startAt(rut).endAt(endText);
//    }

    public static class SaleEntry {

        public static final String ARTICLES_SALE_ND = "venta-articulos";
        public static final String ARTICLE_DESCRIPTION = "descripcion";
        public static final String ARTICLE_UID = "idarticulo";
        public static final String SALES_ND = "ventas";
        public static final String SALES_NAME_KEYS = "ventas-usuario-nombre-cliente";
        public static final String SALES_RUT_KEYS = "ventas-usuario-rut-cliente";


        public static final DatabaseReference sArticlesSalesRef = sDbRef.child(ARTICLES_SALE_ND);
        public static final DatabaseReference sRef = sDbRef.child(SALES_ND);
        public static final DatabaseReference sKeysNames = sDbRef.child(SALES_NAME_KEYS);
        public static final DatabaseReference sKeysRuts = sDbRef.child(SALES_RUT_KEYS);




        public static Query getArticlesSalesBySaleUid(String saleUid, String description, String code ) {
            String endtext;
            if (description != null) {
                endtext = description + "\uf8ff";
                return sArticlesSalesRef.child(saleUid).orderByChild(ARTICLE_DESCRIPTION).startAt(description).endAt(endtext);
            } else if (code != null) {
                endtext = code + "\uf8ff";
                return sArticlesSalesRef.child(saleUid).orderByChild(ARTICLE_UID).startAt(code).endAt(endtext);
            } else {
                return sArticlesSalesRef.child(saleUid);
            }

        }
        public static Query getSalesKeysByClientName(String name) {
            String endText = name + "\uf8ff";
            return sKeysNames.child(getUserId()).orderByValue().startAt(name).endAt(endText);
        }
        public static Query getSalesKeysByClientRut(String rut) {
            String endText = rut + "\uf8ff";
            return sKeysRuts.child(getUserId()).orderByValue().startAt(rut).endAt(endText);
        }

    }

    /**
     * Articles Sales node
     */


}
