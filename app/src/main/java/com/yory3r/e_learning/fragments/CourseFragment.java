package com.yory3r.e_learning.fragments;

import static com.android.volley.Request.Method.DELETE;
import static com.android.volley.Request.Method.GET;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.yory3r.e_learning.R;
import com.yory3r.e_learning.activities.CreateUpdateActivity;
import com.yory3r.e_learning.activities.MainActivity;
import com.yory3r.e_learning.adapters.CourseAdapter;
import com.yory3r.e_learning.api.CourseApi;
import com.yory3r.e_learning.databinding.FragmentCourseBinding;
import com.yory3r.e_learning.databinding.LayoutLoadingBinding;
import com.yory3r.e_learning.models.course.CourseResponse;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CourseFragment extends Fragment implements SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener
{

    private CourseAdapter adapter;
    private LayoutManager manager;
    private RecyclerView rvCourse;
    private SearchView svCourse;
    private SwipeRefreshLayout srCourse;
    private RequestQueue queue;
    
    private FragmentCourseBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
    {
        binding = FragmentCourseBinding.inflate(inflater, container, false);
        
        
        queue = Volley.newRequestQueue(getContext());
        
        initView();
        initListener();
        initAdapter();

        getAllCourse();
        
        
        
        
        
        
        
        
        return binding.getRoot();
    }

    private void initView()
    {
        rvCourse = binding.rvCourse;
        svCourse = binding.svCourse;
        srCourse = binding.srCourse;
    }

    private void initListener()
    {
        svCourse.setOnQueryTextListener(this);
        srCourse.setOnRefreshListener(this);
    }

    private void initAdapter()
    {
        adapter = new CourseAdapter(new ArrayList<>(), getContext());
        manager = new LinearLayoutManager(getContext());

        rvCourse.setLayoutManager(manager);
        rvCourse.setAdapter(adapter);
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
        restartActivity();
    }

    private void getAllCourse()
    {
        srCourse.setRefreshing(true);

        StringRequest stringRequest = new StringRequest(GET, CourseApi.READ, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Gson gson = new Gson();
                CourseResponse courseResponse = gson.fromJson(response, CourseResponse.class);

                adapter.setCourseList(courseResponse.getCourseList());
                adapter.getFilter().filter(svCourse.getQuery());









                srCourse.setRefreshing(false);
            }
        },new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                srCourse.setRefreshing(false);

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

    private void restartActivity()
    {
        getActivity().startActivity(new Intent(getContext(), MainActivity.class));
        getActivity().finish();
    }
}