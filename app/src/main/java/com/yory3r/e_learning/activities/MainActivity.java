package com.yory3r.e_learning.activities;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.yory3r.e_learning.R;
import com.yory3r.e_learning.databinding.ActivityMainBinding;
import com.yory3r.e_learning.databinding.ActivityMainNavigationHeaderBinding;
import com.yory3r.e_learning.models.UserModel;
import com.yory3r.e_learning.utils.ChangeString;
import com.yory3r.e_learning.utils.ResizeBitmap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener
{
    private static final int PERMISSION_REQUEST_CAMERA = 100;
    private static final int CAMERA_REQUEST = 0;
    private static final int GALLERY_PICTURE = 1;



    private AppBarConfiguration appBarConfiguration;
    private NavController navController;


    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private BottomNavigationView bottomNavigation;

    private MenuItem menuItem;


    private View navigationHeader;

    private ImageView ivFotoProfil;
    private TextView tvNamaProfil;
    private TextView tvEmailProfil;

    private Bitmap bitmap = null;


    private ChangeString change;


    private ProgressBar progressBar;
    private Intent intent;



    private FirebaseAuth auth;
    private FirebaseUser user;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private FirebaseStorage storage;
    private StorageReference storageReference;

    private ActivityMainBinding binding;
    private ActivityMainNavigationHeaderBinding navigationBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        navigationBinding = ActivityMainNavigationHeaderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        change = new ChangeString();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Users");


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl("gs://e-learning-1de33.appspot.com/Foto/" + change.DotsToEtc(user.getEmail()));

        initView();
        initListener();
        initAppBar();
        initNavigation();

        getData();









    }

    private void initView()
    {
        drawerLayout = binding.drawerLayout;

        navigationView = binding.navigationView;

        toolbar = binding.mainToolbar.toolbar;

        bottomNavigation = binding.mainToolbar.bottomNavigation;


        navigationHeader = navigationView.getHeaderView(0);

        ivFotoProfil = navigationHeader.findViewById(navigationBinding.ivFotoProfil.getId());
        tvNamaProfil = navigationHeader.findViewById(navigationBinding.tvNamaProfil.getId());
        tvEmailProfil = navigationHeader.findViewById(navigationBinding.tvEmailProfil.getId());

    }

    private void initListener()
    {
        navigationHeader.setOnClickListener(this);
        ivFotoProfil.setOnClickListener(this);

        bottomNavigation.setOnNavigationItemSelectedListener(this);
    }

    private void initAppBar()
    {
        setSupportActionBar(toolbar);

        appBarConfiguration = new AppBarConfiguration.Builder
        (
            R.id.nav_module,
            R.id.nav_share,
            R.id.nav_about
        )
        .setOpenableLayout(drawerLayout)
        .build();
    }

    private void initNavigation()
    {
        navController = Navigation.findNavController(MainActivity.this, R.id.fragmentRoot);

        NavigationUI.setupActionBarWithNavController(MainActivity.this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    private void getData()
    {
        databaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                String user = change.DotsToEtc(MainActivity.this.user.getEmail());
                String nama = snapshot.child(user).getValue(UserModel.class).getNama();
                String email = snapshot.child(user).getValue(UserModel.class).getEmail();

                tvNamaProfil.setText(nama);
                tvEmailProfil.setText(email);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>()
        {
            @Override
            public void onComplete(@NonNull Task<Uri> task)
            {
                if(task.isSuccessful())
                {
                    String url = task.getResult().toString();

                    Glide.with(MainActivity.this).load(url).into(ivFotoProfil);
                }
                else
                {
                    Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);

        for(int a = 0 ; a < menu.size() ; a++)
        {
            menuItem = menu.getItem(a);

            int index = a;

            menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
            {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem)
                {
                    if(index == 0)//Settings
                    {
                        gotoSettingsActivity();
                    }
                    else if(index == 1)//Logout
                    {
                        gotoLoginActivity();
                    }

                    return true;
                }
            });
        }

        return true;
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
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
                Toast.makeText(MainActivity.this, "Permission denied.", Toast.LENGTH_SHORT).show();
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

            try
            {
                InputStream inputStream = getContentResolver().openInputStream(selectedImage);
                bitmap = BitmapFactory.decodeStream(inputStream);
            }
            catch (Exception e)
            {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        else if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST)
        {
            bitmap = (Bitmap) data.getExtras().get("data");
        }

        ResizeBitmap resizeBitmap = new ResizeBitmap();
        bitmap = resizeBitmap.getResizedBitmap(bitmap, 512);

        ivFotoProfil.setImageBitmap(bitmap);
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == navigationHeader.getId())
        {
            Toast.makeText(getApplicationContext(), "Click Header", Toast.LENGTH_SHORT).show();

            intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
            finish();
        }
        else if(view.getId() == ivFotoProfil.getId())
        {
            LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
            View selectMediaView = layoutInflater.inflate(R.layout.layout_select_media, null);
            final AlertDialog alertDialog = new AlertDialog.Builder(selectMediaView.getContext()).create();


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
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        if(item.getItemId() == R.id.menuHome)
        {
            Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_SHORT).show();
        }
        else if(item.getItemId() == R.id.menuGame)
        {
            Toast.makeText(getApplicationContext(), "Game", Toast.LENGTH_SHORT).show();
        }

        return true;
    }

    private void gotoSettingsActivity()
    {
        intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
        finish();
    }

    private void gotoLoginActivity()
    {
        intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        auth.signOut();
        finish();
    }

    private void gotoRegisterActivity()
    {

        intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
        auth.signOut();
        finish();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        user = auth.getCurrentUser();

        if(user == null)
        {
            gotoRegisterActivity();
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
}
