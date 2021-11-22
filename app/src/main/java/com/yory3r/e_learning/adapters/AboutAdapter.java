package com.yory3r.e_learning.adapters;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.yory3r.e_learning.R;
import com.yory3r.e_learning.databinding.FragmentAboutBinding;
import com.yory3r.e_learning.databinding.ItemAboutBinding;
import com.yory3r.e_learning.models.AboutModel;

import java.util.ArrayList;

public class AboutAdapter extends RecyclerView.Adapter<AboutAdapter.viewHolder>
{
    private Context context;
    private ArrayList<AboutModel> listAbout;
    private AboutModel about;
    private ItemAboutBinding binding;

    public AboutAdapter(Context context, ArrayList<AboutModel> listAbout)
    {
        this.context = context;
        this.listAbout = listAbout;
    }

    public class viewHolder extends RecyclerView.ViewHolder
    {
        private ImageView ivFoto;
        private TextView tvNama;
        private TextView tvNpm;
        private TextView tvNomorHp;

        public viewHolder(@NonNull ItemAboutBinding binding)
        {
            super(binding.getRoot());

            ivFoto = binding.ivFoto;
            tvNama = binding.tvNama;
            tvNpm = binding.tvNpm;
            tvNomorHp = binding.tvNomorHp;

        }
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        int layout = R.layout.item_about;

        binding = DataBindingUtil.inflate(inflater,layout,parent,false);
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position)
    {
        about = listAbout.get(position);

        holder.ivFoto.setImageResource(about.getFoto());
        holder.tvNama.setText(about.getNama());
        holder.tvNpm.setText(about.getNpm());
        holder.tvNomorHp.setText(about.getNomorHp());
    }

    @Override
    public int getItemCount()
    {
        return listAbout.size();
    }
}
