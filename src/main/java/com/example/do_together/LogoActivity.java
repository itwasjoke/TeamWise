package com.example.do_together;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class LogoActivity extends AppCompatActivity {
private Animation anim;
private ImageView img;
private FirebaseAuth mAuth;
private boolean aunt= true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sp = getSharedPreferences("Theme", 0);
        String theme = sp.getString("Theme", String.valueOf(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM));
        AppCompatDelegate.setDefaultNightMode(Integer.parseInt(theme));
        super.onCreate(savedInstanceState);




        getSupportActionBar().hide();
        mAuth= FirebaseAuth.getInstance();
        setContentView(R.layout.activity_logo);
        img = findViewById(R.id.imgLogo);

        anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.alpha_anim);
        img.setAnimation(anim);
        img.startAnimation(anim);
        startMainAct();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    private void startMainAct(){
        new Thread(() -> {
            try {
                Thread.sleep(1700);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Intent intent;
            if (aunt) {
                intent = new Intent(getApplicationContext(), LoginActivity.class);
            }
            else {
                intent = new Intent(getApplicationContext(), MainActivity.class);
            }
            startActivity(intent);

            finish();
        }).start();
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!=null){
            aunt=false;
        }
    }
}