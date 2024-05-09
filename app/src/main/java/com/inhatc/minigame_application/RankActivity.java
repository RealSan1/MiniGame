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
        String[] jsonData = ParseData(skThread.SelectRankList());

        /*
        for(int i = 0; i<jsonData.length; i+=4){
            System.out.println("jsonData["+i+"]"+jsonData[i]);
            System.out.println("jsonData["+(i+1)+"]"+jsonData[i+1]);
            System.out.println("jsonData["+(i+2)+"]"+jsonData[i+2]);
            System.out.println("jsonData["+(i+3)+"]"+jsonData[i+3]);
        }
        */
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        RankListAdapter adapter;

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        /*
        RankListItem[] listData = new RankListItem[]{
                new RankListItem("이름",1,100),
                new RankListItem("이름2", 2, 90)
        };*/

        RankListItem[] listData = new RankListItem[jsonData.length/4];
        for(int i = 0; i<jsonData.length/4; i++){
            listData[i] = new RankListItem(jsonData[i*4+1], Integer.parseInt(jsonData[i*4+3]), Integer.parseInt(jsonData[i*4+2]));
        }

        adapter = new RankListAdapter(this, listData);
        recyclerView.setAdapter(adapter);
    }

    protected String[] ParseData(String json){
        System.out.println("json데이터 파싱 중 : "+json);
        try{
            JSONArray jsonArray = new JSONArray(json);
            String[] jsonary = new String[jsonArray.length()*4];

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                jsonary[i * 4] = jsonObject.getString("code");
                jsonary[i * 4 + 1] = jsonObject.getString("userName");
                jsonary[i * 4 + 2] = jsonObject.getString("score");
                jsonary[i * 4 + 3] = jsonObject.getString("rank");
                System.out.println("json : " + jsonary[i * 4] + " " + jsonary[i * 4 + 1] + " " + jsonary[i * 4 + 2] + " " + jsonary[i * 4 + 3]);
            }

            return jsonary;
        }catch(JSONException e){
            e.printStackTrace();
        }
        return null;
    }
}