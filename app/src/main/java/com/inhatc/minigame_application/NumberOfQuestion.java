package com.inhatc.minigame_application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class NumberOfQuestion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_number_of_question);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void Select_10(View view) {
        Intent intent = new Intent(this, CapitalQA.class);
        intent.putExtra("num", 10);
        startActivity(intent);
    }
    public void Select_20(View view) {
        Intent intent = new Intent(this, CapitalQA.class);
        intent.putExtra("num", 20);
        startActivity(intent);
    }
    public void Select_30(View view) {
        Intent intent = new Intent(this, CapitalQA.class);
        intent.putExtra("num", 30);
        startActivity(intent);
    }
    public void Select_50(View view) {
        Intent intent = new Intent(this, CapitalQA.class);
        intent.putExtra("num", 50);
        startActivity(intent);
    }
}
