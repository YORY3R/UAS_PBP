package com.yory3r.e_learning.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.yory3r.e_learning.R;
import com.yory3r.e_learning.databinding.ItemGameBinding;
import com.yory3r.e_learning.models.GameModel;


import java.util.ArrayList;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.viewHolder>
{
    private Context context;
    private ArrayList<GameModel> listGame;
    private GameModel game;
    private Intent intent;
    private ItemGameBinding binding;

    public GameAdapter(Context context, ArrayList<GameModel> listGame)
    {
        this.context = context;
        this.listGame = listGame;
    }

    public class viewHolder extends RecyclerView.ViewHolder
    {
        private TextView tvJudul;

        public viewHolder(@NonNull ItemGameBinding binding)
        {
            super(binding.getRoot());

            tvJudul = binding.tvJudul;
        }
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        int layout = R.layout.item_game;

        binding = DataBindingUtil.inflate(inflater,layout,parent,false);
        return new GameAdapter.viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position)
    {
        game = listGame.get(position);

        holder.tvJudul.setText(game.getJudul());
    }

    @Override
    public int getItemCount()
    {
        return listGame.size();
    }


}
