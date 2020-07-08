package com.example.quickfit.ProfileSettings;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quickfit.DashboardActivity;
import com.example.quickfit.MainActivity;
import com.example.quickfit.R;
import com.example.quickfit.SharedPref;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    ImageView userImage;
    TextView id, name, email, statusCode, phone;
    Button btnLogOut;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        userImage = view.findViewById(R.id.userImage);
        btnLogOut = view.findViewById(R.id.btnLogOut);

        String url = DashboardActivity.CURRENT_USER.getUserImageUrl();
        url = url.replace(" ", "%20");

        id = view.findViewById(R.id.userId);
        name = view.findViewById(R.id.userName);
        email = view.findViewById(R.id.userEmail);
        statusCode = view.findViewById(R.id.userStatusCode);
        phone = view.findViewById(R.id.userPhone);

        Picasso.get()
                .load("http://sania.co.uk/quick_fix/"+url)
                .into(userImage);

        id.setText(DashboardActivity.CURRENT_USER.getId()+"");
        name.setText(DashboardActivity.CURRENT_USER.getName());
        email.setText(DashboardActivity.CURRENT_USER.getEmail());
        statusCode.setText(DashboardActivity.CURRENT_USER.getStatusCode());
        phone.setText(DashboardActivity.CURRENT_USER.getPhone());

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPref.savePreferencesBoolean("isLoggedIn", false, getContext());
                startActivity(new Intent(getContext(), MainActivity.class));
                getActivity().finish();
            }
        });


    }
}