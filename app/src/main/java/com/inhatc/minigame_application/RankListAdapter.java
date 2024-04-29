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
    Context mContext;
    ArrayList<RankListItem> items = new ArrayList();

    public RankListAdapter(Context mContext){
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.listitem, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RankListItem item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(RankListItem item){
        items.add(item);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView ItemRank;
        TextView ItemName;
        TextView ItemScore;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            ItemRank = itemView.findViewById(R.id.ItemRank);
            ItemName = itemView.findViewById(R.id.ItemName);
            ItemScore = itemView.findViewById(R.id.ItemScore);
        }
        public void setItem(RankListItem item){
            ItemRank.setText(item.rank);
            ItemName.setText(item.name);
            ItemScore.setText(item.score);
        }
    }
}
