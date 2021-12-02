package com.yory3r.e_learning.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
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
import com.yory3r.e_learning.databinding.ItemQuizBinding;
import com.yory3r.e_learning.databinding.LayoutDeskripsiBinding;
import com.yory3r.e_learning.models.FavoriteModel;
import com.yory3r.e_learning.models.quiz.Quiz;
import com.yory3r.e_learning.utils.ChangeString;

import java.util.ArrayList;
import java.util.List;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.viewHolder>
{
    private List<Quiz> quizList;
    private List<Quiz> filteredQuizList;
    private Context context;
    private Quiz quiz;
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
    private ItemQuizBinding binding;

    public QuizAdapter(List<Quiz> quizList, Context context)
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
        private CardView cvWarna;
        private ImageView ivFoto;
        private TextView tvNama;
        private TextView tvKode;
        private TextView tvPertanyaan;
        private EditText etJawaban;
        private Button btnJawab;

        public viewHolder(@NonNull ItemQuizBinding binding)
        {
            super(binding.getRoot());

            cvWarna = binding.cvWarna;
            ivFoto = binding.ivFoto;
            tvNama = binding.tvNama;
            tvKode = binding.tvKode;
            tvPertanyaan = binding.tvPertanyaan;
            etJawaban = binding.etJawaban;
            btnJawab = binding.btnJawab;
        }
    }

    @NonNull
    @Override
    public QuizAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(context);
        int layout = R.layout.item_quiz;

        binding = DataBindingUtil.inflate(inflater,layout,parent,false);
        return new QuizAdapter.viewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizAdapter.viewHolder holder, int position)
    {
        quiz = filteredQuizList.get(position);

        Glide.with(context).load(quiz.getUrlfoto()).into(holder.ivFoto);

        holder.tvNama.setText(quiz.getNama());
        holder.tvKode.setText(quiz.getKode());
        holder.tvPertanyaan.setText(quiz.getPertanyaan());

        holder.btnJawab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int warna;
                int position = holder.getAdapterPosition();
                String jawaban = holder.etJawaban.getText().toString();

                quiz = filteredQuizList.get(position);

                if(jawaban.toLowerCase().equals(quiz.getJawaban().toLowerCase()))
                {
                    warna = 0xFF00C853;
                    jawaban = "Benar";
                }
                else
                {
                    warna = 0xFFD50000;
                    jawaban = "Salah";
                }

                holder.cvWarna.setCardBackgroundColor(warna);
                Toast.makeText(context, "Jawaban " + jawaban + " !", Toast.LENGTH_SHORT).show();
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
}
