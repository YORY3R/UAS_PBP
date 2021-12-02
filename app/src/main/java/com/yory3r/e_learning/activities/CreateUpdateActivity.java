package com.yory3r.e_learning.activities;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;
import static com.android.volley.Request.Method.PUT;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.yory3r.e_learning.R;
import com.yory3r.e_learning.adapters.CourseAdminAdapter;
import com.yory3r.e_learning.api.CourseApi;
import com.yory3r.e_learning.api.QuizApi;
import com.yory3r.e_learning.databinding.ActivityCreateUpdateBinding;
import com.yory3r.e_learning.models.course.Course;
import com.yory3r.e_learning.models.course.CourseResponse;
import com.yory3r.e_learning.models.quiz.Quiz;
import com.yory3r.e_learning.utils.ResizeBitmap;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateUpdateActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener
{
    private static final int PERMISSION_REQUEST_CAMERA = 100;
    private static final int CAMERA_REQUEST = 0;
    private static final int GALLERY_PICTURE = 1;
    
    private TextView tvJudul;
    private ImageView ivFoto;
    private TextInputEditText etNama;
    private TextInputEditText etKode;
    private TextInputEditText etJurusan;
    private TextInputEditText etDeskripsi;
    private TextInputEditText etPertanyaan;
    private TextInputEditText etJawaban;
    private TextInputEditText etUrlFoto;
    private TextInputLayout viewJurusan;
    private TextInputLayout viewDeskripsi;
    private TextInputLayout viewPertanyaan;
    private TextInputLayout viewJawaban;
    private Button btnCancel;
    private Button btnSave;

    private LinearLayout layoutLoading;
    private Bitmap bitmap = null;
    private RequestQueue queue;
    
    
    private long id;
    private String page;

    private ActivityCreateUpdateBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        int layout = R.layout.activity_create_update;
        binding = DataBindingUtil.setContentView(CreateUpdateActivity.this,layout);
        binding.setActivityCreateUpdate(CreateUpdateActivity.this);

        queue = Volley.newRequestQueue(CreateUpdateActivity.this);

        initView();
        initListener();
        initPage();
    }

    private void initView()
    {
        tvJudul = binding.tvJudul;
        ivFoto = binding.ivFoto;
        etNama = binding.etNama;
        etKode = binding.etKode;
        etJurusan = binding.etJurusan;
        etDeskripsi = binding.etDeskripsi;
        etPertanyaan = binding.etPertanyaan;
        etJawaban = binding.etJawaban;
        etUrlFoto = binding.etUrlFoto;
        viewJurusan = binding.viewJurusan;
        viewDeskripsi = binding.viewDeskripsi;
        viewPertanyaan = binding.viewPertanyaan;
        viewJawaban = binding.viewJawaban;
        btnCancel = binding.btnCancel;
        btnSave = binding.btnSave;
        layoutLoading = binding.layoutLoading.getRoot();
    }

    private void initListener()
    {
        etUrlFoto.addTextChangedListener(this);
        btnCancel.setOnClickListener(CreateUpdateActivity.this);
        btnSave.setOnClickListener(CreateUpdateActivity.this);
    }

    private void initPage()
    {
        id = getIntent().getLongExtra("id", -1);
        page = getIntent().getStringExtra("page");
        String nama = getIntent().getStringExtra("nama");
        String kode = getIntent().getStringExtra("kode");
        String jurusan = getIntent().getStringExtra("jurusan");
        String deskripsi = getIntent().getStringExtra("deskripsi");
        String pertanyaan = getIntent().getStringExtra("pertanyaan");
        String jawaban = getIntent().getStringExtra("jawaban");
        String urlfoto = getIntent().getStringExtra("urlfoto");

        if(page.equals("Course"))
        {
            viewJurusan.setVisibility(View.VISIBLE);
            viewDeskripsi.setVisibility(View.VISIBLE);
            viewPertanyaan.setVisibility(View.GONE);
            viewJawaban.setVisibility(View.GONE);
        }
        else
        {
            viewJurusan.setVisibility(View.GONE);
            viewDeskripsi.setVisibility(View.GONE);
            viewPertanyaan.setVisibility(View.VISIBLE);
            viewJawaban.setVisibility(View.VISIBLE);
        }

        if (id == -1)
        {
            tvJudul.setText("Tambah " + page);
        }
        else
        {
            tvJudul.setText("Edit " + page);

            etNama.setText(nama);
            etKode.setText(kode);
            etJurusan.setText(jurusan);
            etDeskripsi.setText(deskripsi);
            etPertanyaan.setText(pertanyaan);
            etJawaban.setText(jawaban);
            etUrlFoto.setText(urlfoto);
        }
    }

    private int checkPage(String page)
    {
        if(page.equals("Course"))
        {
            return 0;
        }
        else if(page.equals("Quiz"))
        {
            return 1;
        }
        else
        {
            return -1;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void afterTextChanged(Editable editable)
    {
        Glide.with(CreateUpdateActivity.this).load(editable.toString()).placeholder(R.drawable.notfound).into(ivFoto);
    }

    @Override
    public void onClick(View view) 
    {
        if(view.getId() == btnCancel.getId())
        {
            finish();
        }
        else if(view.getId() == btnSave.getId())
        {
            boolean inputNama = isEmpty(etNama,"Nama");
            boolean inputKode = isEmpty(etKode,"Kode");
            boolean inputUrl = isEmpty(etUrlFoto,"Url");
            boolean urlValidation = urlValidation(etUrlFoto);

            if(checkPage(page) == 0)//Course
            {
                boolean inputJurusan = isEmpty(etJurusan,"Jurusan");
                boolean inputDeskripsi = isEmpty(etDeskripsi,"Deskripsi");

                if(inputNama && inputKode && inputJurusan && inputDeskripsi && inputUrl && urlValidation)
                {
                    String nama = etNama.getText().toString();
                    String kode = etKode.getText().toString();
                    String jurusan = etJurusan.getText().toString();
                    String Deskripsi = etDeskripsi.getText().toString();
                    String url = etUrlFoto.getText().toString();

                    if (id == -1)
                    {
                        createCourse();
                    }
                    else
                    {
                        updateCourse(id);
                    }
                }
            }
            else if(checkPage(page) == 1)//Quiz
            {
                boolean inputPertanyaan = isEmpty(etPertanyaan,"Pertanyaan");
                boolean inputJawaban = isEmpty(etJawaban,"Jawaban");

                if(inputNama && inputKode && inputPertanyaan && inputJawaban && inputUrl && urlValidation)
                {
                    String nama = etNama.getText().toString();
                    String kode = etKode.getText().toString();
                    String jurusan = etJurusan.getText().toString();
                    String Deskripsi = etDeskripsi.getText().toString();
                    String url = etUrlFoto.getText().toString();

                    if (id == -1)
                    {
                        createQuiz();
                    }
                    else
                    {
                        updateQuiz(id);
                    }
                }
            }
        }
    }

    private boolean isEmpty(TextInputEditText editText, String input)
    {
        if(editText.getText().toString().isEmpty())
        {
            editText.setError(input + " Kosong !");
        }
        else
        {
            return true;
        }

        return false;
    }

    private boolean urlValidation(TextInputEditText url)
    {
        String strUrl = url.getText().toString();
        if(strUrl.contains("http://") || strUrl.contains("https://"))
        {
            return true;
        }
        else
        {
            url.setError("Url Tidak Valid !");
            return false;
        }
    }

    private void createCourse()
    {
        setLoading(true);

        Course course = new Course(
            etNama.getText().toString(),
            etKode.getText().toString(),
            etJurusan.getText().toString(),
            etDeskripsi.getText().toString(),
            etUrlFoto.getText().toString());

        StringRequest stringRequest = new StringRequest(POST, CourseApi.CREATE, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Gson gson = new Gson();
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                finish();

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

                    Toast.makeText(CreateUpdateActivity.this, errors.getString("message"), Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {
                    Toast.makeText(CreateUpdateActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

            @Override
            public byte[] getBody() throws AuthFailureError
            {
                Gson gson = new Gson();
                String requestBody = gson.toJson(course);

                return requestBody.getBytes(StandardCharsets.UTF_8);
            }

            @Override
            public String getBodyContentType()
            {
                return "application/json";
            }
        };

        queue.add(stringRequest);
    }

    private void createQuiz()
    {
        setLoading(true);

        Quiz quiz = new Quiz(
            etNama.getText().toString(),
            etKode.getText().toString(),
            etPertanyaan.getText().toString(),
            etJawaban.getText().toString(),
            etUrlFoto.getText().toString());

        StringRequest stringRequest = new StringRequest(POST, QuizApi.CREATE, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Gson gson = new Gson();
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                finish();

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

                    Toast.makeText(CreateUpdateActivity.this, errors.getString("message"), Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {
                    Toast.makeText(CreateUpdateActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

            @Override
            public byte[] getBody() throws AuthFailureError
            {
                Gson gson = new Gson();
                String requestBody = gson.toJson(quiz);

                return requestBody.getBytes(StandardCharsets.UTF_8);
            }

            @Override
            public String getBodyContentType()
            {
                return "application/json";
            }
        };

        queue.add(stringRequest);
    }

    private void updateCourse(long id)
    {
        setLoading(true);

        Course course = new Course(
            etNama.getText().toString(),
            etKode.getText().toString(),
            etJurusan.getText().toString(),
            etDeskripsi.getText().toString(),
            etUrlFoto.getText().toString());

        StringRequest stringRequest = new StringRequest(PUT, CourseApi.UPDATE + id, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                finish();

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

                    Toast.makeText(CreateUpdateActivity.this, errors.getString("message"), Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {
                    Toast.makeText(CreateUpdateActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

            @Override
            public byte[] getBody() throws AuthFailureError
            {
                Gson gson = new Gson();
                String requestBody = gson.toJson(course);

                return requestBody.getBytes(StandardCharsets.UTF_8);
            }

            @Override
            public String getBodyContentType()
            {
                return "application/json";
            }
        };

        queue.add(stringRequest);
    }

    private void updateQuiz(long id)
    {
        setLoading(true);

        Quiz quiz = new Quiz(
            etNama.getText().toString(),
            etKode.getText().toString(),
            etPertanyaan.getText().toString(),
            etJawaban.getText().toString(),
            etUrlFoto.getText().toString());

        StringRequest stringRequest = new StringRequest(PUT, QuizApi.UPDATE + id, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                finish();

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

                    Toast.makeText(CreateUpdateActivity.this, errors.getString("message"), Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {
                    Toast.makeText(CreateUpdateActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

            @Override
            public byte[] getBody() throws AuthFailureError
            {
                Gson gson = new Gson();
                String requestBody = gson.toJson(quiz);

                return requestBody.getBytes(StandardCharsets.UTF_8);
            }

            @Override
            public String getBodyContentType()
            {
                return "application/json";
            }
        };

        queue.add(stringRequest);
    }

    private void setLoading(boolean isLoading)
    {
        if (isLoading)
        {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            layoutLoading.setVisibility(View.VISIBLE);
        }
        else
        {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            layoutLoading.setVisibility(View.INVISIBLE);
        }
    }
}