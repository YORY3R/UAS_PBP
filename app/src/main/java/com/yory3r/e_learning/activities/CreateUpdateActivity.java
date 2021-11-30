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
import com.google.gson.Gson;
import com.yory3r.e_learning.R;
import com.yory3r.e_learning.adapters.CourseAdminAdapter;
import com.yory3r.e_learning.api.CourseApi;
import com.yory3r.e_learning.databinding.ActivityCreateUpdateBinding;
import com.yory3r.e_learning.models.course.Course;
import com.yory3r.e_learning.models.course.CourseResponse;
import com.yory3r.e_learning.utils.ResizeBitmap;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateUpdateActivity extends AppCompatActivity implements View.OnClickListener
{
    private static final int PERMISSION_REQUEST_CAMERA = 100;
    private static final int CAMERA_REQUEST = 0;
    private static final int GALLERY_PICTURE = 1;
    
    private TextView tvJudul;
    private ImageView ivFoto;
    private TextInputEditText etNama;
    private TextInputEditText etKode;
    private TextInputEditText etDeskripsi;
    private TextInputEditText etJurusan;
    private Button btnCancel;
    private Button btnSave;

    private LinearLayout layoutLoading;
    private Bitmap bitmap = null;
    private RequestQueue queue;
    
    
    private long id;
    private Uri firebaseUri;
    private ResizeBitmap resizeBitmap;
    
    
    private Intent intent;
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


        id = getIntent().getLongExtra("id", -1);
        if (id == -1)
        {
            // TODO: 28/11/2021 EXCEPTION HANDLING SEMUA
            tvJudul.setText("Tambah Course");
        }
        else
        {
            tvJudul.setText("Edit Course");
            etNama.setText(getIntent().getStringExtra("nama"));
            etDeskripsi.setText(getIntent().getStringExtra("deskripsi"));
            etKode.setText(getIntent().getStringExtra("kode"));
            etJurusan.setText(getIntent().getStringExtra("jurusan"));
        }
        
    }

    private void initView()
    {
        tvJudul = binding.tvJudul;
        ivFoto = binding.ivFoto;
        etNama = binding.etNama;
        etDeskripsi = binding.etDeskripsi;
        etKode = binding.etKode;
        etJurusan = binding.etJurusan;
        btnCancel = binding.btnCancel;
        btnSave = binding.btnSave;
        layoutLoading = binding.layoutLoading.getRoot();
    }

    private void initListener()
    {
        ivFoto.setOnClickListener(CreateUpdateActivity.this);
        btnCancel.setOnClickListener(CreateUpdateActivity.this);
        btnSave.setOnClickListener(CreateUpdateActivity.this);
    }

    @Override
    public void onClick(View view) 
    {
        if(view.getId() == ivFoto.getId())
        {
            LayoutInflater layoutInflater = LayoutInflater.from(CreateUpdateActivity.this);
            View selectMediaView = layoutInflater.inflate(R.layout.layout_select_media, null);
            final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(selectMediaView.getContext()).create();

            Button btnKamera = selectMediaView.findViewById(R.id.btnKamera);
            Button btnGaleri = selectMediaView.findViewById(R.id.btnGaleri);

            btnKamera.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED)
                    {
                        String[] permission = {Manifest.permission.CAMERA};
                        requestPermissions(permission, PERMISSION_REQUEST_CAMERA);
                    }
                    else
                    {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, CAMERA_REQUEST);
                    }

                    alertDialog.dismiss();
                }
            });

            btnGaleri.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, GALLERY_PICTURE);

                    alertDialog.dismiss();
                }
            });

            alertDialog.setView(selectMediaView);
            alertDialog.show();
        }
        else if(view.getId() == btnCancel.getId())
        {
            finish();
        }
        else if(view.getId() == btnSave.getId())
        {
            if (id == -1)
            {
                // TODO: 28/11/2021 EXCEPTION HANDLING SEMUA
                createCourse();
            }
            else
            {
                updateCourse(id);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CAMERA)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(CreateUpdateActivity.this, "Permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null)
        {
            return;
        }

        if (resultCode == RESULT_OK && requestCode == GALLERY_PICTURE)
        {
            Uri selectedImage = data.getData();

            firebaseUri = data.getData();

            try
            {
                InputStream inputStream = getContentResolver().openInputStream(selectedImage);
                bitmap = BitmapFactory.decodeStream(inputStream);
            }
            catch (Exception e)
            {
                Toast.makeText(CreateUpdateActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        else if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST)
        {
            bitmap = (Bitmap) data.getExtras().get("data");


            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, "Profil", "Foto Profil Saya");

            firebaseUri = Uri.parse(path);


        }

        resizeBitmap = new ResizeBitmap();
        bitmap = resizeBitmap.getResizedBitmap(bitmap, 512);

        ivFoto.setImageBitmap(bitmap);
    }

    private boolean isEmpty(TextInputEditText editText, String input)
    {
        if(editText.getText().toString().isEmpty())
        {
            editText.setError(input + " Kosong !");
            return false;
        }
        else
        {
            return true;
        }
    }

    private void createCourse()
    {
        setLoading(true);

//        ivGambar.buildDrawingCache();
//        bitmap = ivGambar.getDrawingCache();

        Course course = new Course(
                etNama.getText().toString(),
                etDeskripsi.getText().toString(),
                etKode.getText().toString(),
                etJurusan.getText().toString());

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

    private void updateCourse(long id)
    {
        setLoading(true);

//        ivGambar.buildDrawingCache();
//        bitmap = ivGambar.getDrawingCache();

        Course course = new Course(
                etNama.getText().toString(),
                etDeskripsi.getText().toString(),
                etKode.getText().toString(),
                etJurusan.getText().toString());

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