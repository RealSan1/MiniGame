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

        if(currentItem.getRank() <= 3){
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
        }else{
            holder.txtRank.setText(String.valueOf(currentItem.getRank()));
            holder.imgRank.setVisibility(View.GONE);
            holder.txtRank.setVisibility(View.VISIBLE);
        }



        /*
        // 현재 순위가 중복되는지 체크
        boolean isDuplicateRank = false;
        for (int i = 0; i < listData.length; i++) {
            if (i != position && listData[i].getRank() == currentItem.getRank()) {
                isDuplicateRank = true;
                //중복됨
                break;
            }
        }

        // 중복되는 순위가 없다면 isDuplicateRank == true 인 경우 순위의 첫 번째 항목에 대해서만 이미지 표시
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
         */
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
