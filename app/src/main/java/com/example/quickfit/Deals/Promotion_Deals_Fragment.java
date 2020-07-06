package com.example.quickfit.Deals;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.quickfit.Brands.BrandItemsModel;
import com.example.quickfit.DashboardActivity;
import com.example.quickfit.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.util.ArrayList;

public class Promotion_Deals_Fragment extends Fragment {

    ListView commentsListView;
    ArrayList<Offers_Model> list;
    ProgressDialog mProgressDialog;
    RequestQueue mQueue;
    OfferCustomAdapter adapter;
    final String URL = "http://sania.co.uk/quick_fix/getOffers.php";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_promotion__deals_, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        commentsListView = view.findViewById(R.id.commentsListView);
        mQueue = Volley.newRequestQueue(getContext());
        list = new ArrayList<Offers_Model>();
        String method = "status_code";
        new NetworkTask().execute(method, DashboardActivity.CURRENT_USER.getStatusCode());

        adapter = new OfferCustomAdapter(getContext(), list);
        commentsListView.setAdapter(adapter);
    }

    void parseJson(String JSON_STRING){
        int id;
        String nameOfUser;
        String brandName;
        String serviceName;
        String imageUrl;
        String offerDetails;
        String validityTime;
        try {
            JSONArray jsonArray = new JSONArray(JSON_STRING);
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject offer = jsonArray.getJSONObject(i);
                nameOfUser = offer.getString("name");
                id = offer.getInt("id");
                imageUrl = offer.getString("image_url");
                validityTime = offer.getString("validity_time");
                brandName = offer.getString("offer_brand");
                serviceName = offer.getString("offer_service");
                offerDetails = offer.getString("details");

                Offers_Model offers_model = new Offers_Model(id,nameOfUser, brandName, serviceName, offerDetails, validityTime, imageUrl);
                list.add(offers_model);

                // Refreshing data
                adapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class NetworkTask extends AsyncTask<String, Void, String> {


        public NetworkTask() {
            super();
        }

        @Override
        protected String doInBackground(String... params) {
            String method = params[0];
            if(method.equals("status_code")){
                String  status_code = params[1];
                try {
                    java.net.URL url = new URL(URL);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    OutputStream os = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    String data = URLEncoder.encode("status_code", "UTF-8")+"=" + URLEncoder.encode(status_code, "UTF-8");
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
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.show();
            mProgressDialog.setContentView(R.layout.progress_dialog);
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        }

        @Override
        protected void onPostExecute(String result) {
            mProgressDialog.dismiss();
            if(result.isEmpty()){
                Toast.makeText(getContext(), "OOPS, Something went wrong!", Toast.LENGTH_SHORT).show();
            }else{
                parseJson(result);
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

}