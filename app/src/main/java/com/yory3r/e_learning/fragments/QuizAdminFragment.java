package com.yory3r.e_learning.fragments;

import static com.android.volley.Request.Method.GET;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.yory3r.e_learning.R;
import com.yory3r.e_learning.adapters.QuizAdminAdapter;
import com.yory3r.e_learning.api.QuizApi;
import com.yory3r.e_learning.databinding.FragmentQuizAdminBinding;
import com.yory3r.e_learning.models.quiz.QuizResponse;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QuizAdminFragment extends Fragment implements SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener
{
    private String title;
    private int page;
    private QuizAdminAdapter adapter;
    private RecyclerView.LayoutManager manager;
    private RecyclerView rvQuizAdmin;
    private SearchView svQuizAdmin;
    private SwipeRefreshLayout srQuizAdmin;
    private RequestQueue queue;
    private FragmentQuizAdminBinding binding;

    public static QuizAdminFragment newInstance(int page, String title)
    {
        QuizAdminFragment fragment = new QuizAdminFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("Page",page);
        bundle.putString("Title",title);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("Page",0);
        title = getArguments().getString("Title");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        int layout = R.layout.fragment_quiz_admin;
        binding = DataBindingUtil.inflate(inflater, layout,container, false);

        queue = Volley.newRequestQueue(getContext());

        initView();
        initListener();
        initAdapter();

        getAllQuiz();

        return binding.getRoot();
    }

    private void initView()
    {
        rvQuizAdmin = binding.rvQuizAdmin;
        svQuizAdmin = binding.svQuizAdmin;
        srQuizAdmin = binding.srQuizAdmin;
    }

    private void initListener()
    {
        svQuizAdmin.setOnQueryTextListener(QuizAdminFragment.this);
        srQuizAdmin.setOnRefreshListener(QuizAdminFragment.this);
    }

    private void initAdapter()
    {
        adapter = new QuizAdminAdapter(new ArrayList<>(), getContext());
        manager = new LinearLayoutManager(getContext());

        rvQuizAdmin.setLayoutManager(manager);
        rvQuizAdmin.setAdapter(adapter);


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
        srQuizAdmin.setRefreshing(true);

        StringRequest stringRequest = new StringRequest(GET, QuizApi.READ, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Gson gson = new Gson();
                QuizResponse quizResponse = gson.fromJson(response, QuizResponse.class);

                adapter.setQuizList(quizResponse.getQuizList());
                adapter.getFilter().filter(svQuizAdmin.getQuery());

                srQuizAdmin.setRefreshing(false);
            }
        },new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                srQuizAdmin.setRefreshing(false);

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
}