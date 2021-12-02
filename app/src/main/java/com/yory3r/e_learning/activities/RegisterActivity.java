package com.yory3r.e_learning.activities;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.BoringLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yory3r.e_learning.R;
import com.yory3r.e_learning.databinding.ActivityRegisterBinding;
import com.yory3r.e_learning.models.UserModel;
import com.yory3r.e_learning.utils.ChangeString;
import com.yory3r.e_learning.utils.ResizeBitmap;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher
{
    private static final int PERMISSION_REQUEST_CAMERA = 100;
    private static final int CAMERA_REQUEST = 0;
    private static final int GALLERY_PICTURE = 1;
    private static final String[] LIST_JENIS_KELAMIN = new String[]{"Laki - Laki", "Perempuan"};


    private ImageView ivFoto;
    private TextInputEditText etNama;
    private AutoCompleteTextView tvTanggalLahir;
    private AutoCompleteTextView tvJenisKelamin;
    private TextInputEditText etNomorTelepon;
    private TextInputEditText etEmail;
    private TextInputEditText etPassword;
    private TextInputEditText etKonfirmasiPassword;
    private Button btnLogin;
    private Button btnRegister;
    private Calendar calendarDate;




    private Intent intent;
    private Boolean resume = false;


    private Bitmap bitmap = null;
    private ResizeBitmap resizeBitmap;


    private Uri firebaseUri;

    ChangeString change;


    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        int layout = R.layout.activity_register;
        binding = DataBindingUtil.setContentView(RegisterActivity.this,layout);
        binding.setActivityRegister(RegisterActivity.this);

        change = new ChangeString();

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("Users");

        auth = FirebaseAuth.getInstance();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("Foto/");

        initView();
        initAdapter();
        initListener();

    }

    private void initView()
    {
        ivFoto = binding.ivFoto;
        etNama = binding.etNama;
        tvTanggalLahir = binding.tvTanggalLahir;
        tvJenisKelamin = binding.tvJenisKelamin;
        etNomorTelepon = binding.etNomorTelepon;
        etEmail = binding.etEmail;
        etPassword = binding.etPassword;
        etKonfirmasiPassword = binding.etKonfirmasiPassword;
        btnLogin = binding.btnLogin;
        btnRegister = binding.btnRegister;
    }

    private void initAdapter()
    {
        int layout = R.layout.item_list;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(RegisterActivity.this,layout,LIST_JENIS_KELAMIN);
        tvJenisKelamin.setAdapter(adapter);


    }


    private void initListener()
    {
        ivFoto.setOnClickListener(RegisterActivity.this);
        tvTanggalLahir.setOnClickListener(RegisterActivity.this);
        btnLogin.setOnClickListener(RegisterActivity.this);
        btnRegister.setOnClickListener(RegisterActivity.this);

        tvTanggalLahir.addTextChangedListener(RegisterActivity.this);
        tvJenisKelamin.addTextChangedListener(RegisterActivity.this);
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == ivFoto.getId())
        {
            LayoutInflater layoutInflater = LayoutInflater.from(RegisterActivity.this);
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
        else if(view.getId() == tvTanggalLahir.getId())
        {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

            DatePickerDialog dialog = new DatePickerDialog(RegisterActivity.this, (datepicker ,year, month, day) ->
            {
                calendarDate = Calendar.getInstance();
                calendarDate.set(year,month,day);
                tvTanggalLahir.setText(format.format(calendarDate.getTime()));
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

            dialog.show();
        }
        else if(view.getId() == btnLogin.getId())
        {
            gotoLoginActivity();
        }
        else if(view.getId() == btnRegister.getId())
        {
            boolean inputFoto = isEmptyFoto(bitmap);
            boolean inputNama = isEmpty(etNama,"Nama");
            boolean inputTanggalLahir = isEmptyDropdown(tvTanggalLahir,"Tanggal Lahir");
            boolean inputJenisKelamin = isEmptyDropdown(tvJenisKelamin,"Jenis Kelamin");
            boolean inputNomorTelepon = isEmpty(etNomorTelepon,"Nomor Telepon");
            boolean inputEmail = isEmpty(etEmail,"Email");
            boolean inputPassword = isEmpty(etPassword,"Password");
            boolean inputKonfirmasiPassword = isEmpty(etKonfirmasiPassword,"Konfirmasi Password");

            boolean teleponValidation = teleponValidation(etNomorTelepon);
            boolean emailValidation = emailValidation(etEmail);
            boolean passwordValidation = passwordValidation(etPassword,etKonfirmasiPassword);

            if(inputFoto && inputNama && inputTanggalLahir && inputJenisKelamin && inputNomorTelepon && inputEmail && inputPassword && inputKonfirmasiPassword && teleponValidation && emailValidation && passwordValidation)
            {
                String nama = etNama.getText().toString();
                String tanggalLahir = tvTanggalLahir.getText().toString();
                String jenisKelamin=  tvJenisKelamin.getText().toString();
                String nomorTelepon = etNomorTelepon.getText().toString();
                String email = etEmail.getText().toString().toLowerCase();
                String password = etPassword.getText().toString();

                setButtonEnabled(false);

                registerUser(nama, tanggalLahir, jenisKelamin, nomorTelepon, email, password);
            }
        }
    }

    private void setButtonEnabled(boolean code)
    {
        btnRegister.setEnabled(code);
        btnLogin.setEnabled(code);

        btnRegister.setClickable(code);
        btnLogin.setClickable(code);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void afterTextChanged(Editable editable)
    {
        if(!tvTanggalLahir.getText().toString().isEmpty())
        {
            tvTanggalLahir.setError(null);
        }

        if(!tvJenisKelamin.getText().toString().isEmpty())
        {
            tvJenisKelamin.setError(null);
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

    private  boolean isEmptyFoto(Bitmap bitmap)
    {
        if(bitmap == null)
        {
            Toast.makeText(RegisterActivity.this, "Belum Memilih Foto Profil !", Toast.LENGTH_SHORT).show();
        }
        else
        {
            return true;
        }

        return false;
    }

    private boolean isEmptyDropdown(AutoCompleteTextView textView, String input)
    {
        if(textView.getText().toString().isEmpty())
        {
            textView.setError(input + " Kosong !");
        }
        else
        {
            return true;
        }

        return false;
    }

    private boolean teleponValidation(EditText telepon)
    {
        String strTelepon = telepon.getText().toString();
        int length = telepon.getText().toString().length();

        if((length < 11 || length > 13 || strTelepon.charAt(0) != '0' || strTelepon.charAt(1) != '8') && length != 0)
        {
            telepon.setError("Nomor Telepon Tidak Valid !");
        }
        else
        {
            return true;
        }

        return false;
    }

    private boolean emailValidation(EditText email)
    {
        String strEmail = email.getText().toString();

        if(!strEmail.isEmpty())
        {
            if(strEmail.contains("@") && strEmail.contains("."))
            {
                return true;
            }
            else
            {
                email.setError("Email Tidak Valid !");
            }
        }

        return false;
    }

    private boolean passwordValidation(EditText password, EditText konfirmasi)
    {
        String strPassword = password.getText().toString();
        String strKonfirmasi = konfirmasi.getText().toString();

        if(strPassword.length() < 6)
        {
            password.setError("Password Minimal 6 Karakter");
        }
        else
        {
            if(strKonfirmasi.equals(strPassword))
            {
                return true;
            }
            else
            {
                konfirmasi.setError("Password Tidak Sama !");
            }
        }

        return false;
    }

    private void registerUser(String nama, String tanggalLahir, String jenisKelamin, String nomorTelepon, String email, String password)
    {
        auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (task.isSuccessful())
                {
                    user = auth.getCurrentUser();

                    if (user != null)
                    {
                        btnRegister.setEnabled(false);
                        btnLogin.setEnabled(false);

                        sendEmail(user);
                        sendDatabase(nama, tanggalLahir, jenisKelamin, nomorTelepon, email, password);
                    }
                }
                else
                {
                    String error = task.getException().getMessage();

                    if(error.contains("email"))
                    {
                        etEmail.setError(error);
                    }
                    else
                    {
                        etPassword.setError(error);
                    }
                }

                setButtonEnabled(true);
            }
        });
    }

    private void sendEmail(FirebaseUser user)
    {
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if(task.isSuccessful())
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);

                    builder.setTitle("Register Berhasil");
                    builder.setMessage("Silahkan Verifikasi Email");

                    builder.setPositiveButton("Verifikasi", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        {
                            intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                            resume = true;
                        }
                    });

                    builder.show();
                }
            }
        });
    }

    private void sendDatabase(String nama, String tanggalLahir, String jenisKelamin, String nomorTelepon, String email, String password)
    {
        UserModel userModel = new UserModel();

        userModel.setFoto(resizeBitmap.bitmapToBase64(bitmap));
        userModel.setNama(nama);
        userModel.setTanggalLahir(tanggalLahir);
        userModel.setJenisKelamin(jenisKelamin);
        userModel.setNomorTelepon(nomorTelepon);
        userModel.setEmail(email);
        userModel.setPassword(password);

        databaseReference.child(change.DotsToEtc(email)).setValue(userModel)
        .addOnCompleteListener(new OnCompleteListener<Void>()
        {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful())
                {
                    storageReference.child(change.DotsToEtc(email)).putFile(firebaseUri)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
                        {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(RegisterActivity.this, "Register Berhasil !", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
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
                Toast.makeText(RegisterActivity.this, "Permission denied.", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onStart()
    {
        super.onStart();
        user = auth.getCurrentUser();

        if(user != null)
        {
            gotoLoginActivity();
        }
    }

    @Override
    protected void onPostResume()
    {
        super.onPostResume();

        if(resume)
        {
            gotoLoginActivity();
        }
    }

    private void gotoLoginActivity()
    {
        intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}