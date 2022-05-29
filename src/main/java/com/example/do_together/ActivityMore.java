package com.example.do_together;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.do_together.db.NewData;
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
import java.util.HashMap;
import java.util.Map;

public class ActivityMore extends AppCompatActivity {
EditText nameM, descM, doneM;
TextView dateM, toM, fromM;
private DatabaseReference mDatabase;
CardView cd;
String TEAM;
String i;
boolean b=true;
int status;
TextView st;
LinearLayout lr;
ImageView img;
String more1, more2, more3;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        more1= getResources().getString(R.string.more1);
        more2=getResources().getString(R.string.more2);
        more3=getResources().getString(R.string.more3);

        FileInputStream fin = null;
        try{
            fin = openFileInput(LoginActivity.FILE_TEAM);
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            TEAM = new String(bytes);
        }
        catch (FileNotFoundException e) {}
        catch (IOException e) {}

        nameM=findViewById(R.id.nameM);
        descM=findViewById(R.id.descM);
        dateM=findViewById(R.id.dateM);
        toM=findViewById(R.id.toM);
        fromM=findViewById(R.id.FromM);
        doneM = findViewById(R.id.result);
        cd = findViewById(R.id.cardStatus);

        mDatabase = FirebaseDatabase.getInstance().getReference(TEAM).child(MainActivity.TASK_KEY);

        String name = getIntent().getStringExtra("title");
        String desc = getIntent().getStringExtra("desc");
        String date = getIntent().getStringExtra("date");
        String from = getIntent().getStringExtra("from");
        String to = getIntent().getStringExtra("to");
        String stat = getIntent().getStringExtra("status");
        String done = getIntent().getStringExtra("done");
        i = getIntent().getStringExtra("id");


        DatabaseReference UsersDataBase = FirebaseDatabase.getInstance().getReference(TEAM).child(LoginActivity.USERS_KEY);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        assert firebaseUser != null;
        Query UserQuery = UsersDataBase.orderByChild("email").equalTo(firebaseUser.getEmail());
        UserQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    String nam = String.valueOf(ds.child("name").getValue());
                    if (to.equals(nam) && from.equals(nam)){
                        nameM.setFocusable(true);
                        descM.setFocusable(true);
                        cd.setEnabled(true);
                    }
                    else if (to.equals(nam)){
                        nameM.setFocusable(false);
                        descM.setFocusable(false);
                    }
                    else {
                        cd.setEnabled(false);
                    }
                    if (status==0 && to.equals(nam)) {
                        status=1;
                        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot ds: snapshot.getChildren()){
                                    NewData data = ds.getValue(NewData.class);
                                    if ((data.id).equals(i)){
                                        Map<String,Object> map = new HashMap<>();
                                        map.put("status",String.valueOf(status));
                                        mDatabase.child(ds.getKey()).updateChildren(map);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        if (desc==" ") desc=null;
        if (done==" ") done=null;
        status=Integer.parseInt(stat);
        if (status==3) doneM.setFocusable(false);
        nameM.setText(name);
        descM.setText(desc);
        dateM.setText(more1+date);
        toM.setText(more2+to);
        fromM.setText(more3+from);
        doneM.setText(done);

        lr = findViewById(R.id.statusLin);
        st = findViewById(R.id.statusText);
        img = findViewById(R.id.statusImg);
        onCard();

        cd.setOnClickListener(v -> {
            if (status<4){
                Vibrator vibrator;
                vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(400);
                if (status!=3) status++;
                onCard();
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds: snapshot.getChildren()){
                            NewData data = ds.getValue(NewData.class);
                            if ((data.id).equals(i)){
                                Map<String,Object> map = new HashMap<>();
                                map.put("status",String.valueOf(status));
                                mDatabase.child(ds.getKey()).updateChildren(map);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        String s = getResources().getString(R.string.delete1);
        menu.add(s);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String s = getIntent().getStringExtra("id");
        switch (item.getItemId()) {
            case android.R.id.home:
                b=true;
                this.finish();
                return true;
            case 0:
                b=false;
                Query ItemQuery = mDatabase.orderByChild("id").equalTo(s);
                ItemQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            ds.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                this.finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (b){
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds: snapshot.getChildren()){
                        NewData data = ds.getValue(NewData.class);
                        if ((data.id).equals(i)){
                            String name = String.valueOf(nameM.getText()).trim(),
                                    desc= String.valueOf(descM.getText()).trim(),
                                    done = String.valueOf(doneM.getText()).trim();
                            if (desc.isEmpty()) desc=" ";
                            if (done.isEmpty()) done=" ";
                            Map<String,Object> map = new HashMap<>();
                            map.put("title",name);
                            map.put("desc",desc);
                            map.put("done",done);
                            mDatabase.child(ds.getKey()).updateChildren(map);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    public void onCard(){
        if (status==1 || status==0){
            lr.setBackgroundColor(getResources().getColor(R.color.status0));
            cd.setCardBackgroundColor(getResources().getColor(R.color.status0text));
            st.setTextColor(getResources().getColor(R.color.status0text));
            st.setText(R.string.status1);
            img.setImageDrawable(getResources().getDrawable(R.drawable.ic_status0));
        }
        else if (status==2){
            lr.setBackgroundColor(getResources().getColor(R.color.status1));
            cd.setCardBackgroundColor(getResources().getColor(R.color.status1text));
            st.setTextColor(getResources().getColor(R.color.status1text));
            st.setText(R.string.status2);
            img.setImageDrawable(getResources().getDrawable(R.drawable.ic_status1));
        }
        else if (status==3){
            doneM.setFocusable(false);
            lr.setBackgroundColor(getResources().getColor(R.color.status2));
            cd.setCardBackgroundColor(getResources().getColor(R.color.status2text));
            st.setTextColor(getResources().getColor(R.color.status2text));
            st.setText(R.string.status3);
            img.setImageDrawable(getResources().getDrawable(R.drawable.ic_status2));
        }
    }
}