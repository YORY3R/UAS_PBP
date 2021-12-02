package com.yory3r.e_learning.fragments;

import static com.android.volley.Request.Method.GET;

import android.content.Intent;
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
import com.yory3r.e_learning.activities.AdminActivity;
import com.yory3r.e_learning.activities.CreateUpdateActivity;
import com.yory3r.e_learning.adapters.CourseAdminAdapter;
import com.yory3r.e_learning.api.CourseApi;
import com.yory3r.e_learning.databinding.FragmentCourseAdminBinding;
import com.yory3r.e_learning.databinding.FragmentCourseBinding;
import com.yory3r.e_learning.models.course.CourseResponse;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CourseAdminFragment extends Fragment implements SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener
{
    private String title;
    private int page;
    private CourseAdminAdapter adapter;
    private RecyclerView.LayoutManager manager;
    private RecyclerView rvCourseAdmin;
    private SearchView svCourseAdmin;
    private SwipeRefreshLayout srCourseAdmin;
    private RequestQueue queue;
    private FragmentCourseAdminBinding binding;

    public static CourseAdminFragment newInstance(int page, String title)
    {
        CourseAdminFragment fragment = new CourseAdminFragment();
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
        int layout = R.layout.fragment_course_admin;
        binding = DataBindingUtil.inflate(inflater, layout,container, false);

        queue = Volley.newRequestQueue(getContext());

        initView();
        initListener();
        initAdapter();

        getAllCourse();
        
        return binding.getRoot();
    }

    private void initView()
    {
        rvCourseAdmin = binding.rvCourseAdmin;
        svCourseAdmin = binding.svCourseAdmin;
        srCourseAdmin = binding.srCourseAdmin;
    }

    private void initListener()
    {
        svCourseAdmin.setOnQueryTextListener(CourseAdminFragment.this);
        srCourseAdmin.setOnRefreshListener(CourseAdminFragment.this);
    }

    private void initAdapter()
    {
        adapter = new CourseAdminAdapter(new ArrayList<>(), getContext());
        manager = new LinearLayoutManager(getContext());

        rvCourseAdmin.setLayoutManager(manager);
        rvCourseAdmin.setAdapter(adapter);


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
        getAllCourse();
    }

    private void getAllCourse()
    {
        srCourseAdmin.setRefreshing(true);

        StringRequest stringRequest = new StringRequest(GET, CourseApi.READ, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Gson gson = new Gson();
                CourseResponse courseResponse = gson.fromJson(response, CourseResponse.class);

                adapter.setCourseList(courseResponse.getCourseList());
                adapter.getFilter().filter(svCourseAdmin.getQuery());

                srCourseAdmin.setRefreshing(false);
            }
        },new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                srCourseAdmin.setRefreshing(false);

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