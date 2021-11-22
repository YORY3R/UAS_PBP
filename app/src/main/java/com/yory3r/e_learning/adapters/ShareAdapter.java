package com.yory3r.e_learning.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yory3r.e_learning.R;
import com.yory3r.e_learning.databinding.ItemShareBinding;
import com.yory3r.e_learning.models.ShareModel;

import java.util.ArrayList;

public class ShareAdapter extends RecyclerView.Adapter<ShareAdapter.viewHolder>
{
    private Context context;
    private ArrayList<ShareModel> listShare;
    private ShareModel share;
    private Intent intent;
    private ItemShareBinding binding;

    public ShareAdapter(Context context, ArrayList<ShareModel> listShare)
    {
        this.context = context;
        this.listShare = listShare;
    }

    public class viewHolder extends RecyclerView.ViewHolder
    {
        private ImageView ivFoto;
        private TextView tvNama;

        public viewHolder(@NonNull ItemShareBinding binding)
        {
            super(binding.getRoot());

            ivFoto = binding.ivFoto;
            tvNama = binding.tvNama;
        }
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        int layout = R.layout.item_share;

        binding = DataBindingUtil.inflate(inflater,layout,parent,false);
        return new viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position)
    {
        share = listShare.get(position);

        Glide.with(context).load(share.getUrl()).placeholder(R.drawable.ic_menu_camera).into(holder.ivFoto);

        holder.tvNama.setText(share.getNama());

        holder.ivFoto.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String application = share.getApplication();
                String judul = "E-Learning";
                String deskripsi = share.getDeskripsi();

                shareApplication(application,judul,deskripsi);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return listShare.size();
    }

    private void shareApplication(String application, String judul, String deskripsi)
    {
        if(checkApplicationInstall(application))
        {
            intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setPackage(application);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TITLE, judul);
            intent.putExtra(Intent.EXTRA_TEXT, deskripsi);

            context.startActivity(intent);
        }
        else
        {
            Toast.makeText(context, "Aplikasi Belum Terinstal !", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkApplicationInstall(String application)
    {
        PackageManager pm = context.getPackageManager();

        try
        {
            pm.getPackageInfo(application, PackageManager.GET_ACTIVITIES);
            return true;
        }
        catch (PackageManager.NameNotFoundException ignored)
        {
            return false;
        }
    }
}
