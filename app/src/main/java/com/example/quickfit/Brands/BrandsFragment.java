package com.example.quickfit.Brands;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.GridView;
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
import com.example.quickfit.DashboardActivity;
import com.example.quickfit.R;
import com.example.quickfit.Services.ServiceFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public static MenuItem searchItem;
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

        customAdapter = new BrandsCustomAdapter(brands, getContext());

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                searchItem.collapseActionView();
                // BRANDS MODEL LIST FILTERED IS FROM BRAND CUSTOM ADAPTER MADE STATIC
                String brandName = BrandsCustomAdapter.brandModelListFiltered.get(position).getBrandName();
                int brandId = BrandsCustomAdapter.brandModelListFiltered.get(position).getId();

                // Passing data to Service Fragment
                Bundle bundle = new Bundle();
                bundle.putString("brandId", brandId+"");
                bundle.putString("selected_brand_name", brandName+"");
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                ServiceFragment serviceFragment = new ServiceFragment();
                serviceFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_container,serviceFragment).commit();
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        // scroll speed decreases as friction increases. a value of 2 worked
        // well in an emulator; you need to test it on a real device
        gridView.setFriction(ViewConfiguration.getScrollFriction() * 10);
    }

    public void parseJson() {

        StringRequest postRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                int id;
                                String brandName;
                                String brandImage;
                                JSONObject brand = jsonArray.getJSONObject(i);

                                brandName = brand.getString("name");
                                String substring="";
                                for(int j=0; j < brandName.length(); j++){
                                    if(brandName.charAt(j) != '.'){
                                        substring += brandName.charAt(j);
                                    }else if(brandName.charAt(j) == '.'){
                                        break;
                                    }
                                }
                                id = brand.getInt("id");
                                brandImage = brand.getString("image_url");
                                BrandItemsModel object = new BrandItemsModel(brandImage, substring, id);
                                brands.add(object);
                            }
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    gridView.setAdapter(customAdapter);
                                    // Refreshing data
                                    customAdapter.notifyDataSetChanged();
                                }
                            });
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


        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                8000,
                2,
                2));

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(postRequest);
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
    public void onResume() {
        super.onResume();
        new NetworkTask().execute();
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