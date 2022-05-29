package com.example.do_together;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.do_together.adapter.ItemAdapter2;
import com.example.do_together.adapter.itemU3;
import com.example.do_together.db.NewPeo;
import com.example.do_together.db.NewTeam;
import com.example.do_together.db.NewType;
import com.example.do_together.db.NewUser;
import com.example.do_together.db.newId;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    EditText name, pass, userE, teamE, name2, descText;
    TextView txt1, txt2, txtEm, openText;
    CardView cv2;
    Button btn1;
    private FirebaseAuth mAuth;
    private DatabaseReference fDatabase;
    public static final String TEAM_KEY = "Team";
    public static final String TYPES_KEY = "Types";
    public static final String USERS_KEY = "Users";
    public static final String ABOUT_KEY = "About";
    public static final String FILE_TEAM = "team.txt";
    String team1;
    String nam, pas, user1, team;
    boolean sig = true;
    Intent intent;
    Button btn, btn2, btnEnd;
    CheckBox checkBox;
    ProgressBar progressBar;
    Button go;
    LinearLayout inr, inr2, inr3;
    String log1, log2, log3, log4, log5, log6, log7;

    //...

    EditText nameTeam, nameTeam2, descTeam, typeTeam, typeTeam2;
    TextView lst;
    TextView lst2;
    ArrayList<String> list = new ArrayList<>();
    ArrayList<String> list2 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        checkBox = findViewById(R.id.teamIF);
        log1 = getResources().getString(R.string.login1);
        log2 = getResources().getString(R.string.login2);
        log3 = getResources().getString(R.string.login3);
        log4 = getResources().getString(R.string.login4);
        log5 = getResources().getString(R.string.login5);
        log6 = getResources().getString(R.string.login6);
        log7 = getResources().getString(R.string.login7);
        lst = findViewById(R.id.list);
        descText = findViewById(R.id.descText);
        lst2 = findViewById(R.id.list2);
        typeTeam = findViewById(R.id.teamType);
        typeTeam2 = findViewById(R.id.teamType2);
        nameTeam = findViewById(R.id.teamName);
        nameTeam2 = findViewById(R.id.teamName2);
        descTeam = findViewById(R.id.teamDesc);
        openText = findViewById(R.id.openText);
        sig = true;
        txtEm = findViewById(R.id.txtEm);
        txtEm.setVisibility(View.GONE);
        cv2 = findViewById(R.id.cv2);
        userE = findViewById(R.id.nameText);
        teamE = findViewById(R.id.teamText);
        name2 = findViewById(R.id.firstNameText);
        btn1 = findViewById(R.id.signBTN);
        btn1.setText(R.string.sign);
        name = findViewById(R.id.emailText);
        pass = findViewById(R.id.passText);
        mAuth = FirebaseAuth.getInstance();
        txt1 = findViewById(R.id.regist);
        txt2 = findViewById(R.id.textNOT);
        intent = new Intent(getApplicationContext(), MainActivity.class);
        checkBox.setVisibility(View.GONE);
        txt1.setVisibility(View.VISIBLE);
        txt2.setVisibility(View.VISIBLE);
        btn1.setVisibility(View.VISIBLE);
        name.setVisibility(View.VISIBLE);
        pass.setVisibility(View.VISIBLE);

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) cv2.setVisibility(View.VISIBLE);
            else cv2.setVisibility(View.GONE);
        });
    }

    public void onClickSignUp(View view) {
        if (sig) {
            cv2.setVisibility(View.VISIBLE);
            checkBox.setVisibility(View.VISIBLE);
            txt1.setText(R.string.sign);
            txt2.setVisibility(View.GONE);
            btn1.setText(R.string.reg2);
            sig = false;
        } else {
            checkBox.setVisibility(View.GONE);
            txt1.setText(R.string.reg2);
            txt2.setVisibility(View.VISIBLE);
            btn1.setText(R.string.sign);
            sig = true;
        }


    }

    public void onClickSignIn(View view) {
        progressBar = findViewById(R.id.progressBar2);
        inr3 = findViewById(R.id.lay_create);
        progressBar.setVisibility(View.VISIBLE);
        nam = name.getText().toString().trim();
        pas = pass.getText().toString().trim();
        team = teamE.getText().toString().trim();
        boolean t1 = !TextUtils.isEmpty(nam),
                t2 = !TextUtils.isEmpty(pas),
                t3 = !TextUtils.isEmpty(team),
                t4 = !TextUtils.isEmpty(name2.getText().toString()) && !TextUtils.isEmpty(userE.getText().toString());
        boolean tPas = pas.length() >= 6;
        if (sig) {
            mAuth.signInWithEmailAndPassword(nam, pas).addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    FileOutputStream fos = null;
                    try {
                        fos = openFileOutput(FILE_TEAM, MODE_PRIVATE);
                        fos.write(team.getBytes());
                    } catch (FileNotFoundException e) {
                        //
                    } catch (IOException e) {
                        //
                    } finally {
                        try {
                            if (fos != null) {
                                fos.close();
                            }
                        } catch (IOException e) {
                        }
                    }
                    progressBar.setVisibility(View.GONE);
                    startActivity(intent);
                    finish();
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, log1, Toast.LENGTH_SHORT).show();
                }

            });
        } else {
            mAuth.createUserWithEmailAndPassword(nam, pas).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    sendEmail();
                    FirebaseUser user = mAuth.getCurrentUser();
                    assert user != null;
                    if (user.isEmailVerified()) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, log3, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, log4, Toast.LENGTH_SHORT).show();
                }

            });
        }
    }

    private void sendEmail() {
        progressBar.setVisibility(View.GONE);
        FirebaseUser user = mAuth.getCurrentUser();
        checkBox = findViewById(R.id.teamIF);
        inr = findViewById(R.id.lay_includ);
        inr2 = findViewById(R.id.lay_not);
        assert user != null;

        //user.isEmailVerified()
        if (user.isEmailVerified()){
            
        }
        user.sendEmailVerification().addOnCompleteListener(task -> {
            if (!checkBox.isChecked()) {
                CardView cardView = findViewById(R.id.cardPost);
                TextView textView = findViewById(R.id.selectPost);
                cardView.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
            }
            CardView cv4 = findViewById(R.id.cv4), cv5 = findViewById(R.id.cv5), cv6 = findViewById(R.id.cv6);
            cv4.setVisibility(View.VISIBLE);
            cv2.setVisibility(View.GONE);
            cv5.setVisibility(View.GONE);
            cv6.setVisibility(View.GONE);
            Button btnSign = findViewById(R.id.signBTN);
            btnSign.setVisibility(View.GONE);
            checkBox.setVisibility(View.GONE);
            EditText editText = findViewById(R.id.teamText);
            String s = String.valueOf(editText.getText());
            fDatabase = FirebaseDatabase.getInstance().getReference(s).child(TEAM_KEY);
            Spinner sp = findViewById(R.id.spinTypes);

            ArrayList<String> arrayList = new ArrayList<>();
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
            sp.setAdapter(arrayAdapter);
            fDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
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
                    Toast.makeText(LoginActivity.this, log6, Toast.LENGTH_SHORT).show();
                }
            });

            go = findViewById(R.id.btnGO);

            go.setOnClickListener(v -> {
                String nam = String.valueOf(name2.getText()).trim() + " " + String.valueOf(userE.getText()).trim();
                String desccc = String.valueOf(descText.getText()).trim();
                if (checkBox.isChecked()) {
                    fDatabase = FirebaseDatabase.getInstance().getReference(s).child(USERS_KEY);
                    team1 = String.valueOf(sp.getSelectedItem());
                    NewUser userr = new NewUser(nam, team1, "0", String.valueOf(user.getEmail()), desccc);
                    fDatabase.push().setValue(userr);
                    FileOutputStream fos = null;
                    try {
                        fos = openFileOutput(FILE_TEAM, MODE_PRIVATE);
                        fos.write(s.getBytes());
                    } catch (FileNotFoundException e) {
                    } catch (IOException e) {
                    } finally {
                        try {
                            if (fos != null) {
                                fos.close();
                            }
                        } catch (IOException e) {
                        }
                    }
                    startActivity(intent);
                    finish();
                } else {
                    inr.setVisibility(View.GONE);
                    btn = findViewById(R.id.plusType);
                    btn2 = findViewById(R.id.plusType2);
                    btnEnd = findViewById(R.id.createTeam);


                    txtEm.setVisibility(View.GONE);
                    inr3.setVisibility(View.VISIBLE);

                    openText.setText(R.string.team5);


                    btn.setOnClickListener(v1 -> {
                        String n = String.valueOf(typeTeam.getText());
                        list.add(0, n);
                        lst.append(n+"\n");
                        typeTeam.setText("");
                    });
                    btn2.setOnClickListener(v12 -> {
                        String n2 = String.valueOf(typeTeam2.getText());
                        list2.add(0, n2);
                        lst2.append(n2+"\n");
                        typeTeam2.setText("");
                    });

                    btnEnd.setOnClickListener(v13 -> {
                        String name = String.valueOf(nameTeam.getText()),
                                url = String.valueOf(nameTeam2.getText()),
                                desc = String.valueOf(descTeam.getText());
                        user1 = name2.getText().toString().trim() + " " + userE.getText().toString().trim();
                        NewTeam newTeam = new NewTeam(name, desc);
                        FirebaseUser userEmail = mAuth.getCurrentUser();
                        assert userEmail != null;
                        NewUser newUser = new NewUser(nam, log7, "1", userEmail.getEmail(), desccc);
                        fDatabase = FirebaseDatabase.getInstance().getReference(url).child(ABOUT_KEY);
                        fDatabase.push().setValue(newTeam);
                        fDatabase = FirebaseDatabase.getInstance().getReference(url).child(USERS_KEY);
                        fDatabase.push().setValue(newUser);
                        fDatabase = FirebaseDatabase.getInstance().getReference(url).child(TEAM_KEY);
                        for (int i = 0; i < list.size(); i++) {
                            NewType type = new NewType(list.get(i), String.valueOf(i));
                            fDatabase.push().setValue(type);
                        }
                        fDatabase = FirebaseDatabase.getInstance().getReference(url).child(TYPES_KEY);
                        for (int i = 0; i < list2.size(); i++) {
                            NewPeo type = new NewPeo(list2.get(i), String.valueOf(i));
                            fDatabase.push().setValue(type);
                        }
                        fDatabase =  FirebaseDatabase.getInstance().getReference(url).child(MainActivity.ID_KEY);
                        newId newid = new newId("0");
                        fDatabase.push().setValue(newid);
                        FileOutputStream fos = null;
                        try {
                            fos = openFileOutput(FILE_TEAM, MODE_PRIVATE);
                            fos.write(url.getBytes());
                        } catch (FileNotFoundException e) {
                        } catch (IOException e) {
                        } finally {
                            try {
                                if (fos != null) {
                                    fos.close();
                                }
                            } catch (IOException e) {
                            }
                        }
                        startActivity(intent);
                        finish();
                    });

                }
                progressBar.setVisibility(View.GONE);
                txtEm.setVisibility(View.VISIBLE);
                inr2.setVisibility(View.GONE);

            });
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(intent);
        }
    }
}