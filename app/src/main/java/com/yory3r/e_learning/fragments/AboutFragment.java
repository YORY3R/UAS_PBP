package com.yory3r.e_learning.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yory3r.e_learning.adapters.AboutAdapter;
import com.yory3r.e_learning.databinding.FragmentAboutBinding;
import com.yory3r.e_learning.models.AboutModel;
import com.yory3r.e_learning.dummy.AboutDummy;

import java.util.ArrayList;

public class AboutFragment extends Fragment
{
    private FragmentAboutBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = FragmentAboutBinding.inflate(inflater, container, false);


        ArrayList<AboutModel> listAbout = new AboutDummy().listAbout;
        AboutAdapter adapter = new AboutAdapter(getContext(),listAbout);

        RecyclerView aboutRecycler = binding.aboutRecycler;

        aboutRecycler.setAdapter(adapter);
        aboutRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        // TODO: 27/11/2021 JADDIN INIT VIEW DLL 









        return binding.getRoot();
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        binding = null;
    }
}