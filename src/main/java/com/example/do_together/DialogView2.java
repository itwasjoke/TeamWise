package com.example.do_together;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.do_together.db.NewType;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DialogView2 extends DialogFragment {

    DatabaseReference TypesDataBase;
    String sort, cancel;

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        sort=getResources().getString(R.string.dia1);
        cancel=getResources().getString(R.string.dia2);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_sort, null);
        builder.setView(view);
        Spinner sp1 = view.findViewById(R.id.sort1);
        Spinner sp2 = view.findViewById(R.id.sort2);
        String[] templist1= ((MainActivity)getActivity()).getFromFireReturn();
        ArrayAdapter<String> adapterMy = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, templist1);
        adapterMy.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp1.setAdapter(adapterMy);
        builder
                .setPositiveButton(sort, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((MainActivity)getActivity()).getFromFire(String.valueOf(sp1.getSelectedItem()).trim(),
                                sp2.getSelectedItemPosition());
                    }
                })
                .setNegativeButton(cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        return builder.create();
    }
}
