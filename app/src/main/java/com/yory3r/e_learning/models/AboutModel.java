package com.yory3r.e_learning.models;

import java.util.ArrayList;

public class AboutModel
{
    private int foto;
    private String nama;
    private String npm;
    private String nomorHp;

    public AboutModel(int foto, String nama, String npm, String nomorHp)
    {
        this.foto = foto;
        this.nama = nama;
        this.npm = npm;
        this.nomorHp = nomorHp;
    }

    public int getFoto()
    {
        return foto;
    }

    public void setFoto(int foto)
    {
        this.foto = foto;
    }

    public String getNama()
    {
        return nama;
    }

    public void setNama(String nama)
    {
        this.nama = nama;
    }

    public String getNpm()
    {
        return npm;
    }

    public void setNpm(String npm)
    {
        this.npm = npm;
    }

    public String getNomorHp()
    {
        return nomorHp;
    }

    public void setNomorHp(String nomorHp)
    {
        this.nomorHp = nomorHp;
    }
}
