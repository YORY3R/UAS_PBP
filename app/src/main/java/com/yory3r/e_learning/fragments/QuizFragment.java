package com.yory3r.e_learning.fragments;

import static com.android.volley.Request.Method.GET;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.yory3r.e_learning.R;
import com.yory3r.e_learning.activities.MainActivity;
import com.yory3r.e_learning.adapters.QuizAdapter;
import com.yory3r.e_learning.api.QuizApi;
import com.yory3r.e_learning.databinding.FragmentQuizBinding;
import com.yory3r.e_learning.models.quiz.QuizResponse;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QuizFragment extends Fragment implements SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener
{

    private QuizAdapter adapter;
    private RecyclerView.LayoutManager manager;
    private RecyclerView rvQuiz;
    private SearchView svQuiz;
    private SwipeRefreshLayout srQuiz;
    private RequestQueue queue;

    private FragmentQuizBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = FragmentQuizBinding.inflate(inflater, container, false);


        queue = Volley.newRequestQueue(getContext());

        initView();
        initListener();
        initAdapter();

        getAllQuiz();








        return binding.getRoot();
    }

    private void initView()
    {
        rvQuiz = binding.rvQuiz;
        svQuiz = binding.svQuiz;
        srQuiz = binding.srQuiz;
    }

    private void initListener()
    {
        svQuiz.setOnQueryTextListener(this);
        srQuiz.setOnRefreshListener(this);
    }

    private void initAdapter()
    {
        adapter = new QuizAdapter(new ArrayList<>(), getContext());
        manager = new LinearLayoutManager(getContext());

        rvQuiz.setLayoutManager(manager);
        rvQuiz.setAdapter(adapter);
    }

    @Override
    public boolean onQueryTextSubmit(String s)
    {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s)
    {
        adapter.getFilter().filter(s);
        return false;
    }

    @Override
    public void onRefresh()
    {
        getAllQuiz();
    }

    private void getAllQuiz()
    {
        srQuiz.setRefreshing(true);

        StringRequest stringRequest = new StringRequest(GET, QuizApi.READ, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Gson gson = new Gson();
                QuizResponse quizResponse = gson.fromJson(response, QuizResponse.class);

                adapter.setQuizList(quizResponse.getQuizList());
                adapter.getFilter().filter(svQuiz.getQuery());









                srQuiz.setRefreshing(false);
            }
        },new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                srQuiz.setRefreshing(false);

                try
                {
                    String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                    JSONObject errors = new JSONObject(responseBody);

                    Toast.makeText(getContext(), errors.getString("message"), Toast.LENGTH_SHORT).show();
                }
                catch(Exception e)
                {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");

                return headers;
            }
        };

        queue.add(stringRequest);
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        binding = null;
    }
}