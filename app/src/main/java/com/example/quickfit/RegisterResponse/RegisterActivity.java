package com.example.quickfit.RegisterResponse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.quickfit.R;

public class RegisterActivity extends AppCompatActivity {

    EditText regUsername, regEmail, regPhone, regPass, regRePass;
    Button signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        regUsername = findViewById(R.id.regUsername);
        regEmail = findViewById(R.id.regEmail);
        regPhone = findViewById(R.id.regPhone);
        regPass = findViewById(R.id.regPass);
        regRePass = findViewById(R.id.regRePass);
        signUpBtn = findViewById(R.id.signUpBtn);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(regUsername.getText().toString().isEmpty() || regEmail.getText().toString().isEmpty() || regPass.getText().toString().isEmpty() || regRePass.getText().toString().isEmpty() || regPhone.getText().toString().isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Please Enter all details", Toast.LENGTH_SHORT).show();
                }else{
                    if(regPass.getText().toString().trim().equals(regRePass.getText().toString().trim())){
                        String method, username, email, phone, password;
                        method = "register";
                        username = regUsername.getText().toString();
                        email = regEmail.getText().toString();
                        phone = regPhone.getText().toString();
                        password = regPass.getText().toString();
                        GetRegisterResponse backgroundTask = new GetRegisterResponse(RegisterActivity.this);
                        backgroundTask.execute(method, username, email, phone, password);
                    }else{
                        regRePass.setError("Required!");
                        regPass.setError("Required!");
                        Toast.makeText(RegisterActivity.this, "Password does not matches!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

}