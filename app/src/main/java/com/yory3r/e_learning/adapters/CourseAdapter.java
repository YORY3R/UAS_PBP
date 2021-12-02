package com.yory3r.e_learning.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
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
import com.yory3r.e_learning.activities.MainActivity;
import com.yory3r.e_learning.databinding.ItemCourseBinding;
import com.yory3r.e_learning.databinding.LayoutDeskripsiBinding;
import com.yory3r.e_learning.models.FavoriteModel;
import com.yory3r.e_learning.models.UserModel;
import com.yory3r.e_learning.models.course.Course;
import com.yory3r.e_learning.utils.ChangeString;

import java.util.ArrayList;
import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.viewHolder>
{
    private List<Course> courseList;
    private List<Course> filteredCourseList;
    private Context context;
    private Course course;
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
    private ItemCourseBinding binding;

    public CourseAdapter(List<Course> courseList, Context context)
    {
        this.courseList = courseList;
        this.context = context;

        filteredCourseList = new ArrayList<>(courseList);

        initFirebase();
        initAlertDialog();
    }

    // TODO: 28/11/2021 BUAT JADI INIT FIREBASE
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
        layoutBinding.setLayoutDeskripsi(CourseAdapter.this);

        layoutDeskripsi = layoutBinding.getRoot();
        tvDeskripsi =  layoutBinding.tvDeskripsi;
    }

    public class viewHolder extends RecyclerView.ViewHolder
    {
        private ImageView ivFoto;
        private TextView tvNama;
        private TextView tvKode;
        private TextView tvJurusan;
        private Button btnTambah;
        private Button btnDeskripsi;

        public viewHolder(@NonNull ItemCourseBinding binding)
        {
            super(binding.getRoot());

            ivFoto = binding.ivFoto;
            tvNama = binding.tvNama;
            tvKode = binding.tvKode;
            tvJurusan = binding.tvJurusan;
            btnTambah = binding.btnTambah;
            btnDeskripsi = binding.btnDeskripsi;
        }
    }
    
    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        int layout = R.layout.item_course;

        binding = DataBindingUtil.inflate(inflater,layout,parent,false);
        return new CourseAdapter.viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position)
    {
        course = filteredCourseList.get(position);

        Glide.with(context).load(course.getUrlfoto()).into(holder.ivFoto);

        holder.tvNama.setText(course.getNama());
        holder.tvKode.setText(course.getKode());
        holder.tvJurusan.setText(course.getJurusan());

        databaseReference.child(String.valueOf(course.getId()))
        .addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue() != null)
                {
                    if(snapshot.getValue(FavoriteModel.class).getFavorite())
                    {
                        holder.btnTambah.setBackgroundResource(R.drawable.ic_baseline_favorite_24);
                        addFavorite(holder);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        holder.btnTambah.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                addFavorite(holder);

                int position = holder.getAdapterPosition();
                course = filteredCourseList.get(position);

                long id = course.getId();
                String nama = course.getNama();

                databaseReference.child(String.valueOf(id))
                .addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        Toast.makeText(context, "Berhasil Tambah Favorite", Toast.LENGTH_SHORT).show();

                        holder.btnTambah.setBackgroundResource(R.drawable.ic_baseline_favorite_24);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error){}
                });
            }
        });

        holder.btnDeskripsi.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                initAlertDialog();

                tvDeskripsi.setText(filteredCourseList.get(holder.getAdapterPosition()).getDeskripsi());
                
                builder.setTitle("Deskripsi");
                builder.setView(layoutDeskripsi);
                builder.setPositiveButton("OK", null);
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return filteredCourseList.size();
    }

    public void setCourseList(List<Course> courseList)
    {
        this.courseList = courseList;
        filteredCourseList = new ArrayList<>(courseList);
    }
    
    public Filter getFilter()
    {
        return new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence)
            {
                String charSequenceString = charSequence.toString();
                List<Course> filtered = new ArrayList<>();

                if (charSequenceString.isEmpty())
                {
                    filtered.addAll(courseList);
                }
                else
                {
                    for (Course course : courseList)
                    {
                        if (course.getNama().toLowerCase().contains(charSequenceString.toLowerCase()))
                        {
                            filtered.add(course);
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
                filteredCourseList.clear();
                filteredCourseList.addAll((List<Course>) filterResults.values);

                notifyDataSetChanged();
            }
        };
    }

    private void addFavorite(viewHolder holder)
    {
        int position = holder.getAdapterPosition();
        course = filteredCourseList.get(position); // TODO: 28/11/2021 BUAT SEPERTI INI DIPISAH PISAH GITU

        long id = course.getId();
        String nama = course.getNama();
        String kode = course.getKode();
        String jurusan = course.getJurusan();
        String deskripsi = course.getDeskripsi();
        String urlfoto = course.getUrlfoto();

        FavoriteModel favorite = new FavoriteModel();
        favorite.setId(id);
        favorite.setNama(nama);
        favorite.setKode(kode);
        favorite.setJurusan(jurusan);
        favorite.setDeskripsi(deskripsi);
        favorite.setUrlfoto(urlfoto);
        favorite.setFavorite(true);

        databaseReference.child(String.valueOf(id))
        .setValue(favorite).addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if(!task.isSuccessful())
                {
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
