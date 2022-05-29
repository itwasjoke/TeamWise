package com.example.do_together;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.do_together.adapter.ItemAdapter;
import com.example.do_together.adapter.ItemAdapter2;
import com.example.do_together.adapter.SwipeAdapter;
import com.example.do_together.adapter.UserAdapter;
import com.example.do_together.adapter.itemU;
import com.example.do_together.adapter.itemU2;
import com.example.do_together.adapter.itemU3;
import com.example.do_together.db.NewData;
import com.example.do_together.db.NewPeo;
import com.example.do_together.db.NewTeam;
import com.example.do_together.db.NewType;
import com.example.do_together.db.NewUser;
import com.example.do_together.db.newId;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    RecyclerView _rec, _rec2;
    private ItemAdapter itemAdapter; //-adapter of list
    private DatabaseReference UsersDataBase; //-database
    private DatabaseReference TasksDataBase; //-database
    private DatabaseReference TeamDataBase; //-database
    private DatabaseReference TypesDataBase; //-database
    private DatabaseReference AboutDataBase; //-database
    private DatabaseReference IdDataBase; //-database
    private UserAdapter userAdapter;
    private ItemAdapter2 itemAdapter2;
    private ItemAdapter2 itemAdapter2_2;
    SwipeRefreshLayout swipeRefreshLayout;
    public boolean ref = true, newEl;
    DrawerLayout drawer;
    NavigationView navigationView;
    RecyclerView lstTypes, lstTeams;
    TextView addTypeTask, addTypeTeam;
    LinearLayout lnr1, lnr2, lnr3, lSort;
    private float downX;
    FloatingActionButton fab;
    LinearLayout linTasks;
    ScrollView TEAMLINEAR;
    String _txt1, _txt2, _txt3, _txt4, _txt5, _txt6;
    ProgressBar progressBar;
    String[] templist1;
    String TEAM;
    TableRow t1, t2, t3;
    View v1, v2, v3;
    ViewPager2 myViewPager2;
    String none, del, none2, invite1, invite2;
    ArrayList<String> arrayList;

    public boolean open1 = false, open2=false, open3=false;

    public static final String TASK_KEY = "Tasks";
    public static final String ID_KEY = "id";
    int ID=-1, idG=0, idH=0;



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sp = getSharedPreferences("Language", 0);
        String lang = sp.getString("Language", "en");
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(
                config,
                getResources().getDisplayMetrics()
        );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lSort = findViewById(R.id.sortLay);
        none =getResources().getString(R.string.type);
        del = getResources().getString(R.string.delete);
        none2 = getResources().getString(R.string.noneType);
        invite1 = getResources().getString(R.string.inv1);
        invite2 = getResources().getString(R.string.inv2);


        arrayList = new ArrayList<>();
        arrayList.add("0");
        arrayList.add("1");
        arrayList.add("2");
        FileInputStream fin = null;
        try{
            fin = openFileInput(LoginActivity.FILE_TEAM);
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            TEAM = new String(bytes);
        }
        catch (FileNotFoundException e) {}
        catch (IOException e) {}

        progressBar=findViewById(R.id.progressBar);
        TEAMLINEAR = findViewById(R.id.scroll);
        linTasks = findViewById(R.id.linTasks);

        TEAMLINEAR.setVisibility(View.GONE);

        swipeRefreshLayout = findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (ref) {
                FileInputStream fini = null;
                String list = null;
                try{
                    fini = openFileInput("sort.txt");
                    byte[] bytes = new byte[fini.available()];
                    fini.read(bytes);
                    list = new String(bytes);
                }
                catch (FileNotFoundException e) {}
                catch (IOException e) {}
                String[] sorting = list.substring(1,list.length()-1).split(",");
                int status = Integer.parseInt(sorting[1].trim());
                getFromFire(sorting[0].trim(), status);
            }
            else
                getFromFire1();
            swipeRefreshLayout.setRefreshing(false);
        });

        myViewPager2 = findViewById(R.id.pager);

        t1 = findViewById(R.id.row1);
        t2 = findViewById(R.id.row2);
        t3 = findViewById(R.id.row3);
        v1= findViewById(R.id.viewRec);
        v2= findViewById(R.id.viewSent);
        v3= findViewById(R.id.viewNotes);

        t1.setOnClickListener(v -> {
            myViewPager2.setCurrentItem(0, true);
            v1.setVisibility(View.VISIBLE);
            v2.setVisibility(View.INVISIBLE);
            v3.setVisibility(View.INVISIBLE);
        });
        t2.setOnClickListener(v -> {
            myViewPager2.setCurrentItem(1, true);
            v1.setVisibility(View.INVISIBLE);
            v2.setVisibility(View.VISIBLE);
            v3.setVisibility(View.INVISIBLE);
        });
        t3.setOnClickListener(v -> {
            myViewPager2.setCurrentItem(2, true);
            v1.setVisibility(View.INVISIBLE);
            v2.setVisibility(View.INVISIBLE);
            v3.setVisibility(View.VISIBLE);
        });

        myViewPager2.setPageTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                if (myViewPager2.getCurrentItem()==0) {
                    v1.setVisibility(View.VISIBLE);
                    v2.setVisibility(View.INVISIBLE);
                    v3.setVisibility(View.INVISIBLE);
                }
                if (myViewPager2.getCurrentItem()==1){
                    v1.setVisibility(View.INVISIBLE);
                    v2.setVisibility(View.VISIBLE);
                    v3.setVisibility(View.INVISIBLE);
                }
                if (myViewPager2.getCurrentItem()==2){
                    v1.setVisibility(View.INVISIBLE);
                    v2.setVisibility(View.INVISIBLE);
                    v3.setVisibility(View.VISIBLE);
                }
            }
        });

        //import data

        UsersDataBase = FirebaseDatabase.getInstance().getReference(TEAM).child(LoginActivity.USERS_KEY);
        TasksDataBase = FirebaseDatabase.getInstance().getReference(TEAM).child(TASK_KEY);
        TeamDataBase = FirebaseDatabase.getInstance().getReference(TEAM).child(LoginActivity.TEAM_KEY);
        TypesDataBase = FirebaseDatabase.getInstance().getReference(TEAM).child(LoginActivity.TYPES_KEY);
        AboutDataBase = FirebaseDatabase.getInstance().getReference(TEAM).child(LoginActivity.ABOUT_KEY);
        IdDataBase = FirebaseDatabase.getInstance().getReference(TEAM).child(ID_KEY);


        itemAdapter = new ItemAdapter(this, TEAM);
        itemAdapter2 = new ItemAdapter2(this);
        itemAdapter2_2 = new ItemAdapter2(this);
        getFromFire(none, 0);


        // work with adapter
        _rec2 = findViewById(R.id.rec2);


        userAdapter = new UserAdapter(this, TEAM);
        _rec2.setLayoutManager(new LinearLayoutManager(this));
        _rec2.setAdapter(userAdapter);

        _rec2.setVisibility(View.GONE);
        addTypeTeam = findViewById(R.id.addTypeTeam);
        addTypeTask = findViewById(R.id.addTypeTask);

        lstTeams = findViewById(R.id.TeamsList);
        lstTeams.setLayoutManager(new LinearLayoutManager(this));
        lstTeams.setAdapter(itemAdapter2);
        getItemTouchHelper2(true).attachToRecyclerView(lstTeams);



        lstTypes = findViewById(R.id.TypesList);
        lstTypes.setLayoutManager(new LinearLayoutManager(this));
        lstTypes.setAdapter(itemAdapter2_2);
        getItemTouchHelper2(false).attachToRecyclerView(lstTypes);

        lnr1 = findViewById(R.id.LinTypes);
        lnr2=findViewById(R.id.LinTeams);
        lnr3 = findViewById(R.id.LinUsers);
        lstTeams.setVisibility(View.GONE);
        lstTypes.setVisibility(View.GONE);

        addTypeTask.setOnClickListener(v -> {
            newEl=true;
            DialogView dialogView = new DialogView();
            FragmentManager manager = getSupportFragmentManager();
            dialogView.show(manager, "MyDialog");
        });
        addTypeTeam.setOnClickListener(v -> {
            newEl=false;
            DialogView dialogView = new DialogView();
            FragmentManager manager = getSupportFragmentManager();
            dialogView.show(manager, "MyDialog");
        });


        //menu
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ActivityAdd.class);
            startActivityForResult(intent, 1);
        });

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, 0, 0);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        _rec2.setNestedScrollingEnabled(false);
        openElement();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        assert firebaseUser != null;
        Query UserQuery = UsersDataBase.orderByChild("email").equalTo(firebaseUser.getEmail());
        UserQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    String root = String.valueOf(ds.child("root").getValue());
                    int rt = Integer.parseInt(root);
                    if (rt==0){
                        addTypeTeam.setEnabled(false);
                        addTypeTask.setEnabled(false);
                    }
                    else {
                        addTypeTeam.setEnabled(true);
                        addTypeTask.setEnabled(true);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                downX = event.getX();
            }
            case MotionEvent.ACTION_UP: {
                float deltaX;
                float upX = event.getX();
                deltaX = downX - upX;
                if (deltaX < -500) {
                    //drawer.open();
                    return true;
                }
            }
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getOrder()==0){

            DialogView2 dialogView = new DialogView2();
            FragmentManager manager = getSupportFragmentManager();
            dialogView.show(manager, "MyDialogSort");
        }

        if (item.getOrder()==1){
            Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
            startActivity(intent);
            finish();
        }

        if (item.getOrder() == 2) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            FirebaseAuth.getInstance().signOut();
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        // moving from one object of menu to other
        if (id == R.id.nav_tasks) {
            TEAMLINEAR.setVisibility(View.GONE);
            fab.setVisibility(View.VISIBLE);
            linTasks.setVisibility(View.VISIBLE);
            ref = true;

            getFromFire(none, 0);
        }
        if (id == R.id.nav_people) {
            TEAMLINEAR.setVisibility(View.VISIBLE);
            fab.setVisibility(View.GONE);
            linTasks.setVisibility(View.GONE);
            ref = false;
            getFromFire1();
        }
        if (id == R.id.nav_help) {
            fab.setVisibility(View.GONE);
        }
        drawer.close();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //add new data
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                IdDataBase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds: snapshot.getChildren()){
                            newId newid = ds.getValue(newId.class);
                            assert newid != null;
                            if (Integer.parseInt(newid.id)>0) ID = Integer.parseInt(newid.id)+1;
                        }
                        _txt1 = data.getStringExtra("txt1");
                        _txt2 = data.getStringExtra("txt2");
                        _txt3 = data.getStringExtra("txt3");
                        _txt4 = data.getStringExtra("txt4");
                        _txt5 = data.getStringExtra("txt5");
                        _txt6 = data.getStringExtra("txt6");
                        //add data
                        NewData newData = new NewData(String.valueOf(ID), _txt1, _txt2, _txt3, _txt4, _txt5, " ", "0",_txt6);
                        TasksDataBase.push().setValue(newData);
                        getFromFire(none, 0);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                IdDataBase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds: snapshot.getChildren()){
                            Map<String, Object> map = new HashMap<>();
                            map.put("id", String.valueOf(ID));
                            IdDataBase.child(ds.getKey()).updateChildren(map);
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
    protected void onResume() {
        super.onResume();
        if (ref){
            FileInputStream fin = null;
            String list = null;
            try{
                fin = openFileInput("sort.txt");
                byte[] bytes = new byte[fin.available()];
                fin.read(bytes);
                list = new String(bytes);
            }
            catch (FileNotFoundException e) {}
            catch (IOException e) {}
            String[] sorting = list.substring(1,list.length()-1).split(",");
            int status = Integer.parseInt(sorting[1].trim());
            getFromFire(sorting[0].trim(), status);
        }
        else getFromFire1();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private ItemTouchHelper getItemTouchHelper2(boolean ref){
        return new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                String tag = String.valueOf(viewHolder.itemView.getTag());
                Query ItemQuery;
                if (ref){
                    itemAdapter2.removeItem(viewHolder.getAdapterPosition());
                    ItemQuery = TeamDataBase.orderByChild("id").equalTo(tag);
                }
                else {
                    itemAdapter2_2.removeItem(viewHolder.getAdapterPosition());
                    ItemQuery = TypesDataBase.orderByChild("id").equalTo(tag);
                }
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
            }
        });
    }


    @SuppressLint("ResourceAsColor")
    public void getFromFire(String type, int status) {
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        String[] strings = {type, String.valueOf(status)};
        FileOutputStream fos = null;
        try {
            fos = openFileOutput("sort.txt", MODE_PRIVATE);
            fos.write(Arrays.toString(strings).getBytes());
        }
        catch (FileNotFoundException e) {}
        catch (IOException e) {}
        finally {
            try {
                if (fos!=null){
                    fos.close();
                }
            } catch (IOException e) {}
        }

        SwipeAdapter swipeAdapter;
        swipeAdapter=new SwipeAdapter(this, TEAM, type, status, arrayList);
        myViewPager2.setAdapter(swipeAdapter);
    }

    @SuppressLint("ResourceAsColor")
    private void getFromFire1() {
        TextView teamname = findViewById(R.id.thisNameTeam);
        teamname.setText(TEAM);
        progressBar=findViewById(R.id.progressBar);
        List<itemU2> tempList = new ArrayList<>();
        List<itemU3> typeList1 = new ArrayList<>();
        List<itemU3> typeList2 = new ArrayList<>();
        progressBar.setVisibility(View.VISIBLE);
        TextView txt = findViewById(R.id.sizeTeam2);
        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userAdapter.clearItems();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    NewUser newUser = ds.getValue(NewUser.class);
                    assert newUser != null;
                    itemU2 item = new itemU2();
                    item.setNam(newUser.name);
                    item.setTeam(newUser.team);
                    item.setRoot(newUser.root);
                    item.setDesc(newUser.desc);
                    tempList.add(item);
                }
                txt.setText(String.valueOf(tempList.size()));
                progressBar.setVisibility(View.GONE);
                userAdapter.setItems(tempList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        UsersDataBase.addListenerForSingleValueEvent(vListener);


        TextView name, desc, costTeam, costTypes;
        name = findViewById(R.id.ThisNameTeam);
        desc = findViewById(R.id.ThisDescTeam);
        costTeam = findViewById(R.id.sizeTeam);
        costTypes = findViewById(R.id.sizeTypes);

        ValueEventListener valueAbout = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    NewTeam team = ds.getValue(NewTeam.class);
                    name.setText(team.name);
                    desc.setText(team.desc);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        AboutDataBase.addListenerForSingleValueEvent(valueAbout);




        ValueEventListener valueTypes = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemAdapter2_2.clearItems();
                for (DataSnapshot ds: snapshot.getChildren()){
                    NewType newType = ds.getValue(NewType.class);
                    assert newType != null;
                    itemU3 itemu31 = new itemU3();
                    itemu31.setId(newType.id);
                    itemu31.setType(newType.type);
                    typeList1.add(itemu31);
                    idG=Integer.parseInt(newType.id);
                }
                itemAdapter2_2.setItems(typeList1);
                costTypes.setText(String.valueOf(itemAdapter2_2.getItemCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        TypesDataBase.addListenerForSingleValueEvent(valueTypes);

        ValueEventListener valueTeams = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemAdapter2.clearItems();
                for (DataSnapshot ds: snapshot.getChildren()){
                    NewPeo newPeo = ds.getValue(NewPeo.class);
                    assert newPeo != null;
                    itemU3 itemu3 = new itemU3();
                    itemu3.setId(newPeo.id);
                    itemu3.setType(newPeo.type);
                    typeList2.add(itemu3);
                    idH=Integer.parseInt(newPeo.id);
                }
                itemAdapter2.setItems(typeList2);
                costTeam.setText(String.valueOf(itemAdapter2.getItemCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        TeamDataBase.addListenerForSingleValueEvent(valueTeams);
    }

    public void Invante (View v){
        Toast.makeText(this, invite1, Toast.LENGTH_SHORT).show();
        ClipboardManager manager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        String s = invite2 + TEAM;
        ClipData data = ClipData.newPlainText("welcome!", s);
        manager.setPrimaryClip(data);
        Vibrator vibrator;
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }
    private void openElement(){
        lnr1.setOnClickListener(v -> {
            if (open2 || open3){
                lstTeams.setVisibility(View.GONE);
                _rec2.setVisibility(View.GONE);
                addTypeTeam.setVisibility(View.GONE);
                lstTypes.setVisibility(View.VISIBLE);
                addTypeTask.setVisibility(View.VISIBLE);
                open2=false;
                open3=false;
                open1=true;
            }
            else if (open1) {
                lstTypes.setVisibility(View.GONE);
                addTypeTask.setVisibility(View.GONE);
                open1=false;
            }
            else if (open1==false && open2==false && open3==false) {
                lstTypes.setVisibility(View.VISIBLE);
                addTypeTask.setVisibility(View.VISIBLE);
                open1=true;
            }
        });
        lnr2.setOnClickListener(v -> {
            if (open1 || open3){
                lstTeams.setVisibility(View.VISIBLE);
                addTypeTeam.setVisibility(View.VISIBLE);
                addTypeTask.setVisibility(View.GONE);
                _rec2.setVisibility(View.GONE);
                lstTypes.setVisibility(View.GONE);
                open1=false;
                open3=false;
                open2=true;
            }
            else if (open2) {
                lstTeams.setVisibility(View.GONE);
                addTypeTeam.setVisibility(View.GONE);
                open2=false;
            }
            else if (open1==false && open2==false && open3==false) {
                lstTeams.setVisibility(View.VISIBLE);
                addTypeTeam.setVisibility(View.VISIBLE);
                open2=true;
            }
        });
        lnr3.setOnClickListener(v -> {
            if (open1 || open2){
                lstTeams.setVisibility(View.GONE);
                lstTypes.setVisibility(View.GONE);
                addTypeTeam.setVisibility(View.GONE);
                addTypeTask.setVisibility(View.GONE);
                _rec2.setVisibility(View.VISIBLE);
                open1=false;
                open2=false;
                open3=true;
            }
            else if (open3){
                _rec2.setVisibility(View.GONE);
                open3=false;
            }
            else if (open1==false && open2==false && open3==false) {
                _rec2.setVisibility(View.VISIBLE);
                open3=true;
            }
        });
    }
    public void setOnFire(String n){
        if (newEl){
            NewType type = new NewType(n.trim(), String.valueOf(idG++));
            TypesDataBase.push().setValue(type);
        }
        else{
            NewPeo team = new NewPeo(n.trim(), String.valueOf(idH++));
            TeamDataBase.push().setValue(team);
        }
        getFromFire1();
    }

    public String[] getFromFireReturn(){
        List<String> tempList = new ArrayList<>();
        TypesDataBase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tempList.add(none2);
                tempList.add(none);
                for (DataSnapshot ds: snapshot.getChildren()){
                    NewType newType = ds.getValue(NewType.class);
                    assert newType != null;
                    tempList.add(newType.type);
                };
                templist1 = new String[tempList.size()];
                templist1 = tempList.toArray(new String[0]);
                try (FileOutputStream fos = openFileOutput("data.txt", MODE_PRIVATE)) {
                    fos.write(Arrays.toString(templist1).getBytes());
                } catch (IOException ignored) {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FileInputStream fin = null;
        String list = null;
        try{
            fin = openFileInput("data.txt");
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            list = new String(bytes);
        } catch (IOException ignored) {}
        assert list != null;
        return list.substring(1,list.length()-1).split(",");
    }
}


