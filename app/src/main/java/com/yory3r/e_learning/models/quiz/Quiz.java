package com.yory3r.e_learning.models.quiz;

public class Quiz
{
    private Long id;
    private String nama;
    private String kode;
    private String pertanyaan;
    private String jawaban;
    private String urlfoto;

    public Quiz(String nama, String kode, String pertanyaan, String jawaban, String urlfoto)
    {
        this.nama = nama;
        this.kode = kode;
        this.pertanyaan = pertanyaan;
        this.jawaban = jawaban;
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

    public String getPertanyaan()
    {
        return pertanyaan;
    }

    public void setPertanyaan(String pertanyaan)
    {
        this.pertanyaan = pertanyaan;
    }

    public String getJawaban()
    {
        return jawaban;
    }

    public void setJawaban(String jawaban)
    {
        this.jawaban = jawaban;
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