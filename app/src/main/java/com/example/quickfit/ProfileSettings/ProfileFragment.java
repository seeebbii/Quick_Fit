package com.example.quickfit.ProfileSettings;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.quickfit.DashboardActivity;
import com.example.quickfit.MainActivity;
import com.example.quickfit.R;
import com.example.quickfit.Services.SetUserRequest;
import com.example.quickfit.SharedPref;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {

    ImageView userImage;
    TextView id, statusCode;
    EditText name, email, phone;
    Button btnLogOut, updateProfileBtn;
    private final int IMAGE_REQUEST = 1;
    Bitmap userImageBitmap;
    String url;
    String update_profile_url = "http://sania.co.uk/quick_fix/updateProfile.php";

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

        url = SharedPref.getPreferences("user_image", getContext());
        url = url.replace(" ", "%20");

        id = view.findViewById(R.id.userId);
        name = view.findViewById(R.id.userName);
        email = view.findViewById(R.id.userEmail);
        statusCode = view.findViewById(R.id.userStatusCode);
        phone = view.findViewById(R.id.userPhone);
        updateProfileBtn = view.findViewById(R.id.updateProfileBtn);

        Picasso.get()
                .load("http://sania.co.uk/quick_fix/"+url)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        userImageBitmap = bitmap;
                        userImage.setImageBitmap(bitmap);
                    }
                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    }
                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });



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

        updateProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateProfile();
            }
        });

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
    }

    private void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data!=null){
            Uri path = data.getData();
            try {
                userImageBitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), path);
                userImage.setImageBitmap(userImageBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void openDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("Profile updated successfully!")
                .setMessage("Changes will apply once you log out and login again.")
                .setPositiveButton("Okay", null)
                .show();
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void updateProfile(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, update_profile_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("Profile updated successfully")){
                    openDialog();
                }else {
                    Toast.makeText(getContext(), "Oops!, Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
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
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("image", imageToString(userImageBitmap));
                params.put("name", name.getText().toString());
                params.put("email", email.getText().toString());
                params.put("phone", phone.getText().toString());
                params.put("id", DashboardActivity.CURRENT_USER.getId()+"");
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                8000,
                2,
                2));

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(stringRequest);

    }

    private String imageToString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }

}