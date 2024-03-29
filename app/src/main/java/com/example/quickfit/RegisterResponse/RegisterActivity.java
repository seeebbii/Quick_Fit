package com.example.quickfit.RegisterResponse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.quickfit.MyFirebaseMessagingService;
import com.example.quickfit.R;
import com.example.quickfit.SharedPref;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

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
                hideKeyBoard();
                if(regUsername.getText().toString().isEmpty() || regEmail.getText().toString().isEmpty() || regPass.getText().toString().isEmpty() || regRePass.getText().toString().isEmpty() || regPhone.getText().toString().isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Please Enter all details", Toast.LENGTH_SHORT).show();
                }else{
                    if(regPass.getText().toString().trim().equals(regRePass.getText().toString().trim())){
                        if(regPass.length() > 6){
                            String method, username, email, phone, password;
                            method = "register";
                            username = regUsername.getText().toString();
                            email = regEmail.getText().toString();
                            phone = regPhone.getText().toString();
                            password = regPass.getText().toString();
                            GetRegisterResponse backgroundTask = new GetRegisterResponse(RegisterActivity.this);
                            backgroundTask.execute(method, username, email, phone, password);
                        }else{
                            regRePass.setError("> 6");
                            regPass.setError("> 6");
                            Toast.makeText(RegisterActivity.this, "Password must be greater than 6 digits!", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        regRePass.setError("Not Same!");
                        regPass.setError("Not Same!");
                        Toast.makeText(RegisterActivity.this, "Password does not matches!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    public void hideKeyBoard() {
        try {
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public class GetRegisterResponse extends AsyncTask< String, Void, String> {

        Context context;
        public GetRegisterResponse(Context ctx){
            this.context = ctx;
        }

        @Override
        protected String doInBackground(String... params) {
            String REG_URL = "http://sania.co.uk/quick_fix/signup.php";

            String method = params[0];
            if(method.equals("register")){
                String name = params[1];
                String email = params[2];
                String phone = params[3];
                String password = params[4];

                try {
                    URL url = new URL(REG_URL);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    OutputStream os = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    String data = URLEncoder.encode("name", "UTF-8")+"=" + URLEncoder.encode(name, "UTF-8")+"&"+
                            URLEncoder.encode("email", "UTF-8")+"=" + URLEncoder.encode(email, "UTF-8")+"&"+
                            URLEncoder.encode("phone", "UTF-8")+"=" + URLEncoder.encode(phone, "UTF-8")+"&"+
                            URLEncoder.encode("password", "UTF-8")+"=" + URLEncoder.encode(password, "UTF-8");
                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    os.close();
                    InputStream is = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"));
                    String result="";
                    String line="";
                    while((line = bufferedReader.readLine())!= null) {
                        result += line;
                    }
                    bufferedReader.close();
                    is.close();
                    httpURLConnection.disconnect();
                    return result;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return "ERROR!";
        }

        public GetRegisterResponse() {
            super();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            if(result.equals("Email already exists")){
                Toast.makeText(context, "Email already exists", Toast.LENGTH_SHORT).show();
                regEmail.setError("Required");
            }else if(result.equals("Phone number already exists")){
                Toast.makeText(context, "Phone number already exists", Toast.LENGTH_SHORT).show();
                regPhone.setError("Required!");
            }else if(result.equals("Your Account created successfully")){
                Toast.makeText(context, "Account created successfully...!", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(context, "OOPS!,Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

}