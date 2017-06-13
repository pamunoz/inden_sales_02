package com.pfariasmunoz.indensales.data.models;

/**
 * Created by Pablo Farias on 16-05-17.
 */

public class Sale {
    public boolean aprob;
    public String iddireccion;
    public String idcliente;
    public String idvendedor;
    public String nombre_cliente;
    public String rut_cliente;
    public long total;
    public long timestamp;
    public String direccion;

    public Sale() {
    }

    public Sale(boolean aprob, String iddireccion, String idcliente, String idvendedor, String nombre_cliente, String rut_cliente, long total, long timestamp, String direccion) {
        this.aprob = aprob;
        this.iddireccion = iddireccion;
        this.idcliente = idcliente;
        this.idvendedor = idvendedor;
        this.nombre_cliente = nombre_cliente;
        this.rut_cliente = rut_cliente;
        this.total = total;
        this.timestamp = timestamp;
        this.direccion = direccion;
    }
}
