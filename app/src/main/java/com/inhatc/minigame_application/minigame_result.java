package com.inhatc.minigame_application;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.inhatc.minigame_application.Pick;

import java.io.File;

public class minigame_result extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_minigame_result);

        TextView textView = findViewById(R.id.score);
        ImageView imageView = findViewById(R.id.resultimg);
        Button backBtn = findViewById(R.id.mini_button);
        backBtn.setOnClickListener(this);
        // 파일 경로 설정
        String filePath = getFilesDir() + File.separator + "image.jpg";

        // 파일을 Bitmap으로 디코딩
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);

        // 가져온 Bitmap을 ImageView에 설정
        imageView.setImageBitmap(bitmap);

        Pick pick = new Pick();
        int a = pick.pick(bitmap);

        textView.setText(String.valueOf(a));
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onClick(View view) {
        finish();
    }
}
