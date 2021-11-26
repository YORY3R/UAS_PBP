package com.yory3r.e_learning.models;

import java.math.BigInteger;

public class UserModel
{
    private String foto;
    private String nama;
    private String tanggalLahir;
    private String jenisKelamin;
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

    public String getTanggalLahir()
    {
        return tanggalLahir;
    }

    public void setTanggalLahir(String tanggalLahir)
    {
        this.tanggalLahir = tanggalLahir;
    }

    public String getJenisKelamin()
    {
        return jenisKelamin;
    }

    public void setJenisKelamin(String jenisKelamin)
    {
        this.jenisKelamin = jenisKelamin;
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