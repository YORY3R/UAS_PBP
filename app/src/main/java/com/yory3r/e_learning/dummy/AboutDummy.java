package com.yory3r.e_learning.dummy;

import com.yory3r.e_learning.R;
import com.yory3r.e_learning.models.AboutModel;

import java.util.ArrayList;

public class AboutDummy
{
    private static int foto1 = R.drawable.picture_yolif;
    private static int foto2 = R.drawable.picture_tegar;
    private static int foto3 = R.drawable.picture_stanley;

    private static String nama1 = "Yolif Syebathanim";
    private static String nama2 = "Geraldo Tegar Sanchaka";
    private static String nama3 = "Antonius Stanley Waskita";

    private static String npm1 = "190710072";
    private static String npm2 = "190710076";
    private static String npm3 = "190710100";

    private static String nomorHp1 = "081542701211";
    private static String nomorHp2 = "08113336101";
    private static String nomorHp3 = "081390466525";

    public ArrayList<AboutModel> listAbout;
    public AboutDummy()
    {
        listAbout = new ArrayList<>();

        listAbout.add(creator1);
        listAbout.add(creator2);
        listAbout.add(creator3);

    }

    public static final AboutModel creator1 = new AboutModel(foto1, nama1, npm1, nomorHp1);
    public static final AboutModel creator2 = new AboutModel(foto2, nama2, npm2, nomorHp2);
    public static final AboutModel creator3 = new AboutModel(foto3, nama3, npm3, nomorHp3);
}
