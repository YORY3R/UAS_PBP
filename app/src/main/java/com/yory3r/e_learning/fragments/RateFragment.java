package com.yory3r.e_learning.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yory3r.e_learning.R;
import com.yory3r.e_learning.adapters.ShareAdapter;
import com.yory3r.e_learning.databinding.FragmentRateBinding;
import com.yory3r.e_learning.databinding.FragmentShareBinding;
import com.yory3r.e_learning.dummy.ShareDummy;
import com.yory3r.e_learning.models.ShareModel;

import java.util.ArrayList;

public class RateFragment extends Fragment
{
    private FragmentRateBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = FragmentRateBinding.inflate(inflater, container, false);


        return binding.getRoot();
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        binding = null;
    }
}