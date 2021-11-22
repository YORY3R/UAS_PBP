package com.yory3r.e_learning.models;

public class ShareModel
{
    private String url;
    private String nama;
    private String application;
    private String deskripsi;

    public ShareModel(String url, String nama, String application, String deskripsi)
    {
        this.url = url;
        this.nama = nama;
        this.application = application;
        this.deskripsi = deskripsi;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getNama()
    {
        return nama;
    }

    public void setNama(String nama)
    {
        this.nama = nama;
    }

    public String getApplication()
    {
        return application;
    }

    public void setApplication(String application)
    {
        this.application = application;
    }

    public String getDeskripsi()
    {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi)
    {
        this.deskripsi = deskripsi;
    }
}
