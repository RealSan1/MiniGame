package com.inhatc.minigame_application;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RankListAdapter extends RecyclerView.Adapter<RankListAdapter.ViewHolder> {
    Context context;
    public RankListItem[] listData;

    public RankListAdapter(Context context, RankListItem[] listData){
        this.context = context;
        this.listData = listData;
    }

    @NonNull
    @Override
    public RankListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_rank_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RankListAdapter.ViewHolder holder, int position) {
        RankListItem currentItem = listData[position];
        holder.txtName.setText(currentItem.getName());
        holder.txtScore.setText(String.valueOf(currentItem.getScore()));

        // Check if the current rank is duplicated
        boolean isDuplicateRank = false;
        for (int i = 0; i < listData.length; i++) {
            if (i != position && listData[i].getRank() == currentItem.getRank()) {
                isDuplicateRank = true;
                break;
            }
        }

        // Show image only for the first entry with this rank if not duplicated
        if (!isDuplicateRank && currentItem.getRank() <= 3) {
            switch (currentItem.getRank()) {
                case 1:
                    holder.imgRank.setImageResource(R.drawable.first);
                    break;
                case 2:
                    holder.imgRank.setImageResource(R.drawable.second);
                    break;
                case 3:
                    holder.imgRank.setImageResource(R.drawable.third);
                    break;
            }
            holder.imgRank.setVisibility(View.VISIBLE);
            holder.txtRank.setVisibility(View.GONE);
        } else {
            // Show text for ranks that are duplicated or outside of top three
            holder.txtRank.setText(String.valueOf(currentItem.getRank()));
            holder.imgRank.setVisibility(View.GONE);
            holder.txtRank.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return listData.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgRank;
        TextView txtName;
        TextView txtRank;
        TextView txtScore;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgRank = itemView.findViewById(R.id.imgRank);
            txtName = itemView.findViewById(R.id.txtName);
            txtRank = itemView.findViewById(R.id.txtRank);
            txtScore = itemView.findViewById(R.id.txtScore);
        }
    }
}
