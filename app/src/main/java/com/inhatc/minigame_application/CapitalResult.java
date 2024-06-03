package com.inhatc.minigame_application;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CapitalResult extends AppCompatActivity {
    Dialog myDialog;
    TextView gameName, inputScore;
    int score;
    private SocketThread skThread = SocketThread.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_capital_result);

        int correct = getIntent().getIntExtra("correct", 0);
        int total = getIntent().getIntExtra("total", 1);

        score = (int) (((double) correct / total) * 100);

        TextView result = findViewById(R.id.txtCapResult);
        TextView scorePer = findViewById(R.id.txtCapScore);

        result.setText(correct + "개 정답!");
        scorePer.setText(score + "%"); // 점수 뒤에 % 추가

        myDialog = new Dialog(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void resultPopup(View v){
        myDialog.setContentView(R.layout.inputranking);
        myDialog.setTitle("랭킹");
        myDialog.setCancelable(true);
        gameName = (TextView)myDialog.findViewById(R.id.inputGameName);
        inputScore = (TextView)myDialog.findViewById(R.id.inputScore);
        EditText inputName = (EditText)myDialog.findViewById(R.id.inputName);

        Button rankingInput = (Button)myDialog.findViewById(R.id.input);
        gameName.setText("수도 맞히기");

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
    public void Cancel(View v){
        finish();
    }
}
