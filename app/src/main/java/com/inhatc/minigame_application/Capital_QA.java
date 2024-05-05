package com.inhatc.minigame_application;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Capital_QA extends AppCompatActivity {
    private static DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_capital_qa);

        //DB의 인스턴스 가져옴
        dbHelper = DBHelper.getInstance(getApplicationContext());
        insert_qa(dbHelper);

        //DB데이터 가져오기



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void insert_qa(DBHelper dbHelper){
        dbHelper.insertQuestionAndAnswer("첫번째 문제","첫번째 답변");
        dbHelper.insertQuestionAndAnswer("두번째 문제","두번째 답변");
    }
}