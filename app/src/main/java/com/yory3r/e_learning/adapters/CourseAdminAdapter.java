package com.yory3r.e_learning.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yory3r.e_learning.R;
import com.yory3r.e_learning.activities.AdminActivity;
import com.yory3r.e_learning.activities.CreateUpdateActivity;
import com.yory3r.e_learning.activities.MainActivity;
import com.yory3r.e_learning.databinding.ItemCourseAdminBinding;
import com.yory3r.e_learning.databinding.ItemCourseBinding;
import com.yory3r.e_learning.databinding.LayoutDeskripsiBinding;
import com.yory3r.e_learning.models.course.Course;
import com.yory3r.e_learning.utils.ChangeString;

import java.util.ArrayList;
import java.util.List;

public class CourseAdminAdapter extends RecyclerView.Adapter<CourseAdminAdapter.viewHolder>
{
    private List<Course> courseList;
    private List<Course> filteredCourseList;
    private Context context;
    private Course course;
    private Intent intent;

    private ChangeString change;

    private FirebaseAuth auth;
    private FirebaseUser user;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private ItemCourseAdminBinding binding;

    public CourseAdminAdapter(List<Course> courseList, Context context)
    {
        this.courseList = courseList;
        this.context = context;

        filteredCourseList = new ArrayList<>(courseList);

        initFirebase();
    }

    private void initFirebase()
    {
        change = new ChangeString();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Favorite/" + change.DotsToEtc(user.getEmail()));
    }

    public class viewHolder extends RecyclerView.ViewHolder
    {
        private ImageView ivFoto;
        private TextView tvNama;
        private TextView tvKode;
        private TextView tvJurusan;
        private TextView tvDeskripsi;
        private Button btnEdit;
        private Button btnDelete;
        private CardView layoutCourseAdmin;

        public viewHolder(@NonNull ItemCourseAdminBinding binding)
        {
            super(binding.getRoot());

            ivFoto = binding.ivFoto;
            tvNama = binding.tvNama;
            tvKode = binding.tvKode;
            tvJurusan = binding.tvJurusan;
            tvDeskripsi = binding.tvDeskripsi;
            btnEdit = binding.btnEdit;
            btnDelete = binding.btnDelete;
            layoutCourseAdmin = binding.layoutCourseAdmin;
        }
    }

    @NonNull
    @Override
    public CourseAdminAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        int layout = R.layout.item_course_admin;

        binding = DataBindingUtil.inflate(inflater,layout,parent,false);
        return new CourseAdminAdapter.viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdminAdapter.viewHolder holder, int position)
    {
        course = filteredCourseList.get(position);

        holder.tvNama.setText(course.getNama());
        holder.tvKode.setText(course.getKode());
        holder.tvJurusan.setText(course.getJurusan());
        holder.tvDeskripsi.setText(course.getDeskripsi());

        holder.btnEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                gotoCreateEditActivity(holder);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);

                builder.setTitle("Konfirmasi");
                builder.setMessage("Apakah anda yakin ingin menghapus data Course ini?");
                builder.setNegativeButton("Batal", null);

                builder.setPositiveButton("Hapus", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        course = filteredCourseList.get(holder.getAdapterPosition());

                        if (context instanceof AdminActivity)
                        {
                            ((AdminActivity) context).deleteCourse(course.getId());
                            databaseReference.child(String.valueOf(course.getId())).removeValue();
                        }
                    }
                });

                builder.show();
            }
        });

        holder.layoutCourseAdmin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                gotoCreateEditActivity(holder);
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

    private void gotoCreateEditActivity(CourseAdminAdapter.viewHolder holder)
    {
        course = filteredCourseList.get(holder.getAdapterPosition());
        intent = new Intent(context, CreateUpdateActivity.class);

        intent.putExtra("id", course.getId());
        intent.putExtra("nama", course.getNama());
        intent.putExtra("deskripsi",course.getDeskripsi());
        intent.putExtra("kode",course.getKode());
        intent.putExtra("jurusan",course.getJurusan());

        if (context instanceof AdminActivity)
        {
            ((AdminActivity) context).startActivity(intent);
        }
    }
}
