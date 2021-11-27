package com.yory3r.e_learning.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.yory3r.e_learning.models.EditAkunModel;

public class EditAkunPreferences
{
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public static final String IS_LOGIN = "isLogin";
    public static final String KEY_CHECK = "check";

    public EditAkunPreferences(Context context)
    {
        this.context = context;

        preferences = context.getSharedPreferences("editAkunPreferences",Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void setEdit(String check)
    {
        editor.putBoolean(IS_LOGIN,true);
        editor.putString(KEY_CHECK,check);
        editor.commit();
    }

    public EditAkunModel getEdit()
    {
        String check;

        check = preferences.getString(KEY_CHECK,null);

        return new EditAkunModel(check);
    }

    public boolean checkEdit()
    {
        return preferences.getBoolean(IS_LOGIN,false);
    }

    public void uncheckEdit()
    {
        editor.clear();
        editor.commit();
    }
}
