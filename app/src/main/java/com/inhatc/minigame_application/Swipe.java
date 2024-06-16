package com.inhatc.minigame_application;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Random;

public class Swipe extends AppCompatActivity implements View.OnClickListener{

    Button Rbtn, Lbtn;
    ImageView BImg1, BImg2, BImg3, BImg4, BImg5, BImg6, BImg7, D, M;
    ArrayList<Integer> list = new ArrayList<>();
    Random random = new Random();
    TextView scoreText, timerTV, gameName, inputScore;;
    int Dock, Modapi, randomImg, score;
    Boolean isClicked = false;
    Dialog myDialog;
    private SocketThread skThread = SocketThread.getInstance();

    private static final long START_TIME_IN_MILLIS = 15000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_swipe);

        scoreText = findViewById(R.id.swipesocre);
        timerTV = findViewById(R.id.swipetime);
        BImg1 = findViewById(R.id.swipeImg1);
        BImg2 = findViewById(R.id.swipeImg2);
        BImg3 = findViewById(R.id.swipeImg3);
        BImg4 = findViewById(R.id.swipeImg4);
        BImg5 = findViewById(R.id.swipeImg5);
        BImg6 = findViewById(R.id.swipeImg6);
        BImg7 = findViewById(R.id.swipeImg7);
        D = findViewById(R.id.LeftImg);
        M = findViewById(R.id.Rightimg);
        Rbtn = findViewById(R.id.swipeR);
        Lbtn = findViewById(R.id.swipeL);

        D.bringToFront();
        M.bringToFront();

        Dock = R.drawable.duck;
        Modapi = R.drawable.modapi;
        D.setImageResource(Dock);
        M.setImageResource(Modapi);

        list.add(Dock);
        list.add(Modapi);

        BImg1.setImageResource(list.get(random.nextInt(list.size())));
        BImg2.setImageResource(list.get(random.nextInt(list.size())));
        BImg3.setImageResource(list.get(random.nextInt(list.size())));
        BImg4.setImageResource(list.get(random.nextInt(list.size())));
        BImg5.setImageResource(list.get(random.nextInt(list.size())));
        BImg6.setImageResource(list.get(random.nextInt(list.size())));
        BImg7.setImageResource(list.get(random.nextInt(list.size())));


        Rbtn.setOnClickListener(this);
        Lbtn.setOnClickListener(this);
        //팝업창 띄우기
        myDialog = new Dialog(this);
        setTimer();//타이머 시작

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    public void setTimer(){
        new CountDownTimer(START_TIME_IN_MILLIS, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                int secondsRemaining = (int) (millisUntilFinished / 1000);
                timerTV.setText(String.format("%02d:%02d", secondsRemaining / 60, secondsRemaining % 60));
                if (secondsRemaining < 6) {
                    timerTV.setTextColor(Color.RED);
                }
                if(secondsRemaining == 0){
                    // 팝업창 설정
                    myDialog.setContentView(R.layout.inputranking);
                    myDialog.setTitle("랭킹");
                    myDialog.setCancelable(true);
                    gameName = (TextView)myDialog.findViewById(R.id.inputGameName);
                    inputScore = (TextView)myDialog.findViewById(R.id.inputScore);
                    EditText inputName = (EditText)myDialog.findViewById(R.id.inputName);

                    Button rankingInput = (Button)myDialog.findViewById(R.id.input);
                    gameName.setText("좌로우로");

                    inputScore.setText(String.valueOf(score));
                    rankingInput.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String playerName = inputName.getText().toString();
                            //점수 DB전송
                            int result = skThread.sendDataToServer(playerName, score, gameName.getText().toString());
                            //result=1 입력 성공, 2 닉네임 중복
                            if(result == 1){
                                Log.d("result", "입력 성공");
                                finish();
                            }else{
                                Log.d("result", "입력 실패");
                                finish();
                            }
                        }
                    });

                    myDialog.show();
                }
            }
            @Override
            public void onFinish() {
                timerTV.setText("종료");
            }
        }.start();
    }

    @Override
    public void onClick(View view) {
        randomImg = list.get(random.nextInt(list.size())); // 클릭 시 랜덤 이미지 추출

        if (view == Rbtn && M.getDrawable().getConstantState() == BImg7.getDrawable().getConstantState()) {
            score++;
        } else if (view == Lbtn && D.getDrawable().getConstantState() == BImg7.getDrawable().getConstantState()) {
            score++;
        } else score--;
        if (isClicked) BImg7.setImageDrawable(BImg6.getDrawable());
        BImg6.setImageDrawable(BImg5.getDrawable());
        BImg5.setImageDrawable(BImg4.getDrawable());
        BImg4.setImageDrawable(BImg3.getDrawable());
        BImg3.setImageDrawable(BImg2.getDrawable());
        BImg2.setImageDrawable(BImg1.getDrawable());
        BImg1.setImageResource(randomImg);

        if (view == Rbtn) {
            // 오른쪽 버튼 클릭 시 오른쪽으로 이동 애니메이션

        } else if (view == Lbtn) {
            // 왼쪽 버튼 클릭 시 왼쪽으로 이동 애니메이션
        }
        // 점수 표시
        scoreText.setText(String.valueOf(score));
        isClicked = true; // 클릭 플래그 설정
    }

}