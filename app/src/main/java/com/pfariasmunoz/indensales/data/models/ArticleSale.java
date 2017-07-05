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
    private Long precio;
    private Long total;
    private BigDecimal total_con_descuento;

    public ArticleSale() {
    }

    public ArticleSale(
            int cantidad,
            String descripcion,
            String idarticulo,
            String idventa,
            Long total) {
        this.cantidad = cantidad;
        this.descripcion = descripcion;
        this.idarticulo = idarticulo;
        this.idventa = idventa;
        this.total = total;
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

    public Long getPrecio() {
        return precio;
    }

    public void setPrecio(Long precio) {
        this.precio = precio;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public BigDecimal getTotalConDescuento() {
        return total_con_descuento;
    }

    public void setTotalConDescuento(BigDecimal totalConDescuento) {
        this.total_con_descuento = totalConDescuento;
    }
}
