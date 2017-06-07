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
    /**
     * Metodo que entrega la uid del usuario
     * @return
     */
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


    // ***** Referencias por articulos *****
    /**
     * Referencia para el nodo de los articulos
     */
    public static final DatabaseReference sArticlesRef = getDatabase().getReference(DbContract.ARTICLES_ND);

    // *** Referencias por clientes
    /**
     * Referencias para el nodo de los clientes
     */
    public static final DatabaseReference sClientsRef = getDatabase().getReference().child(DbContract.CLIENTS_ND);

    /**
     * Kesys de la referencia de clientes que determinan el orden de los clientes por el nombre;
     */
    public static final DatabaseReference sClientsRefKeysByName = getDatabase().getReference(DbContract.CLIENTS_NAMES_REF_KEYS).child(getUserId());

    /**
     * Kesys de la referencia de clientes que determinan el orden de los clientes por el rut.
     */
    public static final DatabaseReference sClientsRefKeysByRut = getDatabase().getReference(DbContract.CLIENTS_RUTS_REF_KEYS).child(getUserId());

    /**
     *
     */

    public static final DatabaseReference sClientsRefKeysName = getDatabase().getReference(DbContract.CLIENTS_NAMES_REF_KEYS);

    public static final DatabaseReference sClientsRefKeysRut = getDatabase().getReference(DbContract.CLIENTS_RUTS_REF_KEYS);

    // *** Referencias por direcciones
    /**
     * Referencia para el nodo de las direcciones por de cada cliente.
     */
    public static final DatabaseReference sClientAdressRef = getDatabase().getReference(DbContract.CLIENT_ADDRESS_ND);
    // *** Referencias de usuario

    /**
     * Referencia para el nodo de los usuarios.
     */
    public static final DatabaseReference sUsers = getDatabase().getReference(DbContract.USERS_ND);
    // *** Referencias por las ventas

    public static final DatabaseReference sSaleReportRef = getDatabase().getReference(DbContract.SALE_REPORTS);
    public static final DatabaseReference sArticlesSalesRef = getDatabase().getReference(DbContract.ARTICLES_SALE_ND);
    public static final DatabaseReference sSalesRef = getDatabase().getReference(DbContract.SALES_ND);

    /**
     * Metodos para obtener queries de los el nodo de los clientes por nombre.
     * @param newName el texto de busqueda que ingresa el usuario.
     * @return entrega la lista de clientes que empiesan por newName.
     */
    public static Query getClientsNameQuery(String newName) {
        String endtext = newName + "\uf8ff";
        return sClientsRef.orderByChild(DbContract.CLIENT_NAME_KEY).startAt(newName).endAt(endtext);
    }

    // *** Metodos para la obtención de queries del nodo de clientes
    /**
     * Metodos para obtener queries de los el nodo de los clientes por rut.
     * @param newRut el texto de busqueda que ingresa el usuario.
     * @return entrega la lista de clientes que empiesan por newRut.
     */
    public static Query getClientsRutQuery(String newRut) {
        String endtext = newRut + "\uf8ff";
        return sClientsRef.orderByChild(DbContract.CLIENT_RUT_KEY).startAt(newRut).endAt(endtext);
    }

    // *** Metodos para la obtención de queries del nodo de articulos

    public static Query getArticlesCodeQuery(String newCode) {
        String endtext = newCode + "\uf8ff";
        return sArticlesRef.orderByKey().startAt(newCode).endAt(endtext);
    }

    public static Query getArticlesDescriptionQuery(String newDescription) {
        String endtext = newDescription + "\uf8ff";
        return sArticlesRef.orderByChild(DbContract.ARTICLES_DESCRIPTION_KEY).startAt(newDescription).endAt(endtext);
    }

    // *** Metodos para la obtención de queries del nodo de ventas

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

    // *** Metodos para la obtención de queries del nodo de direcciones

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
        return FirebaseDb.sClientAdressRef.child(clientId).orderByChild(DbContract.CLIENT_ADDRESS_AD_FD).startAt(newAddress).endAt(endText);
    }

    public static Query getClientsByUser(String userId) {
        return getDatabase().getReference(DbContract.CLIENTS_NAMES_REF_KEYS).child(userId);
    }

    // ***** Metodos para la obtencion de {@link Query} de usuarios

    public static final Query getUserByName(String name) {
        String endText = name + "\uf8ff";
        return sUsers.orderByChild(DbContract.USER_NAME_KEY).startAt(name).endAt(endText);
    }

}
