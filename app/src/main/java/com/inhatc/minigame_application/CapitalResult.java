package com.inhatc.minigame_application;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CapitalResult extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_capital_result);

        int correct = getIntent().getIntExtra("correct", 0);
        int total = getIntent().getIntExtra("total", 1);

        int score = (int) (((double) correct / total) * 100);

        TextView result = findViewById(R.id.txtCapResult);
        TextView scorePer = findViewById(R.id.txtCapScore);

        result.setText(correct + "개 맞히셨습니다.");
        scorePer.setText(score + "%"); // 점수 뒤에 % 추가

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
