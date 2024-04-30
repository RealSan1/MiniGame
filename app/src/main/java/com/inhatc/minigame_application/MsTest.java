package com.inhatc.minigame_application;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.List;

public class MsTest extends AppCompatActivity implements View.OnClickListener{

    TextView Text;
    TextView TextEx;
    Button Btn;
    Random random;
    int min = 2;
    int max = 4;
    int randomSeconds;
    long beforeTime;
    long afterTime;
    double elapsedTime;
    List<Double> list;
    double listSum;
    double result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ms_test);

        Text = (TextView)findViewById(R.id.MsText);
        TextEx = (TextView)findViewById(R.id.MsTestEx);
        Btn = (Button)findViewById(R.id.MsTestBtn);
        random = new Random();
        list = new ArrayList<>(); // 리스트 초기화
        randomSeconds = (min * 1000) + random.nextInt((max - min + 1) * 1000); // 랜덤한 시간 계산


        Btn.setOnClickListener(MsTest.this);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 버튼 클릭 이벤트 핸들러 설정
                if (Btn.isEnabled() == true) {
                    Btn.setBackgroundColor(Color.GREEN);
                    beforeTime = System.currentTimeMillis(); //현재 시간
                    Btn.setOnClickListener(MsTest.this);
                }
            }
        }, randomSeconds); // 2~5초 후 실행

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onClick(View view) {
        Btn.setEnabled(false); //연속 클릭 방지
        ColorDrawable BtnColor = (ColorDrawable)Btn.getBackground();
        if(BtnColor.getColor() == Color.GREEN){
        afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간
        elapsedTime = afterTime - beforeTime;
        list.add(elapsedTime);
        listSum = 0;
        for (Double value : list) listSum += value;
        result = listSum / list.size();
        Text.setText(result + " MS");
        } else{
            list.add(0.0);
            Text.setText("너무 빨라요.");
        }
    }
    
    //배너 창
    public void Click(View view){
        Text.setVisibility(View.VISIBLE);
        Btn.setVisibility(View.VISIBLE);
        TextEx.setVisibility(View.INVISIBLE);
    }
}