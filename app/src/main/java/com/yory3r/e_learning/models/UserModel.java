package com.yory3r.e_learning.models;

import java.math.BigInteger;

public class UserModel
{
    private String foto;
    private String nama;
    private String npm;
    private String nomorTelepon;
    private String email;
    private String password;

    public UserModel(){}

    public String getFoto()
    {
        return foto;
    }

    public void setFoto(String foto)
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

    public String getNomorTelepon()
    {
        return nomorTelepon;
    }

    public void setNomorTelepon(String nomorTelepon)
    {
        this.nomorTelepon = nomorTelepon;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
