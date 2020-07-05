package com.example.quickfit.LoginResponse;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.quickfit.DashboardActivity;
import com.example.quickfit.R;
import com.example.quickfit.RegisterActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btRegister;
    private TextView tvLogin;
    private Button loginBtn;
    private EditText USERNAME;
    private EditText PASSWORD;
    ProgressDialog mProgressDialog;
    String login_url = "http://sania.co.uk/quick_fix/login.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btRegister = findViewById(R.id.btRegister);
        tvLogin = findViewById(R.id.tvLogin);
        btRegister.setOnClickListener(this);
        loginBtn = findViewById(R.id.loginBtn);
        USERNAME = findViewById(R.id.username);
        PASSWORD = findViewById(R.id.password);

//        mProgressDialog = new ProgressDialog(this);
//        //mProgressDialog.show();
//        mProgressDialog.setContentView(R.layout.progress_dialog);
//        mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));



        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(USERNAME.getText().toString().trim().equals("") || PASSWORD.getText().toString().trim().equals("")){
                    Toast.makeText(LoginActivity.this, "Please enter your credentials first...", Toast.LENGTH_SHORT).show();
                    USERNAME.setError("Required!");
                    PASSWORD.setError("Required!");
                }else{
                    final String username = USERNAME.getText().toString();
                    final String password = PASSWORD.getText().toString();
                    String type = "login";
                    GetLoginResponse backgroundWorker = new GetLoginResponse(LoginActivity.this);
                    backgroundWorker.execute(type, username, password);
                    startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                }

            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        if (v==btRegister){
            Intent intent   = new Intent(LoginActivity.this, RegisterActivity.class);
            Pair[] pairs    = new Pair[1];
            pairs[0] = new Pair<View,String>(tvLogin,"tvLogin");
            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this,pairs);
            startActivity(intent,activityOptions.toBundle());
        }
    }

}