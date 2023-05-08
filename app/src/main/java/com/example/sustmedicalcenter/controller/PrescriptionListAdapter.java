package com.example.sustmedicalcenter.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sustmedicalcenter.model.PrescriptionItem;
import com.example.sustmedicalcenter.R;
import com.example.sustmedicalcenter.singleton.CurrentUserSingleton;

import java.util.ArrayList;

public class PrescriptionListAdapter extends RecyclerView.Adapter<PrescriptionListAdapter.ViewHolder> {

     private ArrayList<PrescriptionItem> list;
     private Context context;
     private int listType;
     private PrescriptionListAdapter.OnItemClicked onItemClickedListener;


     public interface OnItemClicked {
         void onDeleteItem(int position);
     }


    public PrescriptionListAdapter(ArrayList<PrescriptionItem> list, Context context, PrescriptionListAdapter.OnItemClicked onItemClickedListener){
        this.list = list;
        this.context = context;
        this.listType = listType;
        this.onItemClickedListener = onItemClickedListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.prescription_list_item,parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String medicine=list.get(position).getTextview3();
        String timetotake=list.get(position).getTextview4();
        holder.setData(medicine,timetotake);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {


        private TextView textview3, textview4;
        private ImageButton deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);

            textview3 = itemView.findViewById(R.id.prescription_list_item_medicine_name_textView_id);
            textview4 = itemView.findViewById(R.id.prescription_list_item_medicine_dosage_textView_id);
            deleteButton = itemView.findViewById(R.id.prescription_list_item_delete_medicine_button_id);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickedListener.onDeleteItem(getBindingAdapterPosition());
                }
            });

            if(CurrentUserSingleton.getInstance().getCurrentUser().getUserType().equals("1")){
                deleteButton.setVisibility(View.VISIBLE);
            }

        }


        public void setData(String medicine, String timetotake) {
            textview3.setText(medicine);
            textview4.setText(timetotake);
        }
    }

    public ArrayList<PrescriptionItem> getList() {
        return list;
    }


}


