package com.example.do_together.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.do_together.R;
import com.example.do_together.db.NewUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ItemViewHolder> {

    private List<itemU2> itemsList2 = new ArrayList<>();
    private Context context;
    private String team;

    public UserAdapter(Context context, String team){
        this.context = context;
        this.team = team;
        itemsList2 = new ArrayList<>();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_data, parent, false);
        return new ItemViewHolder(view, context, itemsList2);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bind(itemsList2.get(position).getNam(), itemsList2.get(position).getRoot(), itemsList2.get(position).getTeam(), team, itemsList2.get(position).getDesc());
    }

    @Override
    public int getItemCount() {
        return itemsList2.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder{
        private final TextView txt1, txt2, txt3, txt4;
        private final TextView text2;
        private Context context;
        private List<itemU2> mainArray;
        public ItemViewHolder(View itemView, Context context, List<itemU2> mainArray) {
            super(itemView);
            this.context=context;
            this.mainArray=mainArray;
            txt1= itemView.findViewById(R.id.UserNameTxt);
            txt2= itemView.findViewById(R.id.admin2);
            txt3 = itemView.findViewById(R.id.admin);
            txt4 = itemView.findViewById(R.id.admin3);
            text2= itemView.findViewById(R.id.userAdmin);
        }


        public void bind(String nam, String root, String team, String tem, String desc) {
            String s1 = context.getResources().getString(R.string.user1), s2 = context.getResources().getString(R.string.user2);
            txt4.setText(desc);
            DatabaseReference UsersDataBase = FirebaseDatabase.getInstance().getReference(tem).child("Users");

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
                        if (rt==1) {
                            itemView.setOnLongClickListener(v -> {
                                text2.setVisibility(View.VISIBLE);
                                txt4.setVisibility(View.VISIBLE);
                                return true;
                            });
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            txt1.setText(nam);
            int r = Integer.parseInt(root);
            txt3.setText(team);
            if (r==1) {
                txt2.setText(R.string.admin1);
                text2.setText(s2);
            }
            else {
                text2.setText(s1);
                txt2.setText(R.string.admin2);
            }
            itemView.setOnClickListener(v -> {
                txt4.setVisibility(View.GONE);
                text2.setVisibility(View.GONE);
            });
           text2.setOnClickListener(v -> {
               if (String.valueOf(text2.getText()).equals(s1)){
                   text2.setVisibility(View.GONE);
                   UsersDataBase.addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot snapshot) {
                           for (DataSnapshot ds:snapshot.getChildren()){
                               NewUser user = ds.getValue(NewUser.class);
                               if (nam.equals(user.name)){
                                   Map<String,Object> map = new HashMap<>();
                                   map.put("root", "1");
                                   UsersDataBase.child(ds.getKey()).updateChildren(map);
                               }
                           }
                           txt2.setText(R.string.admin1);
                           text2.setText(s2);
                           text2.setVisibility(View.GONE);
                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError error) {

                       }
                   });
               }
               else {
                   text2.setVisibility(View.GONE);
                   UsersDataBase.addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot snapshot) {
                           for (DataSnapshot ds:snapshot.getChildren()){
                               NewUser user = ds.getValue(NewUser.class);
                               if (nam.equals(user.name)){
                                   Map<String,Object> map = new HashMap<>();
                                   map.put("root", "0");
                                   UsersDataBase.child(ds.getKey()).updateChildren(map);
                               }
                           }
                           text2.setText(s1);
                           txt2.setText(R.string.admin2);
                           text2.setVisibility(View.GONE);
                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError error) {

                       }
                   });
               }
           });
        }
    }

    public void setItems(List<itemU2> item) {
        itemsList2.clear();
        itemsList2.addAll(item);
        notifyDataSetChanged();

    }
    public void clearItems() {
        itemsList2.clear();
        notifyDataSetChanged();
    }
}
