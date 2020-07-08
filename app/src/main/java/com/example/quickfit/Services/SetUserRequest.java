package com.example.quickfit.Services;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.quickfit.R;
import com.google.android.gms.common.util.Strings;

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

public class SetUserRequest extends AsyncTask< String, Void, String> {

    public SetUserRequest() {
        super();
    }
    Context context;
    public SetUserRequest(Context ctx){
        this.context = ctx;
    }

    Dialog mDialog;
    @Override
    protected String doInBackground(String... params) {
        String REG_URL = "http://sania.co.uk/quick_fix/sendPushNotification.php";

        String method = params[0];
        if(method.equals("setRequest")){
            String userId = params[1];
            String userName = params[2];
            String userEmail = params[3];
            String userPhone = params[4];
            String brandId = params[5];
            String brandName = params[6];
            String serviceId = params[7];
            String serviceName = params[8];
            String userLat = params[9];
            String userLong = params[10];

            try {
                URL url = new URL(REG_URL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String data = URLEncoder.encode("user_id", "UTF-8")+"=" + URLEncoder.encode(userId, "UTF-8")+"&"+
                        URLEncoder.encode("qf_name", "UTF-8")+"=" + URLEncoder.encode(userName, "UTF-8")+"&"+
                        URLEncoder.encode("qf_email", "UTF-8")+"=" + URLEncoder.encode(userEmail, "UTF-8")+"&"+
                        URLEncoder.encode("qf_phone", "UTF-8")+"=" + URLEncoder.encode(userPhone, "UTF-8")+"&"+
                        URLEncoder.encode("brandId", "UTF-8")+"=" + URLEncoder.encode(brandId, "UTF-8")+"&"+
                        URLEncoder.encode("selected_brand_name", "UTF-8")+"=" + URLEncoder.encode(brandName, "UTF-8")+"&"+
                        URLEncoder.encode("serviceId", "UTF-8")+"=" + URLEncoder.encode(serviceId, "UTF-8")+"&"+
                        URLEncoder.encode("selected_service_name", "UTF-8")+"=" + URLEncoder.encode(serviceName, "UTF-8")+"&"+
                        URLEncoder.encode("user_lat", "UTF-8")+"=" + URLEncoder.encode(userLat, "UTF-8")+"&"+
                        URLEncoder.encode("user_long", "UTF-8")+"=" + URLEncoder.encode(userLong, "UTF-8");
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

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mDialog = new Dialog(context);
        mDialog.setContentView(R.layout.dialog_appointmentfixed);

    }

    @Override
    protected void onPostExecute(String result) {
        if(result.equals("Yahoo!!! Message sent successfully")){
            Toast.makeText(context, "Appointment Fixed!", Toast.LENGTH_SHORT).show();
            mDialog.show();
            Button dialogBtn = mDialog.findViewById(R.id.dialogBtn);
            dialogBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDialog.dismiss();
                }
            });
        }else{
            Toast.makeText(context, "Something went wrong...!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

}
