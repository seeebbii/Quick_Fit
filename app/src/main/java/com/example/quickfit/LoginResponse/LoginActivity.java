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
import android.provider.Settings;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
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
import com.example.quickfit.DashboardActivity;
import com.example.quickfit.MainActivity;
import com.example.quickfit.R;
import com.example.quickfit.RegisterResponse.RegisterActivity;
import com.example.quickfit.SharedPref;
import com.google.firebase.iid.FirebaseInstanceId;

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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btRegister;
    private Button loginBtn;
    private EditText USERNAME;
    private EditText PASSWORD;
    ProgressDialog mProgressDialog;
    String login_url = "http://sania.co.uk/quick_fix/login.php";
    public String android_id, device_token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btRegister = findViewById(R.id.btnRegister);
        btRegister.setOnClickListener(this);
        loginBtn = findViewById(R.id.loginBtn);
        USERNAME = findViewById(R.id.username);
        PASSWORD = findViewById(R.id.password);
        // GETTING DEVICE TOKEN AND DEVICE ID
        android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        device_token = FirebaseInstanceId.getInstance().getToken();

        mProgressDialog = new ProgressDialog(this);
        //mProgressDialog.show();
        mProgressDialog.setContentView(R.layout.progress_dialog);
        mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));



        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyBoard();
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

    public void addDeviceToken(String token, String android_id) {
        String url = "https://sania.co.uk/quick_fix/AddClientDeviceToken.php?token=" + token + "&android_id=" + android_id;
        Log.d("url", url);
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response", response);
                        //Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NetworkError) {
                            Toast.makeText(LoginActivity.this, getString(R.string.Network_error), Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(LoginActivity.this, getString(R.string.Server_error_ksa), Toast.LENGTH_SHORT).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(LoginActivity.this, getString(R.string.Auth_Failure_error), Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(LoginActivity.this, getString(R.string.Parse_error), Toast.LENGTH_SHORT).show();
                        } else if (error instanceof NoConnectionError) {
                            Toast.makeText(LoginActivity.this, getString(R.string.Connection_error), Toast.LENGTH_SHORT).show();
                        } else if (error instanceof TimeoutError) {
                            Toast.makeText(LoginActivity.this, getString(R.string.Timeout_error), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, getString(R.string.Something_went_wrong_ksa), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };


        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                8000,
                2,
                2));

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(postRequest);
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
                mProgressDialog.dismiss();
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
                // ADDING DEVICE TOKEN TO SERVER
                addDeviceToken(device_token, android_id);

                // SAVING DATA IN SHARED PREFERENCES
                SharedPref.savePreferencesBoolean("isLoggedIn",true, LoginActivity.this);
                SharedPref.savePreferencesInt("user_id",Integer.parseInt(data[0]), LoginActivity.this);
                SharedPref.savePreferences("user_name",data[1], LoginActivity.this);
                SharedPref.savePreferences("user_email",data[2], LoginActivity.this);
                SharedPref.savePreferences("user_phone",data[3], LoginActivity.this);
                SharedPref.savePreferences("user_statusCode",data[4], LoginActivity.this);
                SharedPref.savePreferences("user_image",data[5], LoginActivity.this);

                mProgressDialog.dismiss();

                if(!data[4].equals("0")){
                    Intent dashBoard = new Intent(LoginActivity.this, DashboardActivity.class);
                    startActivity(dashBoard);
                    finish();
                } else{
                    Toast.makeText(context, "You have been blocked by admin!", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

}