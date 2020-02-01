package com.example.animelittle.model;

import java.io.Serializable;

public class Anime implements Serializable {

    private int id;
    private String titulo;
    private String estreno;
    private byte[] imagen;
    private boolean favorito;
    private String url;
    private String info;

    public Anime(int id, String titulo, boolean favorito, String estreno, byte[] imagen, String url, String info) {
        this.id = id;
        this.titulo = titulo;
        this.favorito = favorito;
        this.estreno = estreno;
        this.imagen = imagen;
        this.url = url;
        this.info = info;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public byte[] getFoto() {
        return imagen;
    }

    public void setFoto(byte[] imagen) {
        this.imagen = imagen;
    }

    public boolean getFavorito() {
        return favorito;
    }

    public void setFavorito(boolean favorito) {
        this.favorito = favorito;
    }

    public String getEstreno() {
        return estreno;
    }

    public void setEstreno(String estreno) {
        this.estreno = estreno;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
