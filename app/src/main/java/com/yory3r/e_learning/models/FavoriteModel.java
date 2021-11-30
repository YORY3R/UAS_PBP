package com.yory3r.e_learning.models;

public class FavoriteModel
{
    private long id;
    private String nama;
    private String deskripsi;
    private String kode;
    private String jurusan;
    private boolean isFavorite;

    public FavoriteModel(){}

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getNama()
    {
        return nama;
    }

    public void setNama(String nama)
    {
        this.nama = nama;
    }

    public String getDeskripsi()
    {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi)
    {
        this.deskripsi = deskripsi;
    }

    public String getKode()
    {
        return kode;
    }

    public void setKode(String kode)
    {
        this.kode = kode;
    }

    public String getJurusan()
    {
        return jurusan;
    }

    public void setJurusan(String jurusan)
    {
        this.jurusan = jurusan;
    }

    public boolean getFavorite()
    {
        return isFavorite;
    }

    public void setFavorite(boolean favorite)
    {
        isFavorite = favorite;
    }
}
