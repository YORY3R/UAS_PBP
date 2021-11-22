package com.yory3r.e_learning.activities;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yory3r.e_learning.R;
import com.yory3r.e_learning.databinding.ActivityProfileBinding;
import com.yory3r.e_learning.databinding.DialogEditBinding;
import com.yory3r.e_learning.models.UserModel;
import com.yory3r.e_learning.utils.ChangeString;
import com.yory3r.e_learning.utils.ResizeBitmap;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener
{
    private static final int PERMISSION_REQUEST_CAMERA = 100;
    private static final int CAMERA_REQUEST = 0;
    private static final int GALLERY_PICTURE = 1;


    private ImageView ivFoto;
    private EditText etNama;
    private EditText etNpm;
    private EditText etTelepon;
    private EditText etEmail;
    private EditText etPassword;
    private Button btnEdit;
    private Button btnDone;
    private Button btnCancel;
    private Button btnShowPassword;


    private ChangeString change;

    private String url;
    private String tempNama;
    private String tempNpm;
    private String tempTelepon;
    private String tempEmail;
    private String tempPassword;

    private Bitmap bitmap = null;

    private ResizeBitmap resizeBitmap;


    private Uri firebaseUri;

    private Intent intent;


    private FirebaseAuth auth;
    private FirebaseUser user;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private FirebaseStorage storage;
    private StorageReference storageReference;

    private ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        int layout = R.layout.activity_profile;
        binding = DataBindingUtil.setContentView(ProfileActivity.this,layout);
        binding.setActivityProfile(ProfileActivity.this);

        change = new ChangeString();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Users/" + change.DotsToEtc(user.getEmail()));

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl("gs://e-learning-1de33.appspot.com/Foto/" + change.DotsToEtc(user.getEmail()));




        initView();
        initListener();
        getData();

        ivFoto.setEnabled(false);






    }

    private void initView()
    {
        ivFoto = binding.ivFoto;
        etNama = binding.etNama;
        etNpm = binding.etNpm;
        etTelepon = binding.etTelepon;
        etEmail = binding.etEmail;
        etPassword = binding.etPassword;

        btnEdit = binding.btnEdit;
        btnDone = binding.btnDone;
        btnCancel = binding.btnCancel;
        btnShowPassword = binding.btnShowPassword;
    }

    private void initListener()
    {
        ivFoto.setOnClickListener(ProfileActivity.this);
        btnEdit.setOnClickListener(ProfileActivity.this);
        btnDone.setOnClickListener(ProfileActivity.this);
        btnCancel.setOnClickListener(ProfileActivity.this);
        btnShowPassword.setOnTouchListener(ProfileActivity.this);
    }

    private void getData()
    {
        databaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                tempNama = snapshot.getValue(UserModel.class).getNama();
                tempNpm = snapshot.getValue(UserModel.class).getNpm();
                tempTelepon = snapshot.getValue(UserModel.class).getNomorTelepon();
                tempEmail = snapshot.getValue(UserModel.class).getEmail();
                tempPassword = snapshot.getValue(UserModel.class).getPassword();

                setText();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(ProfileActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>()
        {
            @Override
            public void onComplete(@NonNull Task<Uri> task)
            {
                if(task.isSuccessful())
                {
                    url = task.getResult().toString();

                    Glide.with(ProfileActivity.this).load(url).into(ivFoto);
                }
                else
                {
                    Toast.makeText(ProfileActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setText()
    {
        etNama.setText(tempNama);
        etNpm.setText(tempNpm);
        etTelepon.setText(tempTelepon);
        etEmail.setText(tempEmail);
        etPassword.setText(tempPassword);
    }

    private void setEnable(int value)
    {
        if(value == 0)
        {
            ivFoto.setEnabled(false);
            etNama.setEnabled(false);
            etNpm.setEnabled(false);
            etTelepon.setEnabled(false);
            etEmail.setEnabled(false);
            etPassword.setEnabled(false);

            btnEdit.setVisibility(View.VISIBLE);
            btnDone.setVisibility(View.GONE);
            btnCancel.setVisibility(View.GONE);
        }
        else
        {
            ivFoto.setEnabled(true);
            etNama.setEnabled(true);
            etNpm.setEnabled(true);
            etTelepon.setEnabled(true);
            etEmail.setEnabled(false);
            etPassword.setEnabled(true);

            btnEdit.setVisibility(View.GONE);
            btnDone.setVisibility(View.VISIBLE);
            btnCancel.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == ivFoto.getId())
        {
            LayoutInflater layoutInflater = LayoutInflater.from(ProfileActivity.this);
            View selectMediaView = layoutInflater.inflate(R.layout.layout_select_media, null);
            final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(selectMediaView.getContext()).create();


            // TODO: 20/11/2021 GANTI MENGGUNAKAN BINDING
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
        else if(view.getId() == btnEdit.getId())
        {
            setEnable(1);
        }
        else if(view.getId() == btnDone.getId())
        {
            boolean inputNama = isEmpty(etNama);
            boolean inputNpm = isEmpty(etNpm);
            boolean inputTelepon = isEmpty(etTelepon);
            boolean inputPassword = isEmpty(etPassword);

            if(inputNama && inputNpm && inputTelepon && inputPassword)
            {
                String nama = etNama.getText().toString();
                String npm = etNpm.getText().toString();
                String telepon = etTelepon.getText().toString();
                String password = etPassword.getText().toString();

                databaseReference.child("nama").setValue(nama);
                databaseReference.child("npm").setValue(npm);
                databaseReference.child("nomorTelepon").setValue(telepon);

                if(!password.equals(tempPassword))
                {
                    user.updatePassword(password)
                    .addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if(task.isSuccessful())
                            {
                                databaseReference.child("password").setValue(password);
                                gotoLoginActivity();

                                Toast.makeText(ProfileActivity.this, "Silahkan Login Lagi", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                databaseReference.child("password").setValue(tempPassword);
                                Toast.makeText(ProfileActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                if(firebaseUri != null)
                {
                    storageReference.putFile(firebaseUri);
                }

                setEnable(0);
            }
        }
        else if(view.getId() == btnCancel.getId())
        {
            setEnable(0);
            setText();

            Glide.with(ProfileActivity.this).load(url).into(ivFoto);
        }

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent)
    {

        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
        {
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT);
        }

        if(motionEvent.getAction() == MotionEvent.ACTION_UP)
        {
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }

        return true;
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
                Toast.makeText(ProfileActivity.this, "Permission denied.", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private boolean isEmpty(EditText editText)
    {
        if(editText.getText().toString().isEmpty())
        {
            editText.setError("Input Kosong !");
            return false;
        }
        else
        {
            return true;
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        intent = new Intent(ProfileActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void gotoLoginActivity()
    {
        intent = new Intent(ProfileActivity.this, LoginActivity.class);
        startActivity(intent);
        auth.signOut();
        finish();
    }
}