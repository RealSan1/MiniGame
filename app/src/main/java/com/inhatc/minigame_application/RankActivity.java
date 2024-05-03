package com.inhatc.minigame_application;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RankActivity extends AppCompatActivity {

    private SocketThread skThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_rank);

        skThread = SocketThread.getInstance();
        String[] Data = ParseData(skThread.SelectRankList());

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        RankListAdapter adapter;

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        RankListItem[] listData = new RankListItem[]{
                new RankListItem("이름",1,100),
                new RankListItem("이름2", 2, 90)
        };

        /*RankListItem[] listData = new RankListItem[Data.length/4];
        for(int i = 0; i<Data.length; i+=4){
            listData[i/4] = new RankListItem(Data[i+1], Integer.parseInt(Data[i+3]), Integer.parseInt(Data[i+2]));
        }*/

        adapter = new RankListAdapter(this, listData);
        recyclerView.setAdapter(adapter);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    protected String[] ParseData(String json){
        try{
            JSONArray jsonArray = new JSONArray(json);
            String[] jsonary = new String[jsonArray.length()*4];
            for(int i = 0; i<jsonArray.length()*4; i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int code = jsonObject.getInt("code");
                String userName = jsonObject.getString("userName");
                int score = jsonObject.getInt("score");
                int rank = jsonObject.getInt("rank");
                jsonary[i] = Integer.toString(code);
                i++;
                jsonary[i] = userName;
                i++;
                jsonary[i] = Integer.toString(score);
                i++;
                jsonary[i] = Integer.toString(rank);
            }
            return jsonary;
        }catch(JSONException e){
            e.printStackTrace();
        }
        return null;
    }
}