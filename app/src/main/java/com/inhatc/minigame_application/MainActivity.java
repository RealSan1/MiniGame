package com.inhatc.minigame_application;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private SocketThread skThread;
    Animation anim;
    TextView Text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        Text = findViewById(R.id.txtTouchScreen);
        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        Text.startAnimation(anim);

        //소켓 스레드 생성 후 시작(앱 시작 시 서버와 접속)
        skThread = SocketThread.getInstance();
        skThread.start();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    @Override
    protected void onDestroy() {//앱이 종료시 소켓스레드도 종료
        super.onDestroy();
        if(skThread!=null){
            skThread.interrupt();
            skThread=null;
        }
    }

    public void MainClick(View view) {
        Intent intent = new Intent(getApplicationContext(), MainSelectActivity.class);
        Text.clearAnimation();
        startActivity(intent);
    }


}