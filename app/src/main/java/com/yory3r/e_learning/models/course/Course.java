package com.yory3r.e_learning.models.course;

public class Course
{
    private Long id;
    private String nama;
    private String kode;
    private String jurusan;
    private String deskripsi;
    private String urlfoto;

    public Course(String nama, String kode, String jurusan, String deskripsi, String urlfoto)
    {
        this.nama = nama;
        this.kode = kode;
        this.jurusan = jurusan;
        this.deskripsi = deskripsi;
        this.urlfoto = urlfoto;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
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

    public String getDeskripsi()
    {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi)
    {
        this.deskripsi = deskripsi;
    }

    public String getUrlfoto()
    {
        return urlfoto;
    }

    public void setUrlfoto(String urlfoto)
    {
        this.urlfoto = urlfoto;
    }
}
