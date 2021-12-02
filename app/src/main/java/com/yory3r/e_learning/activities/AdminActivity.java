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
import androidx.viewpager.widget.PagerTabStrip;
import androidx.viewpager.widget.ViewPager;

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
import com.yory3r.e_learning.adapters.AdminAdapter;
import com.yory3r.e_learning.adapters.CourseAdapter;
import com.yory3r.e_learning.adapters.CourseAdminAdapter;
import com.yory3r.e_learning.api.CourseApi;
import com.yory3r.e_learning.api.QuizApi;
import com.yory3r.e_learning.databinding.ActivityAdminBinding;
import com.yory3r.e_learning.fragments.CourseAdminFragment;
import com.yory3r.e_learning.models.course.CourseResponse;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener
{
    private ViewPager viewPager;
    private PagerTabStrip header;
    private FloatingActionButton fabCreate;
    private AdminAdapter adapter;
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
    }

    private void initView()
    {
        viewPager = binding.viewPager;
        header = binding.viewPagerHeader;
        fabCreate = binding.fabCreate;
    }

    private void initListener()
    {
        fabCreate.setOnClickListener(AdminActivity.this);
    }

    private void initAdapter()
    {
        adapter = new AdminAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == fabCreate.getId())
        {
            gotoCreateUpdateActivity();
        }
    }


    public void deleteCourse(long id)
    {
        StringRequest stringRequest = new StringRequest(DELETE, CourseApi.DELETE + id, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Toast.makeText(AdminActivity.this, response, Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
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

    public void deleteQuiz(long id)
    {
        StringRequest stringRequest = new StringRequest(DELETE, QuizApi.DELETE + id, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Toast.makeText(AdminActivity.this, response, Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
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

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        intent = new Intent(AdminActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void gotoCreateUpdateActivity()
    {
        intent = new Intent(AdminActivity.this, CreateUpdateActivity.class);

        if(viewPager.getCurrentItem() == 0)//Course
        {
            intent.putExtra("page","Course");
        }
        else if(viewPager.getCurrentItem() == 1)//Quiz
        {
            intent.putExtra("page","Quiz");
        }

        startActivity(intent);
    }

    // TODO: 28/11/2021 GANTI INTENT JADI GOTO APA ACTIVITY GITU ! 
}