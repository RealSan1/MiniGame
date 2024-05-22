package com.inhatc.minigame_application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainSelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_select);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void SelectRank(View view){
        System.out.println("랭킹 검색");
        Intent intent = new Intent(getApplicationContext(), SelectGameActivity.class);
        intent.putExtra("select",0);
        startActivity(intent);
    }

    public void SelectGames(View view) {
        System.out.println("게임 검색");
        Intent intent = new Intent(getApplicationContext(), SelectGameActivity.class);
        intent.putExtra("select", 1);
        startActivity(intent);
    }

}