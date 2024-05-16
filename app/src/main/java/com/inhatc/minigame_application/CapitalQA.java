package com.inhatc.minigame_application;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CapitalQA extends AppCompatActivity {
    private TextView question, answer, result, timerTV;
    private EditText edtAnswer;
    private Button btnNext, btnCommit;
    private static final long START_TIME_IN_MILLIS = 30000;
    private SocketThread skThread;
    int numOfQ = getIntent().getIntExtra("num",1); // 문제 개수 가져오기
    private int count = 0; // 문제 진행률 카운터
    private int correct = 0; // 문제 정답률 카운터
    private List<Map.Entry<String, String>> randomDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_capital_qa);

        skThread = SocketThread.getInstance();

        //서버로 부터 받아온 mysql json형식의 데이터
        String data = skThread.getq_a();
        System.out.println(data);

        // DB에서 가져온 데이터 파싱해 Map 형태로 받아옴
        Map<String, String> parseData = ParseData(data);
        System.out.println(parseData);

        // 가져온 데이터를 문제 개수만큼 랜덤으로 추출해서 리스트에 저장
        randomDataList = new ArrayList<>(getRandomEntries(parseData, numOfQ).entrySet());
        System.out.println(randomDataList);

        question = (TextView)findViewById(R.id.txtQuestion);
        answer = (TextView)findViewById(R.id.txtAnswer);
        result = (TextView)findViewById(R.id.txtResult);
        timerTV = (TextView)findViewById(R.id.txtTimer);
        edtAnswer = (EditText)findViewById(R.id.editTextAnswer);
        btnCommit = (Button)findViewById(R.id.btnCommit);
        btnNext = (Button)findViewById(R.id.btnNext);

        // 타이머 설정
        setTimer();

        //문제 설정
        setQuestion(count);

        edtAnswer.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Commit(btnCommit);
                    return true;
                }
                return false;
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // DB에서 가져온 데이터 파싱
    protected Map<String, String> ParseData(String json) {
        Map<String, String> countryCapitalMap = new HashMap<>();

        try {
            JSONArray jsonArray = new JSONArray(json);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String country = jsonObject.getString("country");
                String capital = jsonObject.getString("capital");
                countryCapitalMap.put(country, capital);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return countryCapitalMap;
    }
    
    // DB에서 가져온 데이터를 원하는 수만큼 랜덤으로 추출
    protected Map<String, String> getRandomEntries(Map<String, String> originalMap, int count) {
        List<Map.Entry<String, String>> entryList = new ArrayList<>(originalMap.entrySet());
        Collections.shuffle(entryList);
        Map<String, String> randomEntriesMap = new HashMap<>();

        for (int i = 0; i < Math.min(count, entryList.size()); i++) {
            Map.Entry<String, String> entry = entryList.get(i);
            randomEntriesMap.put(entry.getKey(), entry.getValue());
        }

        return randomEntriesMap;
    }

    // 가져온 DB 데이터에서 i번째 데이터 추출
    protected Map.Entry<String, String> getIEntry(Map<String, String> map, int i) {
        List<Map.Entry<String, String>> entryList = new ArrayList<>(map.entrySet());
        if (i >= 0 && i < entryList.size()) {
            return entryList.get(i); // i번째 항목 반환
        } else {
            return null; // 맵에 i번째 항목이 없을 경우 null 반환
        }
    }

    // 타이머 설정
    public void setTimer(){
        new CountDownTimer(START_TIME_IN_MILLIS, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                int secondsRemaining = (int) (millisUntilFinished / 1000);
                timerTV.setText(String.valueOf(secondsRemaining));
                if (secondsRemaining < 6) {
                    timerTV.setTextColor(Color.RED);
                }
                if(secondsRemaining == 0){
                    onFinish();
                }
            }
            @Override
            public void onFinish() {
                timerTV.setText("종료");
            }
        }.start();
    }

    private void setQuestion(int index) {
        Map.Entry<String, String> entry = randomDataList.get(index);
        question.setText(entry.getKey());
        answer.setText(entry.getValue());
    }

    public void Commit(View view){
        String userAnswer = edtAnswer.getText().toString().trim();
        String correctAnswer = randomDataList.get(count).getValue();

        if (userAnswer.equalsIgnoreCase(correctAnswer)) {
            result.setText("정답!");
            correct++;
        } else {
            result.setText("오답!");
        }

        result.setVisibility(View.VISIBLE);
        edtAnswer.setVisibility(View.INVISIBLE);
        answer.setVisibility(View.VISIBLE);
        btnCommit.setVisibility(View.INVISIBLE);
        btnNext.setVisibility(View.VISIBLE);
    }

    public void Next(View view){
        count++;
        if (count < randomDataList.size()) {
            setQuestion(count);
            result.setVisibility(View.INVISIBLE);
            edtAnswer.setVisibility(View.VISIBLE);
            edtAnswer.setText("");
            answer.setVisibility(View.INVISIBLE);
            btnCommit.setVisibility(View.VISIBLE);
            btnNext.setVisibility(View.INVISIBLE);
        } else {
            // 모든 문제를 다 푼 경우 결과 페이지로 이동
            Intent intent = new Intent(CapitalQA.this, CapitalResult.class);
            intent.putExtra("correct", correct);
            intent.putExtra("total", numOfQ);
            startActivity(intent);
            finish();
        }
    }
}