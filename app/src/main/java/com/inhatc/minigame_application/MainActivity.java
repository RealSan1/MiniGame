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
        startActivity(intent);
    }
}