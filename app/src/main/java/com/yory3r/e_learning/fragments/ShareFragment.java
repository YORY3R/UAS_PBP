package com.yory3r.e_learning.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yory3r.e_learning.databinding.FragmentShareBinding;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;


public class ShareFragment extends Fragment implements View.OnClickListener
{
    private Button btnShare;
    private FragmentShareBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = FragmentShareBinding.inflate(inflater, container, false);

        initView();
        initListener();

        return binding.getRoot();
    }

    private void initView()
    {
        btnShare = binding.btnShare;
    }

    private void initListener()
    {
        btnShare.setOnClickListener(ShareFragment.this);
    }

    @Override
    public void onClick(View view)
    {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TITLE,"E-LEARNING");
        sendIntent.putExtra(Intent.EXTRA_EMAIL,"yolifsyebathanim@yahoo.com");
        sendIntent.putExtra(Intent.EXTRA_TIME, DateFormat.getTimeInstance().format(new Date()));
        sendIntent.putExtra(Intent.EXTRA_PACKAGE_NAME, "com.yory3r.e_learning");
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Halo pengguna setia Aplikasi E-Learning, tolong Share aplikasi E-Learning yaa, supaya semua orang yang ingin belajar lebih dalam mendapatkan kemudahan dengan modul - modul yang telah disediakan di aplikasi kami. Semangat Belajar !!!");
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        binding = null;
    }
}