package com.example.quickfit.Deals;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.quickfit.Brands.BrandItemsModel;
import com.example.quickfit.DashboardActivity;
import com.example.quickfit.R;
import com.google.gson.Gson;

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
import java.util.HashMap;
import java.util.Map;

public class Promotion_Deals_Fragment extends Fragment {

    ListView commentsListView;
    ArrayList<Offers_Model> list;
    ProgressDialog mProgressDialog;
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

        list = new ArrayList<Offers_Model>();


        adapter = new OfferCustomAdapter(getContext(), list);
        commentsListView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        new NetworkTask().execute();
    }

    public void getBrands() {
        String url = "https://sania.co.uk/quick_fix/getOffers.php?status_code=" + DashboardActivity.CURRENT_USER.getStatusCode();
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int id;
                        String nameOfUser;
                        String brandName;
                        String serviceName;
                        String imageUrl;
                        String offerDetails;
                        String validityTime;
                        try {
                            list.clear();
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject offer = jsonArray.getJSONObject(i);
                                nameOfUser = offer.getString("name");
                                id = offer.getInt("id");
                                imageUrl = offer.getString("image_url");
                                validityTime = offer.getString("validity_time");
                                brandName = offer.getString("offer_brand");
                                serviceName = offer.getString("offer_service");
                                offerDetails = offer.getString("details");

                                Offers_Model offers_model = new Offers_Model(id, nameOfUser, brandName, serviceName, offerDetails, validityTime, imageUrl);
                                list.add(offers_model);
                                adapter.notifyDataSetChanged();
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NetworkError) {
                            Toast.makeText(getContext(), getString(R.string.Network_error), Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(getContext(), getString(R.string.Server_error_ksa), Toast.LENGTH_SHORT).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(getContext(), getString(R.string.Auth_Failure_error), Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(getContext(), getString(R.string.Parse_error), Toast.LENGTH_SHORT).show();
                        } else if (error instanceof NoConnectionError) {
                            Toast.makeText(getContext(), getString(R.string.Connection_error), Toast.LENGTH_SHORT).show();
                        } else if (error instanceof TimeoutError) {
                            Toast.makeText(getContext(), getString(R.string.Timeout_error), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), getString(R.string.Something_went_wrong_ksa), Toast.LENGTH_SHORT).show();
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

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(postRequest);
    }

    class NetworkTask extends AsyncTask<Void, String, String>{

        @Override
        protected String doInBackground(Void... voids) {


            try {
                Thread.sleep(1400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        public NetworkTask() {
            super();
        }

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.show();
            mProgressDialog.setContentView(R.layout.progress_dialog);
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getBrands();
        }

        @Override
        protected void onPostExecute(String s) {
            mProgressDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }
}