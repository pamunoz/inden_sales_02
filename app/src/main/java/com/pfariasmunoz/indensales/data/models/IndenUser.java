package com.pfariasmunoz.indensales.data.models;

/**
 * Created by Pablo Farias on 05-06-17.
 */

public class IndenUser {
    private String nombre;
    private String rut;
    private String email;
    private String telefono;
    private String photoUrl;
    private String rol;

    public IndenUser() {
    }

    public IndenUser(String nombre, String rut, String email, String telefono, String photoUrl, String rol) {
        this.nombre = nombre;
        this.rut = rut;
        this.email = email;
        this.telefono = telefono;
        this.photoUrl = photoUrl;
        this.rol = rol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
