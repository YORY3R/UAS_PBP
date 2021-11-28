package com.yory3r.e_learning.activities;

import static com.android.volley.Request.Method.DELETE;
import static com.android.volley.Request.Method.GET;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
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
import com.yory3r.e_learning.adapters.CourseAdapter;
import com.yory3r.e_learning.adapters.CourseAdminAdapter;
import com.yory3r.e_learning.api.CourseApi;
import com.yory3r.e_learning.databinding.ActivityAdminBinding;
import com.yory3r.e_learning.models.course.CourseResponse;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener
{
    public static final int LAUNCH_ADD_ACTIVITY = 123;

    private CourseAdminAdapter adapter;
    private LayoutManager manager;
    private RecyclerView rvCourseAdmin;
    private SearchView svCourseAdmin;
    private SwipeRefreshLayout srCourseAdmin;
    private FloatingActionButton fabCreate;
    private LinearLayout layoutLoading;
    private RequestQueue queue;
    private Intent intent;
    private ActivityAdminBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        int layout = R.layout.activity_admin;
        binding = DataBindingUtil.setContentView(AdminActivity.this,layout);
        binding.setActivityAdmin(AdminActivity.this);

        queue = Volley.newRequestQueue(AdminActivity.this);

        initView();
        initListener();
        initAdapter();

        getAllCourse();
    }

    private void initView()
    {
        rvCourseAdmin = binding.rvCourseAdmin;
        svCourseAdmin = binding.svCourseAdmin;
        srCourseAdmin = binding.srCourseAdmin;
        fabCreate = binding.fabCreate;
        layoutLoading = binding.layoutLoading.getRoot();
    }

    private void initListener()
    {
        svCourseAdmin.setOnQueryTextListener(AdminActivity.this);
        srCourseAdmin.setOnRefreshListener(AdminActivity.this);
        fabCreate.setOnClickListener(AdminActivity.this);
    }

    private void initAdapter()
    {
        adapter = new CourseAdminAdapter(new ArrayList<>(), AdminActivity.this);
        manager = new LinearLayoutManager(AdminActivity.this);

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

    @Override
    public void onClick(View view)
    {
        intent = new Intent(AdminActivity.this, CreateUpdateActivity.class);
        startActivity(intent);
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

                    Toast.makeText(AdminActivity.this, errors.getString("message"), Toast.LENGTH_SHORT).show();
                }
                catch(Exception e)
                {
                    Toast.makeText(AdminActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void deleteCourse(long id)
    {
        setLoading(true);

        StringRequest stringRequest = new StringRequest(DELETE, CourseApi.DELETE + id, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Toast.makeText(AdminActivity.this, response, Toast.LENGTH_SHORT).show();

                getAllCourse();


                setLoading(false);


            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                setLoading(false);

                try
                {
                    String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                    JSONObject errors = new JSONObject(responseBody);

                    Toast.makeText(AdminActivity.this, errors.getString("message"), Toast.LENGTH_SHORT).show();
                }
                catch(Exception e)
                {
                    Toast.makeText(AdminActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void setLoading(boolean isLoading)
    {
        if(isLoading)
        {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            layoutLoading.setVisibility(View.VISIBLE);
        }
        else
        {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            layoutLoading.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() 
    {
        super.onBackPressed();
        
        intent = new Intent(AdminActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // TODO: 28/11/2021 GANTI INTENT JADI GOTO APA ACTIVITY GITU ! 
}