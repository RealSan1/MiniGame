package com.inhatc.minigame_application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RankSelect extends AppCompatActivity {
    SocketThread skThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_rank_select);

        skThread = SocketThread.getInstance();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void Find_Color(View view) {
        Intent intent = new Intent(getApplicationContext(), RankActivity.class);
        RankActivity.receiveGamename("색깔 맞추기 게임");
        startActivity(intent);
    }

    public void MsTest(View view){
        Intent MsTestIntent = new Intent(getApplicationContext(), RankActivity.class);
        RankActivity.receiveGamename("반응속도 테스트 게임");
        startActivity(MsTestIntent);
    }

    public void Capital(View view){
        Intent intent = new Intent(getApplicationContext(), RankActivity.class);
        RankActivity.receiveGamename("수도 맞히기");
        startActivity(intent);
    }

    public void BlockPuzzle(View view){
        Intent intent = new Intent(getApplicationContext(), RankActivity.class);
        RankActivity.receiveGamename("블럭 맞추기");
        startActivity(intent);
    }
}