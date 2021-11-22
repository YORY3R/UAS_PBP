package com.yory3r.e_learning.adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.viewHolder>
{

    public class viewHolder extends RecyclerView.ViewHolder
    {
        public viewHolder(@NonNull View itemView)
        {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position)
    {

    }

    @Override
    public int getItemCount()
    {
        return 0;
    }


}
