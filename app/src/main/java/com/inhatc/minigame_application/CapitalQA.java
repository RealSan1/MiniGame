package com.inhatc.minigame_application;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.Random;

public class CapitalQA extends AppCompatActivity {
    TextView question, answer, result, timerTV, num, gameName;
    EditText edtAnswer;
    Button btnNext, btnCommit, btnUp, btnDown, checkbtn;
    ImageView imgFlag;
    List<Map<String, String>> findFlag;
    private static final long START_TIME_IN_MILLIS = 15000;
    SocketThread skThread;
    int count = 0; // 문제 진행률 카운터
    int correct = 0; // 문제 정답률 카운터
    List<Map.Entry<String, String>> randomDataList;
    CountDownTimer countDownTimer;
    Dialog myDialog;

    int numOfQ, temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_capital_qa);



        question = (TextView) findViewById(R.id.txtQuestion);
        answer = (TextView) findViewById(R.id.txtAnswer);
        result = (TextView) findViewById(R.id.txtResult);
        timerTV = (TextView) findViewById(R.id.txtTimer);
        edtAnswer = (EditText) findViewById(R.id.editTextAnswer);
        btnCommit = (Button) findViewById(R.id.btnCommit);
        btnNext = (Button) findViewById(R.id.btnNext);
        imgFlag = (ImageView) findViewById(R.id.imgFlag);

        //팝업창 설정 시작
        myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.numofquestion);
        myDialog.setTitle("문제 개수");
        myDialog.setCancelable(true);
        num = (TextView) myDialog.findViewById(R.id.NumOfQuestionSetting);
        gameName = (TextView) myDialog.findViewById(R.id.inputGameName);
        btnUp = (Button) myDialog.findViewById(R.id.countupbtn);
        btnDown = (Button) myDialog.findViewById(R.id.countdownbtn);
        checkbtn = (Button) myDialog.findViewById(R.id.NumOfQuestionInputBtn);
        gameName.setText("수도 맞히기");
        myDialog.show();

        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                temp = Integer.parseInt(num.getText().toString());
                temp += 5;
                if (temp > 50) temp = 50;
                num.setText(String.valueOf(temp));
            }
        });

        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                temp = Integer.parseInt(num.getText().toString());
                temp -= 5;
                if (temp < 5) temp = 5;
                num.setText(String.valueOf(temp));
            }
        });

        checkbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numOfQ = Integer.parseInt(num.getText().toString());
                Log.d("CapitalQA", "Number of Questions: " + numOfQ);

                skThread = SocketThread.getInstance();

                //서버로 부터 받아온 mysql json형식의 데이터
                String data = skThread.getq_a();
                Log.d("CapitalQA", "Data received: " + data);

                // DB에서 가져온 데이터 파싱해 Map 형태로 받아옴
                Map<String, String> parseData = ParseData(data);
                Log.d("CapitalQA", "Parsed Data: " + parseData.toString());

                // 랜덤 문제 리스트 설정
                randomDataList = getRandomEntries(parseData, numOfQ);
                Log.d("Random Data List", randomDataList.toString());

                // 타이머 설정
                setTimer();

                // 문제 설정
                setQuestion(count);
                myDialog.dismiss();
            }
        });

        //팝업 설정 끝

        // 모바일 키보드 제출 버튼 또는 키보드 엔터키 눌렀을 때 Commit 메소드 실행
        edtAnswer.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Commit(btnCommit);
                    // 키보드 숨기기
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edtAnswer.getWindowToken(), 0);
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
        findFlag = new ArrayList<>();
        Map<String, String> engCountryMap = new HashMap<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String country = jsonObject.getString("country");
                String capital = jsonObject.getString("capital");
                countryCapitalMap.put(country, capital);

                String countryEng = jsonObject.getString("country_eng");
                engCountryMap.put(country, countryEng);
                findFlag.add(engCountryMap);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return countryCapitalMap;
    }

    // DB에서 가져온 데이터를 원하는 수만큼 랜덤으로 추출
    protected static List<Map.Entry<String, String>> getRandomEntries(Map<String, String> originalMap, int count) {
        List<Map.Entry<String, String>> entryList = new ArrayList<>(originalMap.entrySet());
        Collections.shuffle(entryList, new Random()); // 무작위로 섞음

        return entryList.subList(0, Math.min(count, entryList.size())); // 원하는 수만큼 반환
    }

    // 타이머 설정
    public void setTimer() {
        timerTV.setTextColor(Color.WHITE);
        countDownTimer = new CountDownTimer(START_TIME_IN_MILLIS, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int secondsRemaining = (int) (millisUntilFinished / 1000);
                timerTV.setText(String.valueOf(secondsRemaining));
                if (secondsRemaining < 6) {
                    timerTV.setTextColor(Color.RED);
                }
                if (secondsRemaining == 0) {
                    onFinish();
                }
            }

            @Override
            public void onFinish() {
                timerTV.setText("종료");
                Commit(null);
            }
        }.start();
    }

    private void resetTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        setTimer();
    }

    // 문제 생성
    private void setQuestion(int index) {
        Map.Entry<String, String> entry = randomDataList.get(index);
        question.setText(entry.getKey());
        answer.setText(entry.getValue());

        // 이미지 탐색 후 설정
        String countryEng = "";
        for (int i = 0; i < findFlag.size(); i++) {
            Map<String, String> engMap = findFlag.get(i);
            countryEng = engMap.get(entry.getKey()).toLowerCase();
        }
        System.out.println(countryEng);
        int resId = getResources().getIdentifier(countryEng, "drawable", getPackageName());
        System.out.println(resId);
        BitmapDrawable img = (BitmapDrawable) getResources().getDrawable(resId);
        imgFlag.setImageDrawable(img);
    }

    public void Commit(View view) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        String userAnswer = edtAnswer.getText().toString().trim();
        String correctAnswer = randomDataList.get(count).getValue();

        if (userAnswer.equalsIgnoreCase(correctAnswer)) {
            result.setText("정답!");
            correct++;
        } else if ("종료".equals(timerTV.getText())) {
            result.setText("타임 오버!");
        } else {
            result.setText("오답!");
        }

        result.setVisibility(View.VISIBLE);
        edtAnswer.setVisibility(View.INVISIBLE);
        answer.setVisibility(View.VISIBLE);
        btnCommit.setVisibility(View.INVISIBLE);
        btnNext.setVisibility(View.VISIBLE);
    }

    public void Next(View view) {
        count++;
        if (count < randomDataList.size()) {
            setQuestion(count);
            resetTimer();
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
            intent.putExtra("total", randomDataList.size());
            startActivity(intent);
            finish();
        }
    }
}
