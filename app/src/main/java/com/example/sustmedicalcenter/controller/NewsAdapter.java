package com.example.sustmedicalcenter.controller;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sustmedicalcenter.model.NewsModel;
import com.example.sustmedicalcenter.R;
import com.example.sustmedicalcenter.FullNewsActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    Context context;
    ArrayList<NewsModel> NewsList;

    public NewsAdapter(Context context, ArrayList<NewsModel> newsList) {
        this.context = context;
        NewsList = newsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.news_design_layout,parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.newsHeading.setText(NewsList.get(position).getNewsTitle());
        holder.newsContent.setText(NewsList.get(position).getNewsContent());
        holder.newsHeading.setText(NewsList.get(position).getNewsTitle());


        if(!NewsList.get(position).getNewsImage().equals("")){
            Picasso.get().load(NewsList.get(position).getNewsImage()).into(holder.newsImage);
        }


    }

    public void addNews(NewsModel news){
        NewsList.add(0,news);
        notifyItemInserted(0);

    }

    @Override
    public int getItemCount() {
        return NewsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView newsHeading, newsContent;
        ImageView newsImage;
        CardView newsCardview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            newsHeading = itemView.findViewById(R.id.news_Title);
            newsContent = itemView.findViewById(R.id.news_content);
            newsImage = itemView.findViewById(R.id.news_image);
            newsCardview = itemView.findViewById(R.id.news_cardview);

            //Pressing the cardview, user can view full news//

            newsCardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FullNewsActivity.class);
                    intent.putExtra("news",NewsList.get(getBindingAdapterPosition()));
                    context.startActivity(intent);
                }
            });

        }

    }
}
