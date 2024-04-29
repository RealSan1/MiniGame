package com.inhatc.minigame_application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private SocketThread skThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //소켓스레드 생성
        skThread = SocketThread.getInstance();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //소켓 스레드 시작(백그라운드 소켓통신)
        skThread.start();
    }

    @Override
    protected void onDestroy() {//앱이 종료시 스레드 종료(소켓통신종료)
        super.onDestroy();
        if(skThread!=null){
            skThread.interrupt();
            skThread=null;
        }
    }

    public void MainClick(View view) {
        Intent intent = new Intent(getApplicationContext(), MainSelectActivity.class);
        startActivity(intent);
    }
}