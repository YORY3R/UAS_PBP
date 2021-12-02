package com.yory3r.e_learning.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yory3r.e_learning.R;
import com.yory3r.e_learning.activities.FavoriteActivity;
import com.yory3r.e_learning.databinding.ItemFavoriteBinding;
import com.yory3r.e_learning.databinding.LayoutDeskripsiBinding;
import com.yory3r.e_learning.models.FavoriteModel;
import com.yory3r.e_learning.utils.ChangeString;

import java.util.ArrayList;
import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.viewHolder>
{
    private Context context;
    private ArrayList<FavoriteModel> listFavorite;
    private FavoriteModel favorite;

    private ChangeString change;

    private AlertDialog.Builder builder;
    private LayoutInflater inflater;
    private View layoutDeskripsi;
    private TextView tvDeskripsi;

    private FirebaseAuth auth;
    private FirebaseUser user;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private LayoutDeskripsiBinding layoutBinding;
    private ItemFavoriteBinding binding;

    public FavoriteAdapter(Context context, ArrayList<FavoriteModel> listFavorite)
    {
        this.context = context;
        this.listFavorite = listFavorite;

        initFirebase();
        initAlertDialog();
    }

    private void initFirebase()
    {
        change = new ChangeString();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Favorite/" + change.DotsToEtc(user.getEmail()));
    }

    private void initAlertDialog()
    {
        builder = new AlertDialog.Builder(context);

        inflater = LayoutInflater.from(builder.getContext());

        int layout = R.layout.layout_deskripsi;
        layoutBinding = DataBindingUtil.inflate(inflater,layout,null,false);
        layoutBinding.setLayoutDeskripsiFavorite(FavoriteAdapter.this);

        layoutDeskripsi = layoutBinding.getRoot();
        tvDeskripsi =  layoutBinding.tvDeskripsi;
    }

    public class viewHolder extends RecyclerView.ViewHolder
    {
        private ImageView ivFoto;
        private TextView tvNama;
        private TextView tvKode;
        private TextView tvJurusan;
        private Button btnDelete;
        private Button btnDeskripsi;

        public viewHolder(@NonNull ItemFavoriteBinding binding)
        {
            super(binding.getRoot());

            ivFoto = binding.ivFoto;
            tvNama = binding.tvNama;
            tvKode = binding.tvKode;
            tvJurusan = binding.tvJurusan;
            btnDelete = binding.btnDelete;
            btnDeskripsi = binding.btnDeskripsi;

        }
    }

    @NonNull
    @Override
    public FavoriteAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        int layout = R.layout.item_favorite;

        binding = DataBindingUtil.inflate(inflater,layout,parent,false);
        return new FavoriteAdapter.viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.viewHolder holder, int position)
    {
        favorite = listFavorite.get(position);

        Glide.with(context).load(favorite.getUrlfoto()).into(holder.ivFoto);

        holder.tvNama.setText(favorite.getNama());
        holder.tvKode.setText(favorite.getKode());
        holder.tvJurusan.setText(favorite.getJurusan());
        
        holder.btnDelete.setOnClickListener(new View.OnClickListener() 
        {
            @Override
            public void onClick(View view) 
            {
                int position = holder.getAdapterPosition();
                favorite = listFavorite.get(position);

                long id = favorite.getId();

                databaseReference.child(String.valueOf(id)).addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        if(snapshot.getValue() != null)
                        {
                            databaseReference.child(String.valueOf(id)).removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if(task.isSuccessful())
                                    {
                                        holder.btnDelete.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24);
                                        Toast.makeText(context, "Berhasil Hapus Favorite", Toast.LENGTH_SHORT).show();
                                        ((FavoriteActivity) context).initAdapter();
                                    }
                                    else
                                    {
                                        Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(context, "Course Sudah Dihapus !", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {

                    }
                });
            }
        });

        holder.btnDeskripsi.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                initAlertDialog();

                tvDeskripsi.setText(listFavorite.get(holder.getAdapterPosition()).getDeskripsi());


                builder.setTitle("Deskripsi");
                builder.setView(layoutDeskripsi);
                builder.setPositiveButton("OK",null);
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return listFavorite.size();
    }

    public Filter getFilter()
    {
        return new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence)
            {
                String charSequenceString = charSequence.toString();
                List<FavoriteModel> filtered = new ArrayList<>();

                if(charSequenceString.isEmpty())
                {
                    filtered.addAll(listFavorite);
                }
                else
                {
                    for(FavoriteModel FavoriteModel : listFavorite)
                    {
                        if(FavoriteModel.getNama().toLowerCase().contains(charSequenceString.toLowerCase()))
                        {
                            filtered.add(FavoriteModel);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filtered;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults)
            {
                listFavorite.clear();
                listFavorite.addAll((List<FavoriteModel>) filterResults.values);

                notifyDataSetChanged();
            }
        };
    }
}