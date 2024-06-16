package com.inhatc.minigame_application;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class FindColor extends AppCompatActivity implements View.OnClickListener {

    private static final int pic_id = 123;
    //아이디 생성
    Button Cbtn;
    HashMap<String, Integer> ColorMap = new HashMap<String, Integer>();
    ImageView imageView;
    TextView ColorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_find_color);

        Cbtn = findViewById(R.id.Cbtn);
        imageView = findViewById(R.id.imageView);
        ColorText = findViewById(R.id.textView);
        ColorMap.put("BLACK",Color.BLACK);
        ColorMap.put("WHITE",Color.WHITE);
        ColorMap.put("RED",Color.RED);
        ColorMap.put("GREEN",Color.GREEN);
        ColorMap.put("BLUE",Color.BLUE);
        ColorMap.put("YELLOW",Color.YELLOW);
        ColorMap.put("CYAN",Color.CYAN);
        ColorMap.put("GRAY",Color.GRAY);
        ColorMap.put("DARKGRAY",Color.DKGRAY);
        ColorMap.put("LTGRAY",Color.LTGRAY);
        ColorMap.put("MAGENTA",Color.MAGENTA);


        List<String> keysList = new ArrayList<>(ColorMap.keySet());
        Random random = new Random();
        String key = keysList.get(random.nextInt(keysList.size()));

        // 선택된 키에 대응하는 값을 가져오기
        Integer value = ColorMap.get(key);

        Intent SendIntent = new Intent(this, FindColorResult.class);
//        SendIntent.putExtra("colorInfoKey", key);
        SendIntent.putExtra("colorInfoValue", value);
        Log.d("FindColor", "colorInfo 송신 " +key + ", "+ value);
        ColorText.setText(key);
        ColorText.setTextColor(value);

        Cbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                camera();
            }
        });

        Button NextBtn = (Button) findViewById(R.id.button2);
        NextBtn.setOnClickListener(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Log.d("FindColor", "onCreate");
    }


    public void camera() {
        Log.d("FindColor", "카메라 버튼 클릭");
        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Start the activity with camera_intent, and request pic id
        startActivityForResult(camera_intent, pic_id);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == pic_id) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Log.d("Bitmap", photo.toString());
            imageView.setImageBitmap(photo);
            saveBitmapAsJpeg(photo);
        }
    }

    private void saveBitmapAsJpeg(Bitmap bitmap) {
        // 내부 저장소에 저장할 파일 경로 설정
        File imageFile = new File(getFilesDir(), "image.jpg");

        try {
            // 비트맵을 JPEG 파일로 압축하여 내부 저장소에 저장
            FileOutputStream fos = openFileOutput("image.jpg", Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
            Log.d("FilePath", "이미지 파일이 저장된 경로: " + imageFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("FilePath", "이미지 저장 실패");
        }
    }

    public void Back(View view) {
        finish();
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getApplicationContext(), FindColorResult.class);
        startActivity(intent);

    }
}
