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
import com.yory3r.e_learning.R;
import com.yory3r.e_learning.activities.AdminActivity;
import com.yory3r.e_learning.activities.CreateUpdateActivity;
import com.yory3r.e_learning.activities.MainActivity;
import com.yory3r.e_learning.databinding.ItemCourseAdminBinding;
import com.yory3r.e_learning.databinding.ItemCourseBinding;
import com.yory3r.e_learning.databinding.LayoutDeskripsiBinding;
import com.yory3r.e_learning.models.course.Course;

import java.util.ArrayList;
import java.util.List;

public class CourseAdminAdapter extends RecyclerView.Adapter<CourseAdminAdapter.viewHolder>
{
    private List<Course> courseList;
    private List<Course> filteredCourseList;
    private Context context;
    private Course course;
    private Intent intent;
    private ItemCourseAdminBinding binding;

    public CourseAdminAdapter(List<Course> courseList, Context context)
    {
        this.courseList = courseList;
        this.context = context;

        filteredCourseList = new ArrayList<>(courseList);
    }

    public class viewHolder extends RecyclerView.ViewHolder
    {
        private ImageView ivFoto;
        private TextView tvNama;
        private TextView tvKode;
        private Button btnDeskripsi;
        private Button btnDelete;
        private CardView layoutCourseAdmin;

        public viewHolder(@NonNull ItemCourseAdminBinding binding)
        {
            super(binding.getRoot());

            ivFoto = binding.ivFoto;
            tvNama = binding.tvNama;
            tvKode = binding.tvKode;
            btnDeskripsi = binding.btnDeskripsi;
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

        holder.btnDeskripsi.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                LayoutInflater inflater = LayoutInflater.from(builder.getContext());

                int layout = R.layout.layout_deskripsi;
                LayoutDeskripsiBinding binding = DataBindingUtil.inflate(inflater,layout,null,false);
                binding.setLayoutDeskripsiAdmin(CourseAdminAdapter.this);

                View layoutDeskripsi = binding.getRoot();
                TextView tvDeskripsi =  binding.tvDeskripsi;

                course = filteredCourseList.get(holder.getAdapterPosition());
                tvDeskripsi.setText(course.getDeskripsi());


                builder.setTitle("Deskripsi");
                builder.setView(layoutDeskripsi);
                builder.setPositiveButton("OK",null);
                builder.show();
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
                course = filteredCourseList.get(holder.getAdapterPosition());
                intent = new Intent(context, CreateUpdateActivity.class);

                intent.putExtra("id", course.getId());
                intent.putExtra("nama", course.getNama());
                intent.putExtra("deskripsi",course.getDeskripsi());
                intent.putExtra("kode",course.getKode());

                if (context instanceof AdminActivity)
                {
                    ((AdminActivity) context).startActivity(intent);
                }
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
