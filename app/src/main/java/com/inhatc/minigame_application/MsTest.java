package com.inhatc.minigame_application;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    TextView Text, TextEx, TextTouch, TextChance, inputScore, gameName;
    Button Btn, StartBtn;
    Random random;
    int min = 3, max = 4;
    int randomSeconds;
    long beforeTime, afterTime;
    List<Double> list;
    double listSum, result, elapsedTime;
    Dialog myDialog;
    int i;
    //3번째 등록 후 계속 틀릴 시 i가 0이되는 문제 발견
    private SocketThread skThread = SocketThread.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ms_test);

        Text = (TextView)findViewById(R.id.MsText);
        TextEx = (TextView)findViewById(R.id.MsTestEx); //설명
        TextTouch = (TextView)findViewById(R.id.txtTouch);
        TextChance = (TextView)findViewById(R.id.MsTextChance); //기회
        Btn = (Button)findViewById(R.id.MsTestBtn);
        StartBtn = (Button)findViewById(R.id.MsStartBtn);
        random = new Random();
        list = new ArrayList<>(); // 리스트 초기화
        randomSeconds = (min * 1000) + random.nextInt((max - min + 1) * 1000); // 3~4초
        myDialog = new Dialog(this);

        StartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ColorDrawable BtnColor = (ColorDrawable)Btn.getBackground();
                if(BtnColor.getColor() == Color.RED  || BtnColor.getColor() == Color.BLUE && list.size() < 3){
                        Btn.setBackgroundColor(Color.RED);
                        Btn.setEnabled(true);
                        test();
                        Case(list.size());
                        Log.d("MsTest", ">>" + list.size());
                }
                if (list.size() == 3) {
                    // 팝업창 설정
                    myDialog.setContentView(R.layout.inputranking);
                    myDialog.setTitle("랭킹");
                    myDialog.setCancelable(true);
                    gameName = (TextView)myDialog.findViewById(R.id.inputGameName);
                    inputScore = (TextView)myDialog.findViewById(R.id.inputScore);
                    EditText inputName = (EditText)myDialog.findViewById(R.id.inputName);

                    Button rankingInput = (Button)myDialog.findViewById(R.id.input);
                    gameName.setText("반응속도 테스트");
                    inputScore.setText(String.format("%.2f MS", result));

                    rankingInput.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String playerName = inputName.getText().toString();

                            //점수 DB전송 입력되는지 확인해야함ㅁㄴ
                            int result1 = skThread.sendDataToServer(playerName, (int)result, gameName.getText().toString());
                            Log.d("result", String.format("%.2f MS", result));
                            //result=1 입력 성공, 2 닉네임 중복
                            if(result1 == 1){
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
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void test(){
        Btn.setOnClickListener(MsTest.this);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 정상적 클릭 시
                if (Btn.isEnabled()) {
                    Btn.setBackgroundColor(Color.GREEN);
                    beforeTime = System.currentTimeMillis(); //현재 시간
                    Btn.setOnClickListener(MsTest.this);
                }
            }
        }, randomSeconds); // 2~5초 후 실행
    }
    
    //배너 창 클릭 후 게임 시작
    public void Click(View view){
        Text.setVisibility(View.VISIBLE);
        Btn.setVisibility(View.VISIBLE);
        StartBtn.setVisibility(View.VISIBLE);
        TextChance.setVisibility(View.VISIBLE);
        TextEx.setVisibility(View.INVISIBLE);
        TextEx.setEnabled(false);
        TextTouch.setVisibility(View.INVISIBLE);
        TextTouch.setEnabled(false);
    }

    @Override
    public void onClick(View view) {
        Btn.setEnabled(false); //연속클릭 방지
        ColorDrawable BtnColor = (ColorDrawable)Btn.getBackground();
        if(BtnColor.getColor() == Color.GREEN){
//            i++;
            afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간
            elapsedTime = afterTime - beforeTime;
            list.add(elapsedTime);
            listSum = 0;
            for (Double value : list) listSum += value;
            result = listSum / list.size();
            Text.setText(String.format("%.2f MS", result));
            Btn.setBackgroundColor(Color.BLUE);
        } else{
//            i--;
//            if (i<0) i++;
            Case(list.size());
            Text.setText("너무 빨라요.");
            Btn.setBackgroundColor(Color.BLUE);
        }
    }
    public void Case(int A){
        int B = A;
        switch (B) {
            case 0:
                Text.setText("-- MS");
                TextChance.setText("■□□");
                break;
            case 1:
                TextChance.setText("■■□");
                break;
            case 2:
                TextChance.setText("■■■");
                StartBtn.setText("등록");
                break;
            default:
                break;
        }
    }
}