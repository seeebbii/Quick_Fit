package com.example.quickfit.Brands;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.GridView;
import android.widget.Toast;

import com.example.quickfit.DashboardActivity;
import com.example.quickfit.R;
import com.example.quickfit.Services.ServiceFragment;

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
        brands = new ArrayList<BrandItemsModel>();
        BrandItemsModel object1 = new BrandItemsModel(R.drawable.bmw, "bmw");
        BrandItemsModel object2 = new BrandItemsModel(R.drawable.mercedes, "mercedes");
        brands.add(object1);
        brands.add(object1);
        brands.add(object1);
        brands.add(object2);
        brands.add(object2);
        brands.add(object2);
        brands.add(object2);


        customAdapter = new BrandsCustomAdapter(brands, getContext());
        gridView.setAdapter(customAdapter);
        // Refreshing data
        customAdapter.notifyDataSetChanged();

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
}