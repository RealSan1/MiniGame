package com.inhatc.minigame_application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SelectGameActivity extends AppCompatActivity {

    public static Intent intent;
    public static int value;
    public static String selectType;
    public String title;
    public TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_select_game);

        intent = getIntent();
        value = intent.getIntExtra("select", -1);
        selectType = intent.getStringExtra("type");
        title = "  게임 리스트 (" + selectType + ")";

        textView = (TextView)findViewById(R.id.textView2);
        textView.setText(title);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void Find_Color(View view) {
        if(value == 0){
            intent = new Intent(getApplicationContext(), RankActivity.class);
            RankActivity.receiveGamename("색깔 맞추기 게임");
            startActivity(intent);
        }else if(value == 1) {
            intent = new Intent(getApplicationContext(), FindColor.class);
            startActivity(intent);
        }
    }

    public void MsTest(View view){
        if(value == 0){
            intent = new Intent(getApplicationContext(), RankActivity.class);
            RankActivity.receiveGamename("반응속도 테스트 게임");
            startActivity(intent);
        }else if(value == 1) {
            intent = new Intent(getApplicationContext(), MsTest.class);
            startActivity(intent);
        }
    }

    public void Capital(View view){
        if(value==0){
            intent = new Intent(getApplicationContext(), RankActivity.class);
            RankActivity.receiveGamename("수도 맞히기");
            startActivity(intent);
        }else if(value == 1) {
            intent = new Intent(getApplicationContext(), CapitalQA.class);
            intent.putExtra("gameType", "Capital");
            startActivity(intent);
        }
    }

    public void BlockPuzzle(View view){
        if(value == 0){
            intent = new Intent(getApplicationContext(), RankActivity.class);
            RankActivity.receiveGamename("블럭 맞추기");
            startActivity(intent);
        }else if(value == 1) {
            intent = new Intent(getApplicationContext(), BlockPuzzle.class);
            startActivity(intent);
        }
    }

    public void Person(View view){
        if(value == 0){
            intent = new Intent(getApplicationContext(), RankActivity.class);
            RankActivity.receiveGamename("인물 맞히기");
            startActivity(intent);
        }else if(value == 1) {
            intent = new Intent(getApplicationContext(), PersonQA.class);
            intent.putExtra("gameType", "Person");
            startActivity(intent);
        }
    }

    public void Tetris(View view){
        if(value == 0){
            intent = new Intent(getApplicationContext(), RankActivity.class);
            RankActivity.receiveGamename("테트리스");
            startActivity(intent);
        }else if(value == 1) {
            intent = new Intent(getApplicationContext(), Tetris.class);
            startActivity(intent);
        }
    }

    public void Swipe(View view){
        if(value == 0){
            intent = new Intent(getApplicationContext(), RankActivity.class);
            RankActivity.receiveGamename("롤 궁극기 맞히기");
            startActivity(intent);
        }else if(value == 1) {
            intent = new Intent(getApplicationContext(), Swipe.class);
            intent.putExtra("gameType", "LOLUlt");
            startActivity(intent);
        }
    }

    public void Country(View view){
        if(value == 0){
            intent = new Intent(getApplicationContext(), RankActivity.class);
            RankActivity.receiveGamename("나라 이름 맞히기");
            startActivity(intent);
        }else if(value == 1) {
            intent = new Intent(getApplicationContext(), CountryQA.class);
            intent.putExtra("gameType", "Country");
            startActivity(intent);
        }
    }

    public void LOLUlt(View view){
        if(value == 0){
            intent = new Intent(getApplicationContext(), RankActivity.class);
            RankActivity.receiveGamename("롤 궁극기 맞히기");
            startActivity(intent);
        }else if(value == 1) {
            intent = new Intent(getApplicationContext(), LOLUltQA.class);
            intent.putExtra("gameType", "LOLUlt");
            startActivity(intent);
        }
    }

}