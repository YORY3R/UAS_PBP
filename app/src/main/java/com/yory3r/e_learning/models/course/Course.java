package com.yory3r.e_learning.models.course;

public class Course
{
    private Long id;
    private String nama;
    private String deskripsi;
    private String kode;

    public Course(String nama, String deskripsi, String kode)
    {
        this.nama = nama;
        this.deskripsi = deskripsi;
        this.kode = kode;
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
}
