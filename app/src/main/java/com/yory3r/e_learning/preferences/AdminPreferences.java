package com.yory3r.e_learning.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.yory3r.e_learning.models.AdminModel;


public class AdminPreferences 
{
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public static final String IS_LOGIN = "isLogin";
    public static final String KEY_CHECK = "check";

    public AdminPreferences(Context context)
    {
        this.context = context;

        preferences = context.getSharedPreferences("adminPreferences",Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void loginAdmin(String check)
    {
        editor.putBoolean(IS_LOGIN,true);
        editor.putString(KEY_CHECK,check);
        editor.commit();
    }

    public AdminModel getAdmin()
    {
        String check;

        check = preferences.getString(KEY_CHECK,null);

        return new AdminModel(check);
    }

    public boolean checkAdmin()
    {
        return preferences.getBoolean(IS_LOGIN,false);
    }

    public void logoutAdmin()
    {
        editor.clear();
        editor.commit();
    }
}
