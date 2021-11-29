package com.yory3r.e_learning.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView.LayoutManager;
import com.android.volley.RequestQueue;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yory3r.e_learning.R;
import com.yory3r.e_learning.adapters.FavoriteAdapter;
import com.yory3r.e_learning.adapters.FavoriteAdapter;
import com.yory3r.e_learning.databinding.ActivityAdminBinding;
import com.yory3r.e_learning.databinding.ActivityFavoriteBinding;
import com.yory3r.e_learning.models.FavoriteModel;
import com.yory3r.e_learning.utils.ChangeString;

import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener
{

    private FavoriteAdapter adapter;
    private LayoutManager manager;
    private RecyclerView rvFavorite;
    private SearchView svFavorite;
    private SwipeRefreshLayout srFavorite;

    private ChangeString change;

    private FirebaseAuth auth;
    private FirebaseUser user;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    
    private Intent intent;
    private ActivityFavoriteBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        int layout = R.layout.activity_favorite;
        binding = DataBindingUtil.setContentView(FavoriteActivity.this,layout);
        binding.setActivityFavorite(FavoriteActivity.this);

        initFirebase();
        initView();
        initListener();
        initAdapter();
    }
    private void initFirebase()
    {
        change = new ChangeString();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Favorite/" + change.DotsToEtc(user.getEmail()));// TODO: 28/11/2021 GANTI JADI DIPISAH
    }

    private void initView()
    {
        rvFavorite = binding.rvFavorite;
        svFavorite = binding.svFavorite;
        srFavorite = binding.srFavorite;
    }

    private void initListener()
    {
        svFavorite.setOnQueryTextListener(FavoriteActivity.this);
        srFavorite.setOnRefreshListener(FavoriteActivity.this);
    }

    public void initAdapter()
    {
        ArrayList<FavoriteModel> listFavorite = new ArrayList<>();
        listFavorite.clear();

        for(int a = 0 ; a < 1000 ; a++)
        {
            databaseReference.child(String.valueOf(a)).addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    if(snapshot.getValue() != null)
                    {
                        listFavorite.add(snapshot.getValue(FavoriteModel.class));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error)
                {
                    Toast.makeText(FavoriteActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        adapter = new FavoriteAdapter(FavoriteActivity.this, listFavorite);
        manager = new LinearLayoutManager(FavoriteActivity.this);

        rvFavorite.setLayoutManager(manager);
        rvFavorite.setAdapter(adapter);
    }

    @Override
    public boolean onQueryTextSubmit(String s)
    {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s)
    {
        return false;
    }

    @Override
    public void onRefresh()
    {
        initAdapter();

        srFavorite.setRefreshing(false);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        gotoMainActivity();
    }

    private void gotoMainActivity()
    {
        intent = new Intent(FavoriteActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart()
    {
        super.onStart();


    }
}