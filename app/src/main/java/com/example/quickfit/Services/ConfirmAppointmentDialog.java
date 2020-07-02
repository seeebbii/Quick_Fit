package com.example.quickfit.Services;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.example.quickfit.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class ConfirmAppointmentDialog extends AppCompatDialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
       AlertDialog.Builder builder =  new AlertDialog.Builder(getActivity());
       builder.setTitle("Brand and Service selected !").setMessage("You have selected brand " + ServiceFragment.brandName + " and service " + ServiceFragment.serviceName + ". Do you want to book appointment?")
               .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                     @Override
                   public void onClick(DialogInterface dialogInterface, int i) {

                   }
               })
               .setPositiveButton("Book", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {

                   }
               });
       return builder.create();
    }
}
