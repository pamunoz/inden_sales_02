package com.pfariasmunoz.indensales.data;

/**
 * Created by Pablo Farias on 16-04-17.
 */

public class DbContract {
    /**
     * Nombres del nodo articulos.
     */
    protected static final String ARTICLES_ND = "articulos";
    /**
     * Nombres del nodo clientes.
     */
    protected static final String CLIENTS_ND = "clientes";
    protected static final String CLIENT_ADDRESS_ND_KEYS = "direcciones-por-cliente";
    protected static final String CLIENT_ADDRESS_AD_FD = "direccion";


    // Referencia de key pra busqueda por nombre del cliente
    protected static final String CLIENTS_NAMES_REF_KEYS = "clientes-nombre-por-usuario";
    // Referencia de key pra busqueda por nombre del cliente
    protected static final String CLIENTS_RUTS_REF_KEYS = "clientes-rut-por-usuario";
    /**
     * Nombres del nodo de Ventas
     */
    protected static final String SALE_REPORTS = "reporte-ventas";
    protected static final String SALE_REPORT_CLIENT_NAME_FD = "nombre_cliente";
    protected static final String SALE_REPORT_CLIENT_RUT_FD = "rut_cliente";
    protected static final String ARTICLES_SALE_ND = "venta-articulos";
    protected static final String SALES_ND = "ventas";

    /**
     * Nombres del nodo de direcciones.
     */
    protected static final String CLIENT_NAME_KEY = "nombre";
    protected static final String CLIENT_RUT_KEY = "rut";
    protected static final String ARTICLES_DESCRIPTION_KEY = "descripcion";
    protected static final String ARTICLES_UID_KEY = "idarticulo";
    protected static final String ADDRESSES = "direcciones";

    /**
     * Valores de usuario.
     */
    protected static final String EMPTY_STRING_VALUE = "Not available";
    public static final int USER_ROLE_SALESCLERK = 30;
    public static final int USER_ROLE_ADMIN = 500;
    public static final int USER_ROLE_GUEST = 200;
    protected static final String USERS_ND = "usuarios";

    protected static final String USER_EMAIL_KEY = "email";
    protected static final String USER_NAME_KEY = "nombre";
    protected static final String USER_PHOTOURL_KEY = "photoUrl";
    protected static final String USER_ROLE_KEY = "role";
    protected static final String USER_RUT_KEY = "rut";
    protected static final String USER_PHONE_KEY = "telefono";

    protected static final String SALES_KEYS_NAMES = "ventas-usuario-nombre-cliente";
    protected static final String SALES_KEYS_RUTS = "ventas-usuario-rut-cliente";

}
