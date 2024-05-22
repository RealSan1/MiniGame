package com.inhatc.minigame_application;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Random;

public class BlockPuzzle extends AppCompatActivity implements View.OnClickListener {

    Button Rbtn, Gbtn, Bbtn, Start;
    ImageView BImg1, BImg2, BImg3, BImg4, BImg5, Combo;
    int ColorR, ColorG, ColorB, randomColor, score;
    ArrayList<Integer> list = new ArrayList<>();
    private static final long START_TIME_IN_MILLIS = 5000;
    int count = 0;
    Random random = new Random();
    Boolean isClicked = false;
    TextView scoreText, timerTV, gameName, inputScore;
    FrameLayout popup;
    Dialog myDialog;
    private SocketThread skThread = SocketThread.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_block_puzzle);

        scoreText = findViewById(R.id.BlockScoreText);

        Rbtn = findViewById(R.id.BlockBtnR);
        Gbtn = findViewById(R.id.BlockBtnG);
        Bbtn = findViewById(R.id.BlockBtnB);
        BImg1 = findViewById(R.id.BlockImg1);
        BImg2 = findViewById(R.id.BlockImg2);
        BImg3 = findViewById(R.id.BlockImg3);
        BImg4 = findViewById(R.id.BlockImg4);
        BImg5 = findViewById(R.id.BlockImg5);
        Start = findViewById(R.id.startBtn);
        Combo = findViewById(R.id.combo);

        popup = findViewById(R.id.popup);
        popup.bringToFront();
        timerTV = findViewById(R.id.txtTimer);

        ColorR = R.drawable.red;
        ColorG = R.drawable.blue;
        ColorB = R.drawable.green;

        list.add(ColorR);
        list.add(ColorG);
        list.add(ColorB);

        BImg1.setImageResource(list.get(random.nextInt(list.size())));
        BImg2.setImageResource(list.get(random.nextInt(list.size())));
        BImg3.setImageResource(list.get(random.nextInt(list.size())));
        BImg4.setImageResource(0);
        BImg5.setImageResource(0);

        GradientDrawable drawR = (GradientDrawable)Rbtn.getBackground();
        drawR.setColor(Color.RED);
        GradientDrawable drawG = (GradientDrawable)Gbtn.getBackground();
        drawG.setColor(Color.GREEN);
        GradientDrawable drawB = (GradientDrawable)Bbtn.getBackground();
        drawB.setColor(Color.BLUE);

        Rbtn.setOnClickListener(this);
        Gbtn.setOnClickListener(this);
        Bbtn.setOnClickListener(this);


        //팝업창 띄우기
        myDialog = new Dialog(this);

        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTimer();
                popup.setEnabled(false);
                popup.setVisibility(View.INVISIBLE);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onClick(View view) {
        randomColor = list.get(random.nextInt(list.size())); //클릭 시 랜덤 색 추출
        if (view == Rbtn && BImg3.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.red).getConstantState())) {
            // 일치하면 점수 증가
            score++;
            count++;
        } else if (view == Gbtn && BImg3.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.green).getConstantState())) {
            // 일치하면 점수 증가
            score++;
            count++;
        } else if (view == Bbtn && BImg3.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.blue).getConstantState())) {
            // 일치하면 점수 증가
            score++;
            count++;
        }
        else{
            count = 0;
        }
        if(isClicked) BImg5.setImageDrawable(BImg4.getDrawable());
        BImg4.setImageDrawable(BImg3.getDrawable());
        BImg3.setImageDrawable(BImg2.getDrawable());
        BImg2.setImageDrawable(BImg1.getDrawable());
        BImg1.setImageResource(randomColor);
        isClicked = true;
        if(count >= 3){
            Combo.setVisibility(View.VISIBLE);
            score += 2;
        } else Combo.setVisibility(View.INVISIBLE);
        scoreText.setText(String.valueOf(score * 10));

    }

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
                    Rbtn.setEnabled(false);
                    Gbtn.setEnabled(false);
                    Bbtn.setEnabled(false);

                    // 팝업창 설정
                    myDialog.setContentView(R.layout.inputranking);
                    myDialog.setTitle("랭킹");
                    myDialog.setCancelable(true);
                    gameName = (TextView)myDialog.findViewById(R.id.inputGameName);
                    inputScore = (TextView)myDialog.findViewById(R.id.inputRankingScore);
                    EditText inputName = (EditText)myDialog.findViewById(R.id.inputName);

                    Button rankingInput = (Button)myDialog.findViewById(R.id.inputRankingI);
                    gameName.setText("블럭 맞추기");

                    inputScore.setText(String.valueOf(score * 10));
                    rankingInput.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String playerName = inputName.getText().toString();
                            //점수 DB전송
                            int result = skThread.sendDataToServer(playerName, score*10, gameName.getText().toString());
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
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            inputName.requestFocus();
                            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            if (inputMethodManager != null) {
                                inputMethodManager.showSoftInput(inputName, InputMethodManager.SHOW_IMPLICIT);
                            }
                        }
                    }, 300);
                }
            }
            @Override
            public void onFinish() {
                timerTV.setText("종료");
            }
        }.start();
    }
    public void Cancel(){
        finish();
    }
}