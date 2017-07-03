package com.pfariasmunoz.indensales.data.models;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Pablo Farias on 17-04-17.
 */

@IgnoreExtraProperties
public class Article {
    private String descripcion;
    private String precio;
    private String descuento1;
    private String descuento2;
    private String descuento3;


    public Article() {
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getDescuento1() {
        return descuento1;
    }

    public void setDescuento1(String descuento1) {
        this.descuento1 = descuento1;
    }

    public String getDescuento2() {
        return descuento2;
    }

    public void setDescuento2(String descuento2) {
        this.descuento2 = descuento2;
    }

    public String getDescuento3() {
        return descuento3;
    }

    public void setDescuento3(String descuento3) {
        this.descuento3 = descuento3;
    }
}
