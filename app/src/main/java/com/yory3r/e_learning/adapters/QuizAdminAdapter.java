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

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yory3r.e_learning.R;
import com.yory3r.e_learning.activities.AdminActivity;
import com.yory3r.e_learning.activities.CreateUpdateActivity;
import com.yory3r.e_learning.databinding.ItemQuizAdminBinding;
import com.yory3r.e_learning.models.quiz.Quiz;
import com.yory3r.e_learning.utils.ChangeString;

import java.util.ArrayList;
import java.util.List;

public class QuizAdminAdapter extends RecyclerView.Adapter<QuizAdminAdapter.viewHolder>
{
    private List<Quiz> quizList;
    private List<Quiz> filteredQuizList;
    private Context context;
    private Quiz quiz;
    private Intent intent;

    private ChangeString change;

    private FirebaseAuth auth;
    private FirebaseUser user;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private ItemQuizAdminBinding binding;

    public QuizAdminAdapter(List<Quiz> quizList, Context context)
    {
        this.quizList = quizList;
        this.context = context;

        filteredQuizList = new ArrayList<>(quizList);

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
        private PorterShapeImageView ivFoto;
        private TextView tvNama;
        private TextView tvKode;
        private TextView tvPertanyaan;
        private TextView tvJawaban;
        private Button btnEdit;
        private Button btnDelete;
        private CardView layoutQuizAdmin;

        public viewHolder(@NonNull ItemQuizAdminBinding binding)
        {
            super(binding.getRoot());

            ivFoto = binding.ivFoto;
            tvNama = binding.tvNama;
            tvKode = binding.tvKode;
            tvPertanyaan = binding.tvPertanyaan;
            tvJawaban = binding.tvJawaban;
            btnEdit = binding.btnEdit;
            btnDelete = binding.btnDelete;
            layoutQuizAdmin = binding.layoutQuizAdmin;
        }
    }

    @NonNull
    @Override
    public QuizAdminAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        int layout = R.layout.item_quiz_admin;

        binding = DataBindingUtil.inflate(inflater,layout,parent,false);
        return new QuizAdminAdapter.viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizAdminAdapter.viewHolder holder, int position)
    {
        quiz = filteredQuizList.get(position);

        Glide.with(context).load(quiz.getUrlfoto()).into(holder.ivFoto);

        holder.tvNama.setText(quiz.getNama());
        holder.tvKode.setText(quiz.getKode());
        holder.tvPertanyaan.setText(quiz.getPertanyaan());
        holder.tvJawaban.setText(quiz.getJawaban());

        holder.btnEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                gotoCreateUpdateActivity(holder);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);

                builder.setTitle("Konfirmasi");
                builder.setMessage("Apakah anda yakin ingin menghapus data Quiz ini?");
                builder.setNegativeButton("Batal", null);

                builder.setPositiveButton("Hapus", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        quiz = filteredQuizList.get(holder.getAdapterPosition());

                        if (context instanceof AdminActivity)
                        {
                            ((AdminActivity) context).deleteQuiz(quiz.getId());
                            databaseReference.child(String.valueOf(quiz.getId())).removeValue();
                        }
                    }
                });

                builder.show();
            }
        });

        holder.layoutQuizAdmin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                gotoCreateUpdateActivity(holder);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return filteredQuizList.size();
    }

    public void setQuizList(List<Quiz> quizList)
    {
        this.quizList = quizList;
        filteredQuizList = new ArrayList<>(quizList);
    }

    public Filter getFilter()
    {
        return new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence)
            {
                String charSequenceString = charSequence.toString();
                List<Quiz> filtered = new ArrayList<>();

                if (charSequenceString.isEmpty())
                {
                    filtered.addAll(quizList);
                }
                else
                {
                    for (Quiz quiz : quizList)
                    {
                        if (quiz.getNama().toLowerCase().contains(charSequenceString.toLowerCase()))
                        {
                            filtered.add(quiz);
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
                filteredQuizList.clear();
                filteredQuizList.addAll((List<Quiz>) filterResults.values);

                notifyDataSetChanged();
            }
        };
    }

    private void gotoCreateUpdateActivity(QuizAdminAdapter.viewHolder holder)
    {
        quiz = filteredQuizList.get(holder.getAdapterPosition());
        intent = new Intent(context, CreateUpdateActivity.class);

        intent.putExtra("page","Quiz");
        intent.putExtra("id", quiz.getId());
        intent.putExtra("nama", quiz.getNama());
        intent.putExtra("kode",quiz.getKode());
        intent.putExtra("pertanyaan",quiz.getPertanyaan());
        intent.putExtra("jawaban",quiz.getJawaban());
        intent.putExtra("urlfoto",quiz.getUrlfoto());

        if (context instanceof AdminActivity)
        {
            ((AdminActivity) context).startActivity(intent);
        }
    }
}
