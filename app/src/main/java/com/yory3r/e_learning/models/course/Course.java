package com.yory3r.e_learning.models.course;

public class Course
{
    private Long id;
    private String nama;
    private String deskripsi;
    private String kode;
    private String jurusan;

    public Course(String nama, String deskripsi, String kode, String jurusan)
    {
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.kode = kode;
        this.jurusan = jurusan;
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
}
