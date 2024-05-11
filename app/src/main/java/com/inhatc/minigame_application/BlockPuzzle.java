package com.inhatc.minigame_application;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Random;

public class BlockPuzzle extends AppCompatActivity implements View.OnClickListener {

    Button Rbtn, Gbtn, Bbtn;
    ImageView BImg1, BImg2, BImg3, BImg4, BImg5;
    int ColorR, ColorG, ColorB, randomColor, score;
    ArrayList<Integer> list = new ArrayList<>();
    Random random = new Random();
    Boolean isClicked = false;
    TextView scoreText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_block_puzzle);

        scoreText = findViewById(R.id.BlockScoreText);
        Rbtn = findViewById(R.id.BlockBtnR);
        Gbtn = findViewById(R.id.BlockBtnG);
        Bbtn = findViewById(R.id.BlockBtnB);
        BImg1 = findViewById(R.id.BlockImg1);
        BImg2 = findViewById(R.id.BlockImg2);
        BImg3 = findViewById(R.id.BlockImg3);
        BImg4 = findViewById(R.id.BlockImg4);
        BImg5 = findViewById(R.id.BlockImg5);

        ColorR = R.drawable.red;
        ColorG = R.drawable.blue;
        ColorB = R.drawable.green;

        list.add(ColorR);
        list.add(ColorG);
        list.add(ColorB);

        BImg1.setImageResource(list.get(random.nextInt(list.size())));
        BImg2.setImageResource(list.get(random.nextInt(list.size())));
        BImg3.setImageResource(list.get(random.nextInt(list.size())));
        BImg4.setImageResource(0);
        BImg5.setImageResource(0);

        GradientDrawable drawR = (GradientDrawable)Rbtn.getBackground();
        drawR.setColor(Color.RED);
        GradientDrawable drawG = (GradientDrawable)Gbtn.getBackground();
        drawG.setColor(Color.GREEN);
        GradientDrawable drawB = (GradientDrawable)Bbtn.getBackground();
        drawB.setColor(Color.BLUE);

        Rbtn.setOnClickListener(this);
        Gbtn.setOnClickListener(this);
        Bbtn.setOnClickListener(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onClick(View view) {
        randomColor = list.get(random.nextInt(list.size())); //클릭 시 랜덤 색 추출
        if (view == Rbtn && BImg3.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.red).getConstantState())) {
            // 일치하면 점수 증가
            score++;
        } else if (view == Gbtn && BImg3.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.green).getConstantState())) {
            // 일치하면 점수 증가
            score++;
        } else if (view == Bbtn && BImg3.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.blue).getConstantState())) {
            // 일치하면 점수 증가
            score++;
        }
        if(isClicked) BImg5.setImageDrawable(BImg4.getDrawable());
        BImg4.setImageDrawable(BImg3.getDrawable());
        BImg3.setImageDrawable(BImg2.getDrawable());
        BImg2.setImageDrawable(BImg1.getDrawable());
        BImg1.setImageResource(randomColor);
        isClicked = true;
        scoreText.setText(String.valueOf(score * 10));

    }
}