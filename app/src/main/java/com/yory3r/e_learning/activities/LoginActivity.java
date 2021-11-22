package com.yory3r.e_learning.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.yory3r.e_learning.R;
import com.yory3r.e_learning.databinding.ActivityLoginBinding;
import com.yory3r.e_learning.databinding.ActivityRegisterBinding;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener
{
    private EditText etEmail;
    private EditText etPassword;
    private Button btnRegister;
    private Button btnLogin;
    private Intent intent;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        int layout = R.layout.activity_login;
        binding = DataBindingUtil.setContentView(LoginActivity.this,layout);
        binding.setActivityLogin(LoginActivity.this);

        auth = FirebaseAuth.getInstance();

        initView();
        initListener();
    }

    private void initView()
    {
        etEmail = binding.etEmail;
        etPassword = binding.etPassword;
        btnRegister = binding.btnRegister;
        btnLogin = binding.btnLogin;
    }

    private void initListener()
    {
        btnRegister.setOnClickListener(LoginActivity.this);
        btnLogin.setOnClickListener(LoginActivity.this);
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == btnRegister.getId())
        {
            gotoRegisterActivity();
        }
        else if(view.getId() == btnLogin.getId())
        {
            Boolean input1 = isEmpty(etEmail,"Email");
            Boolean input2 = isEmpty(etPassword,"Password");

            if(input1 && input2)
            {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                loginUser(email, password);
            }
        }
    }

    private boolean isEmpty(EditText editText, String input)
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

    private void loginUser(String email, String password)
    {
        auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (task.isSuccessful())
                {
                    user = auth.getCurrentUser();

                    if(user.isEmailVerified())
                    {
                        gotoMainActivity();
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this, "Belum Verifikasi Email !", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {

                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        user = auth.getCurrentUser();

        if(user != null)
        {
            if(user.isEmailVerified())
            {
                gotoMainActivity();
            }
        }
    }

    private void gotoRegisterActivity()
    {
        intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
        auth.signOut();
        finish();
    }

    private void gotoMainActivity()
    {
        intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}