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
import com.yory3r.e_learning.preferences.AdminPreferences;
import com.yory3r.e_learning.preferences.EditAkunPreferences;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener
{
    private String EMAIL_ADMIN = "yolifsyebathanim@yahoo.com";

    private EditText etEmail;
    private EditText etPassword;
    private Button btnRegister;
    private Button btnLogin;
    private Intent intent;

    private AdminPreferences preferences;

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



        preferences = new AdminPreferences(LoginActivity.this);

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
            boolean inputEmail = isEmpty(etEmail,"Email");
            boolean inputPassword = isEmpty(etPassword,"Password");
            boolean emailValidation = emailValidation(etEmail);
            boolean passwordValidation = passwordValidation(etPassword);

            if(inputEmail && inputPassword && emailValidation && passwordValidation)
            {
                String email = etEmail.getText().toString().toLowerCase();
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

    private boolean passwordValidation(EditText password)
    {
        String strPassword = password.getText().toString();

        if(!strPassword.isEmpty())
        {
            if(strPassword.length() < 6)
            {
                password.setError("Password Minimal 6 Karakter");
            }
            else
            {
                return true;
            }
        }

        return false;
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
        if(user.getEmail().equals(EMAIL_ADMIN))
        {
            preferences.loginAdmin("true");
        }
        else
        {
            preferences.logoutAdmin();
        }

        intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}