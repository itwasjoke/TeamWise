package com.example.do_together.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.shapes.Shape;
import android.os.Build;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.do_together.ActivityAdd;
import com.example.do_together.ActivityMore;
import com.example.do_together.MainActivity;
import com.example.do_together.R;
import com.example.do_together.adapter.itemU;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder>{

    private List<itemU> itemsList = new ArrayList<>();
    private Context context;
    private String team;

    public ItemAdapter(Context context, String team){
        this.context = context;
        this.team = team;
        itemsList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ItemViewHolder(view, context, itemsList, team);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bind(itemsList.get(position).getTitle(),
                itemsList.get(position).getDesc(),
                itemsList.get(position).getDate(),
                itemsList.get(position).getId(),
                itemsList.get(position).getTo(),
                itemsList.get(position).getFrom(),
                itemsList.get(position).getDone(),
                itemsList.get(position).getStatus(),
                itemsList.get(position).getType()
        );
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder{
        private final TextView txt1;
        private final TextView txt2;
        private final TextView txt3;
        private final TextView txt4;
        private final ImageView img;
        private boolean b=true;
        private Context context;
        private List<itemU> mainArray;
        private String team;

        public ItemViewHolder(View itemView, Context context, List<itemU> mainArray, String tem) {
            super(itemView);
            this.context = context;
            this.mainArray = mainArray;
            this.team = tem;
            txt1 = itemView.findViewById(R.id.nametxt);
            txt2 = itemView.findViewById(R.id.desctxt);
            txt3 = itemView.findViewById(R.id.datetxt);
            txt4 = itemView.findViewById(R.id.fromToText);
            img = itemView.findViewById(R.id.status);
        }

        @SuppressLint("ResourceAsColor")
        public void bind(String title, String desc, String date, int id, String to, String from, String done, String status, String type){
            if (desc.equals(" ")) txt2.setVisibility(View.GONE);
            else txt2.setVisibility(View.VISIBLE);
            String from1 = context.getResources().getString(R.string.more2), to1 =context.getResources().getString(R.string.more3);
            txt1.setText(title);
            txt2.setText(desc);
            txt3.setText(date);
            int stat = Integer.parseInt(status);
            GradientDrawable drawable = (GradientDrawable) img.getBackground();
            if (stat==0) drawable.setColor(Color.RED);
            else if (stat==1) drawable.setColor(Color.rgb(248, 148, 0));
            else if (stat==2) drawable.setColor(Color.rgb(255, 198, 10));
            else if (stat==3) drawable.setColor(Color.GREEN);
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser firebaseUser = mAuth.getCurrentUser();
            assert firebaseUser != null;
            DatabaseReference rDatabase = FirebaseDatabase.getInstance().getReference(team).child("Users");
            Query UserQuery = rDatabase.orderByChild("email").equalTo(firebaseUser.getEmail());
            UserQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String si = String.valueOf(ds.child("name").getValue());
                        if (si.equals(from)) itemView.setLongClickable(false);
                        if (si.equals(from) && si.equals(to)) txt4.setVisibility(View.GONE);
                        else if (si.equals(from) && !si.equals(to)) txt4.setText(to1+to);
                        else if (si.equals(to) && !si.equals(from)) txt4.setText(from1+from);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



            itemView.setTag(id);
            itemView.setOnClickListener(v -> {
                if (b){
                    Intent intent = new Intent(context, ActivityMore.class);
                    intent.putExtra("title", title);
                    intent.putExtra("desc", desc);
                    intent.putExtra("date", date);
                    intent.putExtra("to", to);
                    intent.putExtra("from", from);
                    intent.putExtra("status", status);
                    intent.putExtra("done", done);
                    String idd=String.valueOf(id);
                    intent.putExtra("id", idd);
                    context.startActivity(intent);
                }
                b=true;

            });
            itemView.setOnLongClickListener(v -> {
                b=false;
                if (Integer.parseInt(status)==0){
                    drawable.setColor(Color.rgb(248, 148, 0));
                    Vibrator vibrator;
                    vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(400);
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(team).child(MainActivity.TASK_KEY);
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot ds: snapshot.getChildren()){
                                NewData data = ds.getValue(NewData.class);
                                if ((data.id).equals(String.valueOf(id))){
                                    Map<String,Object> map = new HashMap<>();
                                    map.put("status","1");
                                    mDatabase.child(ds.getKey()).updateChildren(map);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }


                return false;
            });
        }
    }

    public void setItems(List<itemU> item) {
        itemsList.clear();
        itemsList.addAll(item);
        notifyDataSetChanged();

    }
    public void clearItems() {
        itemsList.clear();
        notifyDataSetChanged();
    }

    public void removeItem(int pos){
       itemsList.remove(pos);
       notifyItemRangeChanged(0, itemsList.size());
       notifyItemRemoved(pos);
    }
}
