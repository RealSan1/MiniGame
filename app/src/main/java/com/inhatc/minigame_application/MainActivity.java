package com.inhatc.minigame_application;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private SocketThread skThread;
    private Animation anim;
    private TextView Text;
    private BackgroundSoundService bgmService;
    private boolean isBound = false;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            BackgroundSoundService.LocalBinder binder = (BackgroundSoundService.LocalBinder) service;
            bgmService = binder.getService();
            isBound = true;
            Log.d(TAG, "Service Connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
            Log.d(TAG, "Service Disconnected");
        }
    };

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

        // Start and bind the background music service
        Intent serviceIntent = new Intent(this, BackgroundSoundService.class);
        startService(serviceIntent);
        bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);

        // Start socket thread
        skThread = SocketThread.getInstance();
        skThread.start();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (skThread != null) {
            skThread.interrupt();
            skThread = null;
        }

        // Unbind and stop the background music service
        if (isBound) {
            unbindService(connection);
            isBound = false;
        }
        Intent serviceIntent = new Intent(this, BackgroundSoundService.class);
        stopService(serviceIntent);
    }

    public void MainClick(View view) {
        Intent intent = new Intent(getApplicationContext(), MainSelectActivity.class);
        Text.clearAnimation();
        startActivity(intent);
    }
    /** 음소거 기능

    public void mute(View view) {
        if (isBound && bgmService != null) {
            bgmService.mute();
        } else {
            Log.d(TAG, "Service is not bound or bgmService is null");
        }
    }
    **/
}
