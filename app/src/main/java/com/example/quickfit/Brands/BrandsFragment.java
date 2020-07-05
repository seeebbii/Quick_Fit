package com.example.quickfit.Brands;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.quickfit.DashboardActivity;
import com.example.quickfit.R;
import com.example.quickfit.Services.ServiceFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class BrandsFragment extends Fragment {

    List<BrandItemsModel> brands;
    GridView gridView;
    BrandsCustomAdapter customAdapter;
    MenuItem searchItem;
    RequestQueue mQueue;
    final String URL = "http://sania.co.uk/quick_fix/brands_api.php";
    ProgressDialog mProgressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_brands, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gridView = view.findViewById(R.id.gridView);
        mQueue = Volley.newRequestQueue(getContext());
        brands = new ArrayList<BrandItemsModel>();

        new NetworkTask().execute();

        customAdapter = new BrandsCustomAdapter(brands, getContext());
        gridView.setAdapter(customAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                searchItem.collapseActionView();
                // BRANDS MODEL LIST FILTERED IS FROM BRAND CUSTOM ADAPTER MADE STATIC
                String brandName = BrandsCustomAdapter.brandModelListFiltered.get(position).getBrandName();
                double latitude = DashboardActivity.LATITUDE;
                double longitude = DashboardActivity.LONGITUDE;
                String userName;
                String phoneNumber;

                // Passing data to Service Fragment
                Bundle bundle = new Bundle();
                bundle.putString("BrandName", brandName);
                bundle.putString("latitude", String.valueOf(latitude));
                bundle.putString("longitude", String.valueOf(longitude));
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                ServiceFragment serviceFragment = new ServiceFragment();
                serviceFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_container,serviceFragment).commit();
            }
        });
    }


    void parseJson(){
        // REQUESTING BRAND OBJECT USING VOLLEY
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i= 0; i < response.length(); i++){
                    try {
                        int id;
                        String brandName;
                        String brandImage;
                        JSONObject brand = response.getJSONObject(i);

                        brandName = brand.getString("name");
                        id = brand.getInt("id");
                        brandImage = brand.getString("image_url");
                        BrandItemsModel object = new BrandItemsModel(brandImage, brandName, id);
                        brands.add(object);
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
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.serachview_menu, menu);
        searchItem = menu.findItem(R.id.action_search);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) searchItem.getActionView();
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

    class NetworkTask extends AsyncTask<Void, String, String>{

        @Override
        protected String doInBackground(Void... voids) {
            parseJson();

            try {
                Thread.sleep(500);
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