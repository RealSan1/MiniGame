package com.inhatc.minigame_application;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class BackgroundSoundService extends Service {
    private static final String TAG = "BackgroundSoundService";
    private MediaPlayer player;
    private final IBinder binder = new LocalBinder();

    public class LocalBinder extends Binder {
        BackgroundSoundService getService() {
            return BackgroundSoundService.this;
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service Created");
        player = MediaPlayer.create(this, R.raw.main);
        player.setLooping(true);
        player.setVolume(1.0f, 1.0f); // Adjust volume if needed
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        player.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        player.stop();
        player.release();
        super.onDestroy();
    }

    public void mute() {
        if (player != null) {
            if (player.isPlaying()) {
                player.pause();
                Log.d(TAG, "Music Paused");
            } else {
                player.start();
                Log.d(TAG, "Music Started");
            }
        }
    }

    @Override
    public void onLowMemory() {
        // Handle low memory if needed
    }
}
