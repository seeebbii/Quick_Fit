package com.example.quickfit.Deals;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quickfit.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class OfferCustomAdapter extends ArrayAdapter<Offers_Model> {

    private final Context context;
    private final ArrayList<Offers_Model> offers;

    public OfferCustomAdapter(@NonNull Context context, ArrayList<Offers_Model> list) {
        super(context, R.layout.offers_row_model, list);
        this.context = context;
        this.offers = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowViewer = layoutInflater.inflate(R.layout.offers_row_model, parent, false);
        TextView userName = rowViewer.findViewById(R.id.offer_name);
        TextView brandName = rowViewer.findViewById(R.id.offer_brandsName);
        TextView serviceName = rowViewer.findViewById(R.id.offer_service);
        TextView phoneNumber = rowViewer.findViewById(R.id.offer_phoneNumber);
        TextView offerDetails = rowViewer.findViewById(R.id.offer_details);
        //ImageView userImage = rowViewer.findViewById(R.id.offer_Image);

        userName.setText(offers.get(position).getNameOfUser());
        brandName.setText(offers.get(position).getBrandName());
        serviceName.setText(offers.get(position).getServiceName());
        phoneNumber.setText(offers.get(position).getPhoneNumber());
        offerDetails.setText(offers.get(position).getOfferDetails());


        return rowViewer;
    }
}
