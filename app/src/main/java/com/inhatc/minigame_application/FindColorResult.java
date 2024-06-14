package com.inhatc.minigame_application;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class FindColorResult extends AppCompatActivity implements View.OnClickListener {
    private SocketThread skThread = SocketThread.getInstance();
    Dialog myDialog;
    TextView gameName, inputScore;
    int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_findcolor_result);

        TextView scoreText = findViewById(R.id.score);
        ImageView imageView = findViewById(R.id.resultimg);
        Button submit = findViewById(R.id.mini_button);
        submit.setOnClickListener(this); // Registering the button click listener

        // 파일 경로 설정
        String filePath = getFilesDir() + File.separator + "image.jpg";

        // 파일을 Bitmap으로 디코딩
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);

        if (bitmap != null) { // Null check
            // 가져온 Bitmap을 ImageView에 설정
            imageView.setImageBitmap(bitmap);

            Intent getIntent = new Intent(this.getIntent());
            int value = getIntent.getIntExtra("colorInfoValue", Color.BLACK);

            Log.d("AA", ">>" + getIntent.getStringExtra("colorInfoKey"));

            PickColor pick = new PickColor();
            score = pick.pick(bitmap, value); // Use class-level score variable
            String a = " 점수: " + score;
            scoreText.setText(a);
        } else {
            Log.d("Bitmap Error", "Bitmap is null.");
        }

        //팝업창 띄우기
        myDialog = new Dialog(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onClick(View view) {
        // 팝업창 설정
        myDialog.setContentView(R.layout.inputranking);
        myDialog.setTitle("랭킹");
        myDialog.setCancelable(true);
        gameName = myDialog.findViewById(R.id.inputGameName);
        inputScore = myDialog.findViewById(R.id.inputScore);
        EditText inputName = myDialog.findViewById(R.id.inputName);

        gameName.setText("색깔 맞추기");
        inputScore.setText(String.valueOf(score));

        Button rankingInput = myDialog.findViewById(R.id.input);
        rankingInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String playerName = inputName.getText().toString();
                int result = skThread.sendDataToServer(playerName, score, gameName.getText().toString());
                // result=1 입력 성공, 2 닉네임 중복
                if(result == 1){
                    Log.d("result", "입력 성공");
                    finish();
                }else{
                    Log.d("result", "입력 실패");
                    finish();
                }
            }
        });
        myDialog.show(); // Show the dialog
    }
}
