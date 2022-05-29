package com.example.do_together;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.do_together.db.NewData;
import com.example.do_together.db.NewPeo;
import com.example.do_together.db.NewUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SettingActivity extends AppCompatActivity {
    DatabaseReference UsersDataBase;
    EditText nam, desc;
    Spinner spTeam, spTheme, spLang;
    DatabaseReference TeamDatabase;
    String TEAM;
    boolean b=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        b=true;
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);



        nam = (EditText) findViewById(R.id.namePers);
        desc = (EditText) findViewById(R.id.descPers);
        spTeam = (Spinner) findViewById(R.id.spTeam);
        spTheme = (Spinner) findViewById(R.id.spTheme);
        spLang = (Spinner) findViewById(R.id.spLang);

        FileInputStream fin = null;
        try {
            fin = openFileInput(LoginActivity.FILE_TEAM);
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            TEAM = new String(bytes);
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }

        UsersDataBase = FirebaseDatabase.getInstance().getReference(TEAM).child(LoginActivity.USERS_KEY);
        TeamDatabase = FirebaseDatabase.getInstance().getReference(TEAM).child(LoginActivity.TEAM_KEY);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        assert firebaseUser != null;

        ArrayList<String> arrayList = new ArrayList<>();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        spTeam.setAdapter(arrayAdapter);
        TeamDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    NewPeo newPeo = ds.getValue(NewPeo.class);
                    assert newPeo != null;
                    arrayList.add(newPeo.type);
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        Query UserQuery = UsersDataBase.orderByChild("email").equalTo(firebaseUser.getEmail());
        UserQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String name = String.valueOf(ds.child("name").getValue());
                    String des = String.valueOf(ds.child("desc").getValue());
                    nam.setText(name);
                    desc.setText(des);
                    String post = String.valueOf(ds.child("team").getValue());
                    int pos = arrayAdapter.getPosition(post);
                    spTeam.setSelection(pos);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        spTheme.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==1) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    saveTheme(AppCompatDelegate.MODE_NIGHT_NO);
                }
                if (position==2) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    saveTheme(AppCompatDelegate.MODE_NIGHT_YES);
                }
                if (position==3) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                    saveTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spLang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position==1){
                        Locale myLocale = new Locale("ru");
                        Locale.setDefault(myLocale);
                        saveLang("ru");
                        android.content.res.Configuration config = new android.content.res.Configuration();
                        config.locale = myLocale;
                        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                    if (position==2){
                        Locale myLocale = new Locale("en");
                        Locale.setDefault(myLocale);
                        saveLang("en");
                        android.content.res.Configuration config = new android.content.res.Configuration();
                        config.locale = myLocale;
                        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void saveLang(String lang){
        String langPref = "Language";
        SharedPreferences prefs = getSharedPreferences("Language", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(langPref, lang);
        editor.apply();
    }
    private void saveTheme(int mode){
        String langPref = "Theme";
        SharedPreferences prefs = getSharedPreferences("Theme", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(langPref, String.valueOf(mode));
        editor.apply();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        assert firebaseUser != null;
        UsersDataBase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    NewUser data = ds.getValue(NewUser.class);
                    if ((data.email).equals(firebaseUser.getEmail())) {
                        String name = String.valueOf(nam.getText()).trim(),
                                des = String.valueOf(desc.getText()).trim(),
                                post = String.valueOf(spTeam.getSelectedItem()).trim();
                        if (des.isEmpty()) des = " ";
                        Map<String, Object> map = new HashMap<>();
                        map.put("name", name);
                        map.put("team", post);
                        map.put("desc", des);
                        UsersDataBase.child(ds.getKey()).updateChildren(map);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onBackPressed() {
        // do nothing
    }
}