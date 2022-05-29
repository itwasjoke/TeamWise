package com.example.do_together;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.example.do_together.db.NewType;
import com.example.do_together.db.NewUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class ActivityAdd extends AppCompatActivity {
Button _add, _time;
private DatabaseReference rDatabase;
private DatabaseReference TypeDatabase;
EditText _impName, _impDes;
TextView  _txtD, txtDESC, txtNAM;
Spinner sp1, sp2;
String txt1, txt2, txt3, txt4, txt5, txt6;
String TEAM;


Calendar dateAndTime = Calendar.getInstance();
String day, month1;
    int mYear, mMonth, mDay;
    private boolean isEditState = true;
    private boolean tx1 = false;

    String addN, type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long[] pattern = {0, 300, 0};
        setContentView(R.layout.activity_add);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        addN=getResources().getString(R.string.addN);
        type=getResources().getString(R.string.type);

        FileInputStream fin = null;
        try{
            fin = openFileInput(LoginActivity.FILE_TEAM);
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            TEAM = new String(bytes);
        }
        catch (FileNotFoundException e) {}
        catch (IOException e) {}



        _impName = (EditText)findViewById(R.id.impName);
        _impName.setText(null);
        txtNAM = findViewById(R.id.nametxt);
        txtDESC = findViewById(R.id.desctxt);
        _impDes = (EditText) findViewById(R.id.impDes);
        _add = (Button)findViewById(R.id.btnAdd);
        sp1 = findViewById(R.id.spinType);
        sp2 = findViewById(R.id.spinPeo);
        _time = findViewById(R.id.btnDate);
        _txtD = findViewById(R.id.txtDate);
        _add.setBackgroundColor(getResources().getColor(R.color.add3));



        rDatabase = FirebaseDatabase.getInstance().getReference(TEAM).child(LoginActivity.USERS_KEY);
        TypeDatabase = FirebaseDatabase.getInstance().getReference(TEAM).child(LoginActivity.TYPES_KEY);
        getFromFire2();

        setInitialDateTime();
        getMyIntents();
        _time.setOnClickListener(v -> {
            setTime(_txtD);
            setDate(_txtD);
        });
        _impName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                txt1=_impName.getText().toString();
                if (!TextUtils.isEmpty(txt1)) tx1=true;
                if (tx1==false){
                    _add.setBackgroundColor(getResources().getColor(R.color.add4));
                }
                else _add.setBackgroundColor(getResources().getColor(R.color.add5));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        _add.setOnClickListener(v -> {
            Intent answer = new Intent();
            txt1=_impName.getText().toString();
            txt2=_impDes.getText().toString();
            txt1 = txt1.trim();
            txt2 = txt2.trim();
            txt3 = txt3.trim();
            txt5 = txt5.trim();
            if (TextUtils.isEmpty(txt2)) txt2=" ";
            if (TextUtils.isEmpty(txt1)) tx1=false;

            txt4 = String.valueOf(sp2.getSelectedItem());
            txt6 = String.valueOf(sp1.getSelectedItem());
            txt4 = txt4.trim();

            if (tx1==false){
                Toast.makeText(ActivityAdd.this, addN, Toast.LENGTH_SHORT).show();
            }
            else
            {
                answer.putExtra("txt1", txt1);
                answer.putExtra("txt2", txt2);
                answer.putExtra("txt3", txt3);
                answer.putExtra("txt4", txt4);
                answer.putExtra("txt5", txt5);
                answer.putExtra("txt6", txt6);
                setResult(RESULT_OK, answer);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getMyIntents(){
        Intent i = getIntent();
        if (i != null){

            if (!isEditState){
                _add.setText(R.string.DONE);
                _add.setClickable(false);
                _time.setClickable(false);
                _impDes.setRawInputType(0x00000000);
                _impName.setRawInputType(0x00000000);
            }
        }
    }
    // отображаем диалоговое окно для выбора даты
    public void setDate(View v) {
        new DatePickerDialog(ActivityAdd.this, d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    // отображаем диалоговое окно для выбора времени
    public void setTime(View v) {
        new TimePickerDialog(ActivityAdd.this, t,
                dateAndTime.get(Calendar.HOUR_OF_DAY),
                dateAndTime.get(Calendar.MINUTE), true)
                .show();
    }
    // установка начальных даты и времени
    private void setInitialDateTime() {

        _txtD.setText(DateUtils.formatDateTime(this,
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR
                        | DateUtils.FORMAT_SHOW_TIME));

                txt3 = DateUtils.formatDateTime(this,
                        dateAndTime.getTimeInMillis(),
                        DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR
                                | DateUtils.FORMAT_SHOW_TIME);
    }
    // установка обработчика выбора времени
    TimePickerDialog.OnTimeSetListener t=new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateAndTime.set(Calendar.MINUTE, minute);
            setInitialDateTime();
        }
    };

    // установка обработчика выбора даты
    DatePickerDialog.OnDateSetListener d=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
        }
    };

    private void getFromFire2(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        List<String> tempList = new ArrayList<>();
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(ActivityAdd.this, android.R.layout.simple_spinner_item, tempList);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp2.setAdapter(adapter1);
        List<String> tempList2 = new ArrayList<>();
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(ActivityAdd.this, android.R.layout.simple_spinner_item, tempList2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp1.setAdapter(adapter2);
        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    NewUser newUser = ds.getValue(NewUser.class);
                    assert newUser != null;
                    tempList.add(newUser.name);
                    assert firebaseUser != null;
                    if (firebaseUser.getEmail().equals(newUser.email)) txt5=newUser.name;
                }
                adapter1.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        rDatabase.addListenerForSingleValueEvent(vListener);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tempList2.add(type);
                for (DataSnapshot ds: snapshot.getChildren()){
                    NewType newType = ds.getValue(NewType.class);
                    assert newType != null;
                    tempList2.add(newType.type);
                }
                adapter2.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        TypeDatabase.addListenerForSingleValueEvent(valueEventListener);
    }
}