package com.pfariasmunoz.indensales.data.models;

/**
 * Created by Pablo Farias on 16-05-17.
 */

public class Sale {
    private boolean aprob;
    private String iddireccion;
    private String idcliente;
    private String idvendedor;
    private String nombre_cliente;
    private String rut_cliente;
    private Long total;
    private Long total_con_descuento;
    private Long timestamp;
    private String direccion;

    public Sale() {
    }

    public Sale(boolean aprob, String iddireccion, String idcliente, String idvendedor, String nombre_cliente, String rut_cliente, Long total, Long timestamp, String direccion) {
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

    public Sale(boolean aprob, String iddireccion, String idcliente, String idvendedor, String nombre_cliente, String rut_cliente, Long total, Long totalConDescuento, Long timestamp, String direccion) {
        this.aprob = aprob;
        this.iddireccion = iddireccion;
        this.idcliente = idcliente;
        this.idvendedor = idvendedor;
        this.nombre_cliente = nombre_cliente;
        this.rut_cliente = rut_cliente;
        this.total = total;
        this.total_con_descuento = totalConDescuento;
        this.timestamp = timestamp;
        this.direccion = direccion;
    }

    public boolean isAprob() {
        return aprob;
    }

    public void setAprob(boolean aprob) {
        this.aprob = aprob;
    }

    public String getIddireccion() {
        return iddireccion;
    }

    public void setIddireccion(String iddireccion) {
        this.iddireccion = iddireccion;
    }

    public String getIdcliente() {
        return idcliente;
    }

    public void setIdcliente(String idcliente) {
        this.idcliente = idcliente;
    }

    public String getIdvendedor() {
        return idvendedor;
    }

    public void setIdvendedor(String idvendedor) {
        this.idvendedor = idvendedor;
    }

    public String getNombre_cliente() {
        return nombre_cliente;
    }

    public void setNombre_cliente(String nombre_cliente) {
        this.nombre_cliente = nombre_cliente;
    }

    public String getRut_cliente() {
        return rut_cliente;
    }

    public void setRut_cliente(String rut_cliente) {
        this.rut_cliente = rut_cliente;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getTotalConDescuento() {
        return total_con_descuento;
    }

    public void setTotalConDescuento(Long totalConDescuento) {
        this.total_con_descuento = totalConDescuento;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
