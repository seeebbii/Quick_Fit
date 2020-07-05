package com.example.quickfit.Services;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.quickfit.Brands.BrandItemsModel;
import com.example.quickfit.Brands.BrandsCustomAdapter;
import com.example.quickfit.DashboardActivity;
import com.example.quickfit.MainActivity;
import com.example.quickfit.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class ServiceFragment extends Fragment{

    List<ServicesItemModel> services;
    GridView gridView;
    ServicesCustomAdapter customAdapter;
    MenuItem searchItem;
    final String URL = "http://sania.co.uk/quick_fix/services_api.php";
    RequestQueue mQueue;
    ProgressDialog mProgressDialog;

    // SELECTED CHOICES
    public static String brandName;
    public static String serviceName;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_service, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gridView = view.findViewById(R.id.servicesGridView);
        services = new ArrayList<ServicesItemModel>();
        mQueue = Volley.newRequestQueue(getContext());

        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.show();
        mProgressDialog.setContentView(R.layout.progress_dialog);
        mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // REQUESTING BRAND OBJECT USING VOLLEY
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i= 0; i < response.length(); i++){
                    try {
                        int id;
                        String serviceName;
                        String serviceImage;
                        JSONObject service = response.getJSONObject(i);

                        serviceName = service.getString("name");
                        id = service.getInt("id");
                        serviceImage = service.getString("image_url");
                        ServicesItemModel object = new ServicesItemModel(serviceImage, serviceName, id);

                        services.add(object);
                        // Refreshing data
                        customAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage() + "", Toast.LENGTH_SHORT).show();
            }
        });

        mQueue.add(jsonArrayRequest);
        mProgressDialog.dismiss();

        customAdapter = new ServicesCustomAdapter(services, getContext());
        gridView.setAdapter(customAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // BRANDS MODEL LIST FILTERED IS FROM BRAND CUSTOM ADAPTER MADE STATIC
                searchItem.collapseActionView();
                if(getArguments()!= null){
                    Bundle bundle = getArguments();
                    brandName = bundle.getString("BrandName");
                    double latitude = Double.parseDouble(bundle.getString("latitude"));
                    double longitude = Double.parseDouble(bundle.getString("longitude"));
                    String userName;
                    String phoneNumber;
                    serviceName = ServicesCustomAdapter.serviceModelListFiltered.get(position).getServiceName();
                    //Toast.makeText(getContext(), "Brand: " + brandName + "\nService: " + serviceName +"\n" + "LatLong: " + latitude + " , " + longitude, Toast.LENGTH_LONG).show();
                    openDialog();
                }
            }
        });
    }

    public void openDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("Brand and Service selected !")
                .setMessage("You have selected brand " + brandName + " and service " + serviceName + ". Do you want to book appointment?")
                .setIcon(R.drawable.ic_baseline_check)
                .setPositiveButton("Book", null)
                .setNegativeButton("Cancel", null)
                .show();
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // UPLOAD DATA TO SERVER
                Toast.makeText(getContext(), "Appointment Fixed", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.serachview_menu, menu);
        searchItem = menu.findItem(R.id.action_search);
        androidx.appcompat.widget.SearchView  searchView = (androidx.appcompat.widget.SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setQueryHint("Search...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                customAdapter.getFilter().filter(newText);
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            return true;
        }
        return true;
    }
}