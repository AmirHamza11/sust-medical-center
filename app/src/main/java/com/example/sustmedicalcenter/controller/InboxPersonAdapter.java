package com.example.sustmedicalcenter.controller;

import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sustmedicalcenter.R;
import com.example.sustmedicalcenter.model.InboxPerson;
import com.example.sustmedicalcenter.view.InboxPersonViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class InboxPersonAdapter extends RecyclerView.Adapter<InboxPersonViewHolder> {

    private Context context;
    private ArrayList<InboxPerson> inboxPersons;
    private ItemOnClickListener itemOnClickListener;

    public InboxPersonAdapter(Context context, ArrayList<InboxPerson> inboxPersons, ItemOnClickListener itemOnClickListener) {
        this.context = context;
        this.inboxPersons = inboxPersons;
        this.itemOnClickListener = itemOnClickListener;
    }

    public interface ItemOnClickListener{
        void onItemClick(int position);
    }

    @NonNull
    @NotNull
    @Override
    public InboxPersonViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.inbox_person,parent,false);
        return new InboxPersonViewHolder(view,itemOnClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull InboxPersonViewHolder holder, int position) {
        holder.bind(inboxPersons.get(position));
    }

    @Override
    public int getItemCount() {
        return inboxPersons.size();
    }

    public void setAdapter(ArrayList<InboxPerson>inboxPersons){
        this.inboxPersons = inboxPersons;
    }

    public void addInboxPerson(InboxPerson ip){
        inboxPersons.add(0,ip);
        notifyItemInserted(0);
    }
    public void modifyInboxPerson(InboxPerson ip){
        for(int i = 0; i < inboxPersons.size(); i++){
            if(inboxPersons.get(i).getPersonUid().equals(ip.getPersonUid())){
                inboxPersons.remove(i);
                inboxPersons.add(0,ip);
                notifyItemRemoved(i);
                notifyItemInserted(0);
                return;
            }
        }
    }
}
