package com.example.quickfit.Deals;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quickfit.R;
import com.squareup.picasso.Picasso;

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
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowViewer = layoutInflater.inflate(R.layout.offers_row_model, parent, false);
        TextView userName = rowViewer.findViewById(R.id.offer_name);
        TextView brandName = rowViewer.findViewById(R.id.offer_brandsName);
        TextView serviceName = rowViewer.findViewById(R.id.offer_service);
        TextView validityTimer = rowViewer.findViewById(R.id.offer_validityTime);
        TextView offerDetails = rowViewer.findViewById(R.id.offer_details);
        Button offerDetailsBtn = rowViewer.findViewById(R.id.offer_detailsBtn);
        Button availOfferBtn = rowViewer.findViewById(R.id.offer_availOffer);
        ImageView userImage = rowViewer.findViewById(R.id.offer_Image);

        String url =  offers.get(position).getImageUrl();;
        url = url.replace(" ", "%20");
        userName.setText(offers.get(position).getNameOfUser());
        brandName.setText(offers.get(position).getBrandName());
        serviceName.setText(offers.get(position).getServiceName());
        validityTimer.setText(offers.get(position).getValidityTime());
        offerDetails.setText(offers.get(position).getOfferDetails());

        Picasso.get()
                .load("http://sania.co.uk/quick_fix/"+url)
                .into(userImage);

        offerDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Display(position+"");
            }
        });

        availOfferBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Display(position+"");
            }
        });


        return rowViewer;
    }

    public void Display(String mssg){
        Toast.makeText(context,mssg, Toast.LENGTH_SHORT).show();
    }

}
