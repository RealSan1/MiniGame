package com.inhatc.minigame_application;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class Capital_QA extends AppCompatActivity {
    private static DBHelper dbHelper;
    private static TextView question;
    private static TextView answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_capital_qa);

        //DB의 인스턴스 가져옴
        dbHelper = DBHelper.getInstance(getApplicationContext());
        insert_qa(dbHelper);

        System.out.println("capital test");

        //DB데이터 가져오기
        Cursor cursor = dbHelper.getAllData();
        if(cursor.moveToFirst()){
        int quesitonIndex = cursor.getColumnIndex("qa_question");
        int answerIndex = cursor.getColumnIndex("qa_answer");

        String questions = cursor.getString(quesitonIndex);
        String answers = cursor.getString(answerIndex);

        question = (TextView)findViewById(R.id.textView2);
        question.setText(questions);

        answer = (TextView)findViewById(R.id.textView3);
        answer.setText(answers);
        }


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