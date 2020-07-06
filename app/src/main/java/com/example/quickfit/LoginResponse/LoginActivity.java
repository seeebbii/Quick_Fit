package com.example.quickfit.LoginResponse;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quickfit.DashboardActivity;
import com.example.quickfit.MainActivity;
import com.example.quickfit.R;
import com.example.quickfit.RegisterResponse.RegisterActivity;

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
        btRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);
        btRegister.setOnClickListener(this);
        loginBtn = findViewById(R.id.loginBtn);
        USERNAME = findViewById(R.id.username);
        PASSWORD = findViewById(R.id.password);

        mProgressDialog = new ProgressDialog(this);
        //mProgressDialog.show();
        mProgressDialog.setContentView(R.layout.progress_dialog);
        mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));



        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(USERNAME.getText().toString().trim().equals("") || PASSWORD.getText().toString().trim().equals("")){
                    Toast.makeText(LoginActivity.this, "Please enter your credentials first...", Toast.LENGTH_SHORT).show();
                    USERNAME.setError("Required!");
                    PASSWORD.setError("Required!");
                }else{
                    final String email = USERNAME.getText().toString().trim();
                    final String password = PASSWORD.getText().toString().trim();
                    String type = "login";
                    GetLoginResponse getLoginResponse = new GetLoginResponse(LoginActivity.this);
                    getLoginResponse.execute(type, email,password);
                }

            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        if (v==btRegister){
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        }
    }

    public class GetLoginResponse extends AsyncTask<String,Void,String> {
        Context context;
        GetLoginResponse (Context ctx) {
            context = ctx;
        }
        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            String login_url = "http://sania.co.uk/quick_fix/login.php";
            if(type.equals("login")) {
                try {
                    String user_name = params[1];
                    String password = params[2];
                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("user_email","UTF-8")+"="+URLEncoder.encode(user_name,"UTF-8")+"&"
                            +URLEncoder.encode("user_password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                    String result="";
                    String line="";
                    while((line = bufferedReader.readLine())!= null) {
                        result += line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return result;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return "null";
        }

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(LoginActivity.this);
            mProgressDialog.show();
            mProgressDialog.setContentView(R.layout.progress_dialog);
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        @Override
        protected void onPostExecute(String result) {
            if(result.equals("0 results")){
                Toast.makeText(context, "Invalid Credentials!", Toast.LENGTH_SHORT).show();
            }else{
                // LOGIC FOR EXTRACTING USER DATA FROM LOGIN RESPONSE AND SETTING IT TO LOGGED IN USER OBJECT
                String substring = "";
                String [] data = new String[6];
                int count = 0;
                int counter = 0;
                for (int i = 0; i < result.length(); i++){
                    if(count > 0 && result.charAt(i) != ','){
                        substring = substring + result.charAt(i);
                    }
                    if(result.charAt(i) == ',' || i == result.length() - 1){
                        data[counter] = substring;
                        counter++;
                        count = 0;
                        substring = "";
                    }
                    else if(result.charAt(i) == ':') {
                        count++;
                    }
                }

                 // SETTING UP USER OBJECT
                DashboardActivity.CURRENT_USER.setId(Integer.parseInt(data[0]));
                DashboardActivity.CURRENT_USER.setName(data[1]);
                DashboardActivity.CURRENT_USER.setEmail(data[2]);
                DashboardActivity.CURRENT_USER.setPhone(data[3]);
                DashboardActivity.CURRENT_USER.setStatusCode(data[4]);
                DashboardActivity.CURRENT_USER.setUserImageUrl(data[5]);

                mProgressDialog.dismiss();

                Intent dashBoard = new Intent(LoginActivity.this, DashboardActivity.class);
                startActivity(dashBoard);
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

}