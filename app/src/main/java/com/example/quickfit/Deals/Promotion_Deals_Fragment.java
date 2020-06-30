package com.example.quickfit.Deals;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.quickfit.R;

import java.util.ArrayList;

public class Promotion_Deals_Fragment extends Fragment {

    ListView commentsListView;
    ArrayList<Offers_Model> list;
    Button offerDetaisBtn, availOfferBtn;

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
        Offers_Model object = new Offers_Model("Haseeb", "BMW", "Service", "03134495174", "25% OFF!", null);


        list.add(object);
        list.add(object);
        list.add(object);
        list.add(object);
        list.add(object);
        list.add(object);
        list.add(object);
        list.add(object);


        OfferCustomAdapter adapter = new OfferCustomAdapter(getContext(), list);
        commentsListView.setAdapter(adapter);

    }
}