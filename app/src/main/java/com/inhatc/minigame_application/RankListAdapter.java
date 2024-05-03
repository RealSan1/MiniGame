package com.inhatc.minigame_application;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        holder.txtName.setText(listData[position].getName());
        holder.txtRank.setText(String.valueOf(listData[position].getRank()));
        holder.txtScore.setText(String.valueOf(listData[position].getScore()));
    }

    @Override
    public int getItemCount() {
        return listData.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtName;
        TextView txtRank;
        TextView txtScore;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtRank = itemView.findViewById(R.id.txtRank);
            txtScore = itemView.findViewById(R.id.txtScore);
        }
    }
}
