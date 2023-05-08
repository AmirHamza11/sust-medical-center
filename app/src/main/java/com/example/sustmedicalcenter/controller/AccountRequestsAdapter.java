package com.example.sustmedicalcenter.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sustmedicalcenter.R;
import com.example.sustmedicalcenter.model.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountRequestsAdapter extends RecyclerView.Adapter<AccountRequestsAdapter.ViewHolder> {

    private Context ctx;
    private ArrayList<User> requestedUsers;
    private AccountRequestsAdapter.ItemClickListener itemClickListener;


    public AccountRequestsAdapter(Context ctx, ArrayList<User> requestedUsers, AccountRequestsAdapter.ItemClickListener itemClickListener){
        this.ctx = ctx;
        this.requestedUsers = requestedUsers;
        this.itemClickListener = itemClickListener;
    }


    public interface ItemClickListener {
        void onItemClicked(int position);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.account_requests_item,parent,false);
        return new ViewHolder(view,itemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(requestedUsers.get(position));
    }

    @Override
    public int getItemCount() {
        return requestedUsers.size();
    }

    public void addRequestedUser(User user){
        requestedUsers.add(user);
        notifyItemInserted(getItemCount()-1);

    }

    public void removeRequestedUser(User user){
        for(int i=0;i<requestedUsers.size();i++){
            if(requestedUsers.get(i).getUserUid().equals(user.getUserUid())){
                requestedUsers.remove(i);
                notifyItemRemoved(i);
                return;
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView userDisplayImageCIV;
        private TextView userNameTV;
        private TextView userTypeTV;
        private TextView requestSentDateTV;
        private AccountRequestsAdapter.ItemClickListener itemClickListener;

        public ViewHolder(@NonNull View itemView,AccountRequestsAdapter.ItemClickListener itemClickListener) {
            super(itemView);

            userDisplayImageCIV = itemView.findViewById(R.id.account_request_item_profile_image_id);
            userTypeTV = itemView.findViewById(R.id.account_request_item_user_type_tV_id);
            userNameTV = itemView.findViewById(R.id.account_request_item_userName_tV_id);
            requestSentDateTV = itemView.findViewById(R.id.account_request_item_request_date_tV_id);
            this.itemClickListener = itemClickListener;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onItemClicked(getBindingAdapterPosition());
                }
            });
        }

        public void bind(User user) {

            if(!user.getDisplayImageUrl().equals("")){
                Picasso.get().load(user.getDisplayImageUrl()).placeholder(R.drawable.d1).into(userDisplayImageCIV);
            }
            userNameTV.setText(user.getUserName());
            if(user.getUserType().equals("0")){
                userTypeTV.setText("Student");
            }else userTypeTV.setText("Doctor");

            requestSentDateTV.setText(user.getRequestSentDate());



        }
    }
}
