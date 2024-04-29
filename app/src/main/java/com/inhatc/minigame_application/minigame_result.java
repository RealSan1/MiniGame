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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.inhatc.minigame_application.Pick;

import java.io.File;

public class minigame_result extends AppCompatActivity {
    private SocketThread skThread = SocketThread.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_minigame_result);

        TextView textView = findViewById(R.id.score);
        ImageView imageView = findViewById(R.id.resultimg);
        // 파일 경로 설정
        String filePath = getFilesDir() + File.separator + "image.jpg";

        // 파일을 Bitmap으로 디코딩
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);

        // 가져온 Bitmap을 ImageView에 설정
        imageView.setImageBitmap(bitmap);

        Pick pick = new Pick();
        int a = pick.pick(bitmap);
        //a 점수 가져옴

        textView.setText(String.valueOf(a));
        //점수 표현
        EditText txtName = findViewById(R.id.txtName);
        Button btn_scoreSave = (Button)findViewById(R.id.btnScoreSave);

        btn_scoreSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //DB연결부
                String playerName = txtName.getText().toString();

                if(playerName.isEmpty()){
                    Toast.makeText(minigame_result.this, "이름을 입력해주세요",Toast.LENGTH_SHORT).show();
                }else{
                    //이름,점수,게임이름 데이터 전송 후 DB삽입
                    int result = skThread.sendDataToServer(playerName,a,"색깔 맞추기 게임");
                    //result=1 입력 성공, 2 닉네임 중복
                    if(result == 1){
                        Toast.makeText(minigame_result.this,"성공적으로 등록되었습니다.",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(minigame_result.this,"서버에러.",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
