package com.yory3r.e_learning.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
//import com.christophesmet.android.views.maskableframelayout.MaskableFrameLayout;
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
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
import com.yory3r.e_learning.preferences.AdminPreferences;
import com.yory3r.e_learning.preferences.EditAkunPreferences;
import com.yory3r.e_learning.utils.ChangeString;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private AppBarConfiguration appBarConfiguration;
    private NavController navController;


    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    private MenuItem menuItem;
    private MenuItem adminItem;


    private View navigationHeader;

//    private MaskableFrameLayout mflFoto;
    private PorterShapeImageView ivFotoProfil;
    private TextView tvNamaProfil;
    private TextView tvEmailProfil;







    private ChangeString change;


    private Intent intent;

    private EditAkunPreferences editAkunPreferences;
    private AdminPreferences adminPreferences;



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


// TODO: 02/12/2021 BUAT JADI INI PREFERENCEWS
        editAkunPreferences = new EditAkunPreferences(MainActivity.this);
        checkEdit();


        adminPreferences = new AdminPreferences(MainActivity.this);






    }

    private void initView()
    {
        drawerLayout = binding.drawerLayout;

        navigationView = binding.navigationView;

        toolbar = binding.mainToolbar.toolbar;


        navigationHeader = navigationView.getHeaderView(0);

//        mflFoto = navigationHeader.findViewById(navigationBinding.mflFoto.getId());
        ivFotoProfil = navigationHeader.findViewById(navigationBinding.ivFotoProfil.getId());
        tvNamaProfil = navigationHeader.findViewById(navigationBinding.tvNamaProfil.getId());
        tvEmailProfil = navigationHeader.findViewById(navigationBinding.tvEmailProfil.getId());


    }

    private void initListener()
    {
        navigationHeader.setOnClickListener(this);
    }

    private void initAppBar()
    {
        setSupportActionBar(toolbar);

        appBarConfiguration = new AppBarConfiguration.Builder
        (
            R.id.nav_course,
            R.id.nav_quiz,
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
                if(task.isSuccessful())// TODO: 28/11/2021 BUAT SUPAYA EMAIL TIDDAK CAPITAL CAPSWORD 
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

    private void checkEdit()
    {


        if(editAkunPreferences.checkEdit())
        {
            navigationHeader.setEnabled(true);
        }
        else
        {
            navigationHeader.setEnabled(false);
        }
    }

    private void checkAnim()
    {
//        if(animFotoPreferences.checkAnim())
//        {
//            Drawable drawable = mflFoto.getDrawableMask();
//
//            if (drawable instanceof AnimationDrawable)
//            {
//                AnimationDrawable animationDrawable = (AnimationDrawable) drawable;
//
//                animationDrawable.selectDrawable(0);
//                animationDrawable.stop();
//                animationDrawable.start();
//            }
//        }
    }

    private void checkAdmin()
    {
        if(adminPreferences.checkAdmin())
        {
            adminItem.setEnabled(true);
            adminItem.setVisible(true);
        }
        else
        {
            adminItem.setEnabled(false);
            adminItem.setVisible(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);

        for(int a = 0 ; a < menu.size() ; a++)
        {
            menuItem = menu.getItem(a);
            adminItem = menu.getItem(0);

            checkAdmin();

            int index = a;

            menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
            {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem)
                {
                    if(index == 0)//Admin
                    {
                        gotoAdminActivity();
                    }
                    else if(index == 1)//Favorite
                    {
                        gotoFavoriteActivity();
                    }
                    else if(index == 2)//maps
                    {
                        gotoMapsActivity();
                    }
                    else if(index == 3)//Settings
                    {
                        gotoSettingsActivity();
                    }
                    else if(index == 4)//Logout
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
    public void onClick(View view)
    {
        if(view.getId() == navigationHeader.getId())
        {
            gotoProfileActivity();
        }
    }

    private void gotoAdminActivity()
    {
        intent = new Intent(MainActivity.this, AdminActivity.class);
        startActivity(intent);
        finish();
    }

    private void gotoFavoriteActivity()
    {
        intent = new Intent(MainActivity.this, FavoriteActivity.class);
        startActivity(intent);
        finish();
    }

    private void gotoMapsActivity()
    {
        intent = new Intent(MainActivity.this, MapsActivity.class);
        startActivity(intent);
        finish();
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
        editAkunPreferences.uncheckEdit();
        finish();
    }

    private void gotoRegisterActivity()
    {

        intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
        auth.signOut();
        finish();
    }

    private void gotoProfileActivity()
    {
        intent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(intent);
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
