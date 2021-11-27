package com.yory3r.e_learning.models;

public class GameModel
{
    private String judul;
    private String deskripsi;
    private double rating;

    public GameModel(String judul, String deskripsi, double rating)
    {
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.rating = rating;
    }

    public String getJudul()
    {
        return judul;
    }

    public void setJudul(String judul)
    {
        this.judul = judul;
    }

    public String getDeskripsi()
    {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi)
    {
        this.deskripsi = deskripsi;
    }

    public double getRating()
    {
        return rating;
    }

    public void setRating(double rating)
    {
        this.rating = rating;
    }
}
