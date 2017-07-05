package com.pfariasmunoz.indensales.data.models;

import java.math.BigDecimal;

/**
 * Created by Pablo Farias on 29-04-17.
 */

public class ArticleSale {
    private int cantidad;
    private String descripcion;
    private String idarticulo;
    private String idventa;
    private long precio;
    private long total;
    private double total_con_descuento;

    public ArticleSale() {
    }

    public ArticleSale(
            int cantidad,
            String descripcion,
            String idarticulo,
            String idventa,
            long total) {
        this.cantidad = cantidad;
        this.descripcion = descripcion;
        this.idarticulo = idarticulo;
        this.idventa = idventa;
        this.total = total;
    }

    public ArticleSale(int cantidad, String descripcion, String idarticulo, String idventa, long precio, long total, double total_con_descuento) {
        this.cantidad = cantidad;
        this.descripcion = descripcion;
        this.idarticulo = idarticulo;
        this.idventa = idventa;
        this.precio = precio;
        this.total = total;
        this.total_con_descuento = total_con_descuento;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getIdarticulo() {
        return idarticulo;
    }

    public void setIdarticulo(String idarticulo) {
        this.idarticulo = idarticulo;
    }

    public String getIdventa() {
        return idventa;
    }

    public void setIdventa(String idventa) {
        this.idventa = idventa;
    }

    public long getPrecio() {
        return precio;
    }

    public void setPrecio(Long precio) {
        this.precio = precio;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public double getTotalConDescuento() {
        return total_con_descuento;
    }

    public void setTotalConDescuento(double totalConDescuento) {
        this.total_con_descuento = totalConDescuento;
    }
}
