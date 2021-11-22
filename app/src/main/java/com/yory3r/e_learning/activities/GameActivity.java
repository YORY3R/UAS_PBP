package com.yory3r.e_learning.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.yory3r.e_learning.R;
import com.yory3r.e_learning.databinding.ActivityGameBinding;

public class GameActivity extends AppCompatActivity
{
    private RecyclerView view;
    private ActivityGameBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        int layout = R.layout.activity_game;
        binding = DataBindingUtil.setContentView(GameActivity.this,layout);
        binding.setActivityGame(GameActivity.this);


        view = binding.view;


//        view.set




    }
}