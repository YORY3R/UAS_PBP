package com.yory3r.e_learning.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yory3r.e_learning.adapters.ShareAdapter;
import com.yory3r.e_learning.databinding.FragmentShareBinding;
import com.yory3r.e_learning.models.ShareModel;
import com.yory3r.e_learning.dummy.ShareDummy;

import java.util.ArrayList;


public class ShareFragment extends Fragment
{

    private FragmentShareBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = FragmentShareBinding.inflate(inflater, container, false);



        ArrayList<ShareModel> listShare = new ShareDummy().listShare;
        ShareAdapter adapter = new ShareAdapter(getContext(),listShare);

        RecyclerView shareRecycler = binding.shareRecycler;

        shareRecycler.setAdapter(adapter);
        shareRecycler.setLayoutManager(new GridLayoutManager(getContext(),2));









        return binding.getRoot();
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        binding = null;
    }
}