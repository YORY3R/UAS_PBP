package com.yory3r.e_learning.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
        private Button btnTambah;
        private Button btnDeskripsi;

        public viewHolder(@NonNull ItemCourseBinding binding)
        {
            super(binding.getRoot());

            ivFoto = binding.ivFoto;
            tvNama = binding.tvNama;
            tvKode = binding.tvKode;
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

        holder.tvNama.setText(course.getNama());
        holder.tvKode.setText(course.getKode());

        holder.btnTambah.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int position = holder.getAdapterPosition();
                course = filteredCourseList.get(position); // TODO: 28/11/2021 BUAT SEPERTI INI DIPISAH PISAH GITU
                
                long id = course.getId();
                String nama = course.getNama();
                String deskripsi = course.getDeskripsi();
                String kode = course.getKode();

                FavoriteModel favorite = new FavoriteModel();
                favorite.setId(id);
                favorite.setNama(nama);
                favorite.setDeskripsi(deskripsi);
                favorite.setKode(kode);

                databaseReference.child(String.valueOf(id))
                .addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        if(snapshot.getValue() == null)
                        {

                            databaseReference.child(String.valueOf(id))
                            .setValue(favorite).addOnCompleteListener(new OnCompleteListener<Void>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<Void> task)
                                {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(context, "Berhasil Tambah Favorite", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(context, "Sudah Pernah Tambah Course " + nama + " Ke Favorite", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {

                    }
                });




                // TODO: 28/11/2021 GANTI SEMUAMNYA YANG PAKE TITIK BANYAK JADIIIN SATU SATU
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
}
