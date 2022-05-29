package com.example.do_together.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.do_together.R;
import com.example.do_together.db.NewData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SwipeAdapter extends RecyclerView.Adapter<SwipeAdapter.MyViewHolder>{
    private Context context;
    private String team, type;
    private ArrayList<String> arrayList = new ArrayList<>();
    int fromTo, status;

    public SwipeAdapter(Context context, String team, String type, int status, ArrayList<String> arrayList){
        this.context = context;
        this.team = team;
        this.status = status;
        this.type = type;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public SwipeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler, parent, false);
        return new SwipeAdapter.MyViewHolder(view, context, team);
    }

    @Override
    public void onBindViewHolder(@NonNull SwipeAdapter.MyViewHolder holder, int position) {
        holder.bind(position, team, type, status);
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        private final RecyclerView rec;
        private final TextView text;
        private final ProgressBar br;
        private Context context;
        private String team;
        private String none, none2;

        public MyViewHolder(View itemView, Context context, String tem) {
            super(itemView);
            this.context = context;
            this.team = tem;
            this.none = context.getResources().getString(R.string.type);
            this.none2 = context.getResources().getString(R.string.noneType);
            rec = itemView.findViewById(R.id.recFirst);
            br = itemView.findViewById(R.id.progressbar3);
            text = itemView.findViewById(R.id.textNone);
        }

        @SuppressLint("ResourceAsColor")
        public void bind(int pos, String tem, String Type, int Status){
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser firebaseUser = mAuth.getCurrentUser();
            assert firebaseUser != null;
            DatabaseReference UsersDataBase = FirebaseDatabase.getInstance().getReference(tem).child("Users");
            DatabaseReference TasksDataBase = FirebaseDatabase.getInstance().getReference(tem).child("Tasks");
            ItemAdapter itemAdapter = new ItemAdapter(context, team);
            rec.setLayoutManager(new LinearLayoutManager(context));
            rec.setAdapter(itemAdapter);

            boolean b = Type.equals(none) && Status == 0;
            br.setVisibility(View.VISIBLE);
            text.setVisibility(View.GONE);
            Query UserQuery = UsersDataBase.orderByChild("email").equalTo(firebaseUser.getEmail());
            UserQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String s = String.valueOf(ds.child("name").getValue());
                        String root = String.valueOf(ds.child("root").getValue());
                        int root1 = Integer.parseInt(root);

                        List<itemU> tempList = new ArrayList<>();
                        ValueEventListener vListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                itemAdapter.clearItems();
                                for (DataSnapshot ds : snapshot.getChildren()) {

                                    NewData newData = ds.getValue(NewData.class);
                                    assert newData != null;
                                    itemU item = new itemU();
                                    if (s.equals(newData.to) || s.equals(newData.from) || root1==1) {
                                        item.setTitle(newData.title);
                                        item.setDesc(newData.desc);
                                        item.setDate(newData.date);
                                        item.setFrom(newData.from);
                                        item.setTo(newData.to);
                                        item.setDone(newData.done);
                                        item.setId(Integer.parseInt(newData.id));
                                        item.setStatus(newData.status);
                                        item.setType(newData.type);
                                        if (pos==2 && newData.from.equals(s) && newData.to.equals(s)){
                                            if (b) tempList.add(0, item);
                                            else if ((newData.type.equals(Type) || Type.equals(none2)) && (newData.status.equals(String.valueOf(Status-1)) || Status==0))
                                                tempList.add(0, item);
                                        }
                                        else if (pos==1 && newData.from.equals(s) && !newData.to.equals(s)) {
                                            if (b) tempList.add(0, item);
                                            else if ((newData.type.equals(Type) || Type.equals(none2)) && (newData.status.equals(String.valueOf(Status-1)) || Status==0))
                                                tempList.add(0, item);
                                        }
                                        else if (pos==0 && newData.to.equals(s) && !newData.from.equals(s)) {
                                            if (b) tempList.add(0, item);
                                            else if ((newData.type.equals(Type) || Type.equals(none2)) && (newData.status.equals(String.valueOf(Status-1)) || Status==0))
                                                tempList.add(0, item);
                                        }
                                    }


                                }
                                if (tempList.size()==0) text.setVisibility(View.VISIBLE);
                                itemAdapter.setItems(tempList);
                                br.setVisibility(View.GONE);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        };
                        TasksDataBase.addListenerForSingleValueEvent(vListener);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}
