package com.example.quickfit.RegisterResponse;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

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
                httpURLConnection.setRequestMethod("GET");
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
                is.close();
                return data;
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
        Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
