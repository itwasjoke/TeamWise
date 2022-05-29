package com.example.do_together.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.do_together.MainActivity;
import com.example.do_together.R;
import com.example.do_together.db.NewUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter2 extends RecyclerView.Adapter<ItemAdapter2.ItemViewHolder> {

    private List<itemU3> itemU3List=new ArrayList<>();
    private Context context;

    public ItemAdapter2(Context context){
        this.context = context;
        itemU3List = new ArrayList<>();
    }
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.type_item, parent, false);
        return new ItemViewHolder(view, context, itemU3List);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bind(itemU3List.get(position).getType(), itemU3List.get(position).getId());
    }

    @Override
    public int getItemCount() {
        return itemU3List.size();
    }
    static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener{
        private final TextView text;
        private Context context;
        private List<itemU3> mainArray;

        public ItemViewHolder(View itemView, Context context, List<itemU3> mainArray) {
            super(itemView);
            this.context = context;
            this.mainArray = mainArray;
            text = itemView.findViewById(R.id.typeText);
            itemView.setOnLongClickListener(this);
        }
        public void bind(String type, String id){
            text.setText(type);
            itemView.setTag(id);
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }

    public void setItems(List<itemU3> item) {
        itemU3List.clear();
        itemU3List.addAll(item);
        notifyDataSetChanged();

    }
    public void clearItems() {
        itemU3List.clear();
        notifyDataSetChanged();
    }

    public void removeItem(int pos){
        itemU3List.remove(pos);
        notifyItemRangeChanged(0, itemU3List.size());
        notifyItemRemoved(pos);
    }
}
