package com.yory3r.e_learning.dummy;

import com.yory3r.e_learning.R;
import com.yory3r.e_learning.models.AboutModel;
import com.yory3r.e_learning.models.ShareModel;

import java.util.ArrayList;

public class ShareDummy
{
    private static String url1 = "https://www.freeiconspng.com/uploads/logo-whatsapp-png-pic-0.png";
    private static String url2 = "https://www.freeiconspng.com/uploads/instagram-icon-circle-vector-logo-10.png";
    private static String url3 = "https://www.freeiconspng.com/uploads/twitter-icon--basic-round-social-iconset--s-icons-0.png";
    private static String url4 = "https://www.freeiconspng.com/uploads/facebook-logo-facebook-logo-9.png";

    private static String nama1 = "Whatsapp";
    private static String nama2 = "Instagram";
    private static String nama3 = "Twitter";
    private static String nama4 = "Facebook";

    private static String application1 = "com.whatsapp";
    private static String application2 = "com.instagram.android";
    private static String application3 = "com.twitter.android";
    private static String application4 = "com.facebook.katana";

    private static String deskripsi1 = "Share Melalui Whatsapp";
    private static String deskripsi2 = "Share Melalui Instagram";
    private static String deskripsi3 = "Share Melalui Twitter";
    private static String deskripsi4 = "Share Melalui Facebook";


    public ArrayList<ShareModel> listShare;
    public ShareDummy()
    {
        listShare = new ArrayList<>();
        listShare.add(share1);
        listShare.add(share2);
        listShare.add(share3);
        listShare.add(share4);
    }

    public static final ShareModel share1 = new ShareModel(url1, nama1, application1, deskripsi1);
    public static final ShareModel share2 = new ShareModel(url2, nama2, application2, deskripsi2);
    public static final ShareModel share3 = new ShareModel(url3, nama3, application3, deskripsi3);
    public static final ShareModel share4 = new ShareModel(url4, nama4, application4, deskripsi4);
}
