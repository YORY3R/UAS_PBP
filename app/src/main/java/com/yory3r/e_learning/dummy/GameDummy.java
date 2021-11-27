package com.yory3r.e_learning.dummy;

import com.yory3r.e_learning.models.GameModel;
import com.yory3r.e_learning.models.ShareModel;

import java.util.ArrayList;

public class GameDummy
{
    private static String judul1 = "Whatsapp";
    private static String judul2 = "Instagram";
    private static String judul3 = "Twitter";
    private static String judul4 = "Facebook";

    private static String deskripsi1 = "com.whatsapp";
    private static String deskripsi2 = "com.instagram.android";
    private static String deskripsi3 = "com.twitter.android";
    private static String deskripsi4 = "com.facebook.katana";

    private static double rating1 = 4.8;
    private static double rating2 = 3.6;
    private static double rating3 = 4.75;
    private static double rating4 = 3.67;


    public ArrayList<GameModel> listGame;
    public GameDummy()
    {
        listGame = new ArrayList<>();
        listGame.add(game1);
        listGame.add(game2);
        listGame.add(game3);
        listGame.add(game4);
    }

    public static final GameModel game1 = new GameModel(judul1, deskripsi1, rating1);
    public static final GameModel game2 = new GameModel(judul2, deskripsi2, rating2);
    public static final GameModel game3 = new GameModel(judul3, deskripsi3, rating3);
    public static final GameModel game4 = new GameModel(judul4, deskripsi4, rating4);
}
