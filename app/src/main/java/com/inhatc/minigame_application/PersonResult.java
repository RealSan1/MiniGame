package com.inhatc.minigame_application;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PersonResult extends AppCompatActivity {
    Dialog myDialog;
    TextView gameName, inputScore;
    int score;
    private SocketThread skThread = SocketThread.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_person_result);

        int correct = getIntent().getIntExtra("correct", 0);
        int total = getIntent().getIntExtra("total", 1);

        score = (int) (((double) correct / total) * 100);

        TextView result = findViewById(R.id.txtPerResult);
        TextView scorePer = findViewById(R.id.txtPerScore);

        result.setText(correct + "개 정답!");
        scorePer.setText(score + "%"); // 점수 뒤에 % 추가

        myDialog = new Dialog(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}