package com.example.do_together;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import org.jetbrains.annotations.NotNull;

public class DialogView extends DialogFragment {
    String add, cancel;
    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        add=getResources().getString(R.string.dia3);
        cancel=getResources().getString(R.string.dia2);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog, null);
        EditText text = view.findViewById(R.id.newElement);
        builder.setView(view);
        builder
                .setPositiveButton(add, (dialog, which) ->
                        ((MainActivity)getActivity()).setOnFire(String.valueOf(text.getText())))
                .setNegativeButton(cancel, (dialog, which) ->
                        dialog.cancel());
        return builder.create();
    }

}
