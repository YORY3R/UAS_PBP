package com.yory3r.e_learning.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.yory3r.e_learning.R;

public class FavoriteActivity extends AppCompatActivity
{

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        gotoMainActivity();
    }

    private void gotoMainActivity()
    {
        intent = new Intent(FavoriteActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}