package com.yory3r.e_learning.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.yory3r.e_learning.R;
import com.yory3r.e_learning.databinding.ActivityMainBinding;
import com.yory3r.e_learning.databinding.ActivitySettingsBinding;
import com.yory3r.e_learning.utils.ChangeString;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener
{
    private Button btnDelete;



    private ChangeString change;

    private Intent intent;

    private FirebaseAuth auth;
    private FirebaseUser user;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private FirebaseStorage storage;
    private StorageReference storageReference;

    private ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        int layout = R.layout.activity_settings;
        binding = DataBindingUtil.setContentView(SettingsActivity.this,layout);
        binding.setActivitySettings(SettingsActivity.this);

        change = new ChangeString();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Users/" + change.DotsToEtc(user.getEmail()));

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl("gs://e-learning-1de33.appspot.com/Foto/" + change.DotsToEtc(user.getEmail()));


        initView();
        initListener();



    }

    private void initView()
    {
        btnDelete = binding.btnDelete;
    }

    private void initListener()
    {
        btnDelete.setOnClickListener(SettingsActivity.this);
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == btnDelete.getId())
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);

            builder.setTitle("Hapus Akun ?");
            builder.setMessage("Apakah anda yakin ingin hapus akun?");

            builder.setPositiveButton("Hapus", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    user.delete();

                    gotoLoginActivity();

                }
            });

            builder.setNegativeButton("Batal", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {


                    dialogInterface.dismiss();
                }
            });

            builder.show();
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        gotoMainActivity();
    }

    private void gotoMainActivity()
    {
        intent = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void gotoLoginActivity()
    {
        intent = new Intent(SettingsActivity.this, LoginActivity.class);
        startActivity(intent);
        auth.signOut();
        finish();
    }
}