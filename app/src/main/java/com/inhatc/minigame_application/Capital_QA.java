package com.inhatc.minigame_application;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Capital_QA extends AppCompatActivity {
    private static DBHelper dbHelper;
    private static TextView question;
    private static TextView answer;
    private TextView timerTV;
    private static final long START_TIME_IN_MILLIS = 30000;
    private SocketThread skThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_capital_qa);

        // 문제 개수 받아오기
        int numOfQ = getIntent().getIntExtra("num",1);
/*
        skThread = SocketThread.getInstance();

        //서버로 부터 받아온 mysql json형식의 데이터
        //mysql테이블 capital_qa 테이블 만들어놓음
        String data = skThread.getq_a();
        System.out.println(data);


        //DB의 인스턴스 가져옴(로컬전용DB) 쓰고싶으면 쓰고 안쓰고 싶으면 안쓰고
        dbHelper = DBHelper.getInstance(getApplicationContext());
        insert_qa(dbHelper);

        loadDataFromDB();
*/
        question = (TextView)findViewById(R.id.txtQuestion);
        answer = (TextView)findViewById(R.id.txtAnswer);

        timerTV = (TextView)findViewById(R.id.txtTimer);

       setTimer();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loadDataFromDB() {
        Cursor cursor = dbHelper.getAllData();
        if (cursor.moveToFirst()) {
            int questionIndex = cursor.getColumnIndex("country");
            int answerIndex = cursor.getColumnIndex("capital");

            if (questionIndex != -1 && answerIndex != -1) {
                String questions = cursor.getString(questionIndex);
                String answers = cursor.getString(answerIndex);

                question.setText(questions);
                answer.setText(answers);
            }
        }
        cursor.close();
    }

    public void insert_qa(DBHelper dbHelper){
        dbHelper.insertQuestionAndAnswer("첫번째 문제","첫번째 답변");
        dbHelper.insertQuestionAndAnswer("두번째 문제","두번째 답변");
    }

    // 타이머 설정
    public void setTimer(){
        new CountDownTimer(START_TIME_IN_MILLIS, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                int secondsRemaining = (int) (millisUntilFinished / 1000);
                timerTV.setText(String.valueOf(secondsRemaining));
                if (secondsRemaining < 6) {
                    timerTV.setTextColor(Color.RED);
                }
                if(secondsRemaining == 0){
                    onFinish();
                }
            }
            @Override
            public void onFinish() {
                timerTV.setText("종료");
            }
        }.start();

    }
}