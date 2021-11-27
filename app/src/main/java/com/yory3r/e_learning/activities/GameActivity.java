package com.yory3r.e_learning.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.yory3r.e_learning.R;
import com.yory3r.e_learning.adapters.AboutAdapter;
import com.yory3r.e_learning.adapters.GameAdapter;
import com.yory3r.e_learning.databinding.ActivityGameBinding;
import com.yory3r.e_learning.dummy.AboutDummy;
import com.yory3r.e_learning.dummy.GameDummy;
import com.yory3r.e_learning.models.AboutModel;
import com.yory3r.e_learning.models.GameModel;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity
{
    private RecyclerView gameRecycler;
    private Intent intent;
    private ActivityGameBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        int layout = R.layout.activity_game;
        binding = DataBindingUtil.setContentView(GameActivity.this,layout);
        binding.setActivityGame(GameActivity.this);

        ArrayList<GameModel> listGame = new GameDummy().listGame;
        GameAdapter adapter = new GameAdapter(GameActivity.this,listGame);

        gameRecycler = binding.gameRecycler;

        gameRecycler.setAdapter(adapter);
        gameRecycler.setLayoutManager(new LinearLayoutManager(GameActivity.this));







    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        gotoMainActivity();
    }

    private void gotoMainActivity()
    {
        intent = new Intent(GameActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}