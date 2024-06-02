package com.inhatc.minigame_application;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class Tetris extends AppCompatActivity {
    private SocketThread skThread = SocketThread.getInstance();
    private static final int ROWS = 20;
    private static final int COLS = 11;
    private static final int N_ROWS = 4;
    private static final int N_COLS = 4;
    private static View[][] cells = new View[ROWS][COLS];//진짜 보드판
    private int[][] v_board = new int[ROWS][COLS];//가상의 보드판
    Button btnRight;
    Button btnLeft;
    Button btnBottom;
    Button btnFast;
    Button btnTurn;
    private Seven_Block[] s_blocks;//7가지 블럭을 담고있는 배열
    private Seven_Block currentBlock;//현재의 블럭
    private Seven_Block nextBlock;//다음 블럭
    private int currentBlockRow;//블럭과 보드의 현재행
    private int currentBlockCol;//블럭과 보드의 현재열
    private int bottomBlockRow;//가장 밑 보드의 행
    private int bottomBlockCol;//가장 밑 보드의 열
    private Handler handler;//타이머
    private Runnable runnable; //스레드 동작
    private static final int INTERVAL = 1500;// 시간 1.5초
    //색깔블럭 지정
    //I=mint, L=orange, J=blue, o=yellow, s=green, z=red, T=pupple
    private Drawable Zblock;
    private Drawable Lblock;
    private Drawable Iblock;
    private Drawable Jblock;
    private Drawable Oblock;
    private Drawable Sblock;
    private Drawable Tblock;
    private static Drawable currentDrawableBlock;//현재 무슨 색깔블럭인지
    private static Drawable nextDrawableBlock;//다음 블럭이 무슨 색깔인지
    private CountDownTimer countDownTimer;//시간제한
    private TextView timerTV, scoreTV, gameName, inputScore;//시간 점수 팝업게임명, 점수삽입
    private static int realScore; //진짜 점수용 변수
    private static final long START_TIME_IN_MILLIS = 60000;//시간 제한 1분
    Dialog myDialog;    //끝 팝업창
    Button Start;
    FrameLayout popup;  //시작 팝업창
    Button startBtn;//시작 팝업창 시작 버튼


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tetris);
        //버튼생성
        btnLeft = findViewById(R.id.btnLeft);
        btnRight = findViewById(R.id.btnRight);
        btnBottom = findViewById(R.id.btnBottom);
        btnTurn = findViewById(R.id.btnTurn);
        btnFast = findViewById(R.id.btnFastBottom);
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveBlockLeft();
            }
        });
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveBlockRight();
            }
        });
        btnBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveBlockDown();
            }
        });
        btnTurn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotateBlock();
            }
        });
        btnFast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveBlockDrop();
            }
        });

        //색깔블럭 지정
        //I=mint, L=orange, J=blue, o=yellow, s=green, z=red, T=pupple
        Zblock = ContextCompat.getDrawable(this, R.drawable.border_red);
        Lblock = ContextCompat.getDrawable(this, R.drawable.border_orange);
        Iblock = ContextCompat.getDrawable(this, R.drawable.border_mint);
        Jblock = ContextCompat.getDrawable(this, R.drawable.border_blue);
        Oblock = ContextCompat.getDrawable(this, R.drawable.border_yellow);
        Sblock = ContextCompat.getDrawable(this, R.drawable.border_green);
        Tblock = ContextCompat.getDrawable(this, R.drawable.border_pupple);

        //팝업 창 초기화
        popup = findViewById(R.id.popup);
        //시작버튼 초기화
        startBtn = findViewById(R.id.startBtn);
        //타이머 텍스트뷰 초기화
        timerTV = findViewById(R.id.txtTimer);

        myDialog = new Dialog(this);

        //시작 버튼 클릭 설정
        startBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //팝업창 숨기기
                popup.setVisibility(View.GONE);
                GameStart();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void GameStart(){
        scoreTV = findViewById(R.id.txtScore);//점수
        scoreTV.setText("00000");
        realScore = 0;
        setTimer();//타이머 시작

        System.out.println("게임 시작 ");

        //게임판 생성
        GridLayout tetrisBoard = findViewById(R.id.tetrisBoard);
        initGridBoard(tetrisBoard);

        //7가지의 블럭을 생성 후 이를 토대로 랜덤 가져오기
        s_blocks = Seven_Block.createBlocks();
        spawnRandomBlock();
        spawnBlock();

        //특정 시간마다 블럭이 1행씩 내려오기
        handler = new Handler();
        runnable = new Runnable(){
            @Override
            public void run() {
                moveBlockDown();
                handler.postDelayed(this, INTERVAL);
            }
        };
        handler.postDelayed(runnable, INTERVAL);
    }

    //보드판 생성
    private void initGridBoard(GridLayout gridLayout){
        gridLayout.setRowCount(ROWS);
        gridLayout.setColumnCount(COLS);

        Drawable borderDrawable = ContextCompat.getDrawable(this, R.drawable.border);
        Drawable border_shape = ContextCompat.getDrawable(this,R.drawable.border_shape);

        for(int row = 0; row<ROWS; row++){
            for(int col = 0; col<COLS; col++){
                View cell = new View(this);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 0;
                params.height = 0;
                params.rowSpec = GridLayout.spec(row, 1f);
                params.columnSpec = GridLayout.spec(col, 1f);

                if(row == ROWS-1){
                    cell.setBackground(border_shape);
                }else if(col == 0 || col == COLS-1){
                    cell.setBackground(border_shape);
                }else{
                    cell.setBackground(borderDrawable);
                }
                gridLayout.addView(cell, params);
                cells[row][col] = cell;
                if(row==ROWS-1 || col==0 || col==COLS-1){
                    v_board[row][col] = 1; //벽을 1로 설정
                }else{
                    v_board[row][col]=0; //빈공간을 0으로 설정
                }
            }
        }
    }
    //블럭의 색깔 구현
    private Drawable makeBlockDrawable(int index){
        //0:I, 1:J, 2:L, 3:O, 4:S, 5:T, 6:Z
        switch(index){
            case 0:
                System.out.println(index+"번 I블럭");
                return Iblock;
            case 1:
                System.out.println(index+"번 J블럭");
                return Jblock;
            case 2:
                System.out.println(index+"번 L블럭");
                return Lblock;
            case 3:
                System.out.println(index+"번 O블럭");
                return Oblock;
            case 4:
                System.out.println(index+"번 S블럭");
                return Sblock;
            case 5:
                System.out.println(index+"번 T블럭");
                return Tblock;
            case 6:
                System.out.println(index+"번 Z블럭");
                return Zblock;
        }
        return null;//에러
    }

    //랜덤 블럭
    private void spawnRandomBlock(){
        Random random = new Random();

        int blockIndex = random.nextInt(s_blocks.length);
        //0~6까지의 랜덤값을 토대로 nextBlock에 블럭 넣기
        nextBlock = s_blocks[blockIndex];
        //블럭의 색깔을 지정
        //I=mint, L=orange, J=blue, o=yellow, s=green, z=red, T=pupple
        nextDrawableBlock = makeBlockDrawable(blockIndex);
        drawNextBlock();//다음 블럭뭔지 확인 함수
        //spawnBlock(nextBlock); //실제 보드판에 블럭생성
    }
    //랜덤블럭에 근거하여 보드판에 블럭 생성
    private void spawnBlock(){
        currentBlock = nextBlock;
        currentDrawableBlock = nextDrawableBlock;
        currentBlockRow = 0;
        currentBlockCol =4;

        //블럭이 동작중이라면 보드 생성 대기
        drawBlock();
        spawnRandomBlock();
    }
    //다음에 어떤 블럭이 나올지 미리 확인하는 함수
    private void drawNextBlock() {
        int[][]nextBlockShape = nextBlock.getCurrentShape();//다음에 등장할 블럭
        Drawable blockDrawble = nextDrawableBlock;
        GridLayout nextBlockGrid = findViewById(R.id.nextBlock);
        nextBlockGrid.removeAllViews();//이전에 그려진 블럭 삭제

        for (int i = 0; i < N_ROWS && i < nextBlockShape.length; i++) {
            for (int j = 0; j < N_COLS && j < nextBlockShape[i].length; j++) {
                View cell = new View(this);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 0;
                params.height = 0;
                params.rowSpec = GridLayout.spec(i, 1f);
                params.columnSpec = GridLayout.spec(j, 1f);
                if (nextBlockShape[i][j] == 1) {
                    cell.setBackground(blockDrawble);
                } else {
                    cell.setBackgroundColor(Color.TRANSPARENT);
                }
                nextBlockGrid.addView(cell, params);
            }
        }
    }

    //블럭 보드판에 재구축
    private void drawBlock(){
        int[][] shape = currentBlock.getCurrentShape();//현재 블럭 가져오기
        Drawable blockDrawble = currentDrawableBlock;
        for(int i = 0; i< shape.length; i++){
            for(int j = 0; j<shape[i].length; j++){
                if(shape[i][j] != 0){
                    int row = currentBlockRow + i;
                    int col = currentBlockCol + j;
                    if(v_board[row][col] == 3){
                        //게임 오버
                        gameOverBlock();
                    }
                    cells[row][col].setBackground(blockDrawble);  //실제 보여지는 블럭
                    v_board[row][col] = 2;//현재 블럭을 2로 표시  가상블럭에
                }
            }
        }
    }

    //현재위치의 블럭을 삭제
    private void clearBlock(){
        int[][] shape = currentBlock.getCurrentShape();
        Drawable blockDrawable = ContextCompat.getDrawable(this, R.drawable.border);

        for(int i = 0; i<shape.length; i++){
            for(int j = 0; j<shape.length; j++){
                if(shape[i][j] != 0){
                    int row = currentBlockRow + i;
                    int col = currentBlockCol + j;
                    cells[row][col].setBackground(blockDrawable);
                    v_board[row][col]=0;//빈 공간은 0으로 표시
                }
            }
        }
    }

    //블럭이 내려가는 함수
    private void moveBlockDown(){
        if(canMoveDown()){
            clearBlock();
            currentBlockRow++;
            drawBlock();
        }else {
            //블럭이 벽 혹은 블럭에 막힘
            //블럭이 한줄 되면 지우는 함수 삽입
            fixBlock();
            ck_clearRow();
            spawnBlock();
        }
    }

    //더 내려갈 수 있는지 구현
    private boolean canMoveDown(){
        int[][] shape = currentBlock.getCurrentShape(); //현재 블럭을 가져옴
        //Drawable blockDrawable = currentDrawableBlock;

        for(int i = 0; i< shape.length; i++){
            for(int j = 0; j<shape[i].length; j++){
                if(shape[i][j] != 0){
                    int newRow = currentBlockRow + i + 1; //미리 앞의 행을 찾음
                    int newCol = currentBlockCol + j;

                    if(newRow >= ROWS || v_board[newRow][newCol] == 1 || v_board[newRow][newCol]==3){
                        return false;
                    }

                }
            }
        }
        return true;
    }

    //블럭을 고정하는 함수
    private void fixBlock(){
        int[][]shape = currentBlock.getCurrentShape();
        Drawable fixedBlockDrawable = currentDrawableBlock;

        for(int i = 0; i<shape.length; i++){
            for(int j = 0; j<shape[i].length; j++){
                if(shape[i][j] != 0){
                    cells[currentBlockRow+i][currentBlockCol+j].setBackground(fixedBlockDrawable);
                    v_board[currentBlockRow+i][currentBlockCol+j]=3;
                }
            }
        }
    }

    //왼쪽으로 더 갈 수 있는지
    private boolean canMoveLeft(){
        int[][]shape = currentBlock.getCurrentShape();

        for(int i = 0; i<shape.length; i++){
            for(int j = 0; j< shape[i].length; j++){
                if(shape[i][j] != 0){
                    int newRow = currentBlockRow + i;
                    int newCol = currentBlockCol + j - 1;

                    if(newCol < 0 || v_board[newRow][newCol]==1 || v_board[newRow][newCol]==3){
                        return false;
                    }
                }
            }
        }
        return true;
    }
    //오른쪽으로 더 갈 수 있는지
    private boolean canMoveRight(){
        int[][] shape = currentBlock.getCurrentShape();

        for(int i = 0; i<shape.length; i++){
            for(int j = 0; j<shape[i].length; j++){
                if(shape[i][j] != 0){
                    int newRow = currentBlockRow + i;
                    int newCol = currentBlockCol + j + 1;

                    if(newCol >= COLS || v_board[newRow][newCol]==1 || v_board[newRow][newCol]==3){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    //왼쪽으로 가는 함수
    private void moveBlockLeft(){
        if(canMoveLeft()){
            clearBlock();
            currentBlockCol--;
            drawBlock();
        }
    }
    //오른쪽으로 가는 함수
    private void moveBlockRight(){
        if(canMoveRight()){
            clearBlock();
            currentBlockCol++;
            drawBlock();
        }
    }

    //회전이 가능한지 확인
    private boolean canRotate(){
        int[][] nextShape = currentBlock.getNextShape();
        int nextRow = currentBlockRow;
        int nextCol = currentBlockCol;

        for(int i = 0; i< nextShape.length; i++){
            for(int j = 0; j<nextShape[i].length; j++){
                if(nextShape[i][j] != 0){
                    int newRow = nextRow + i;
                    int newCol = nextCol + j;
                    if (newRow < 0 || newRow >= 20 || newCol < 0 || newCol >= 11 || v_board[newRow][newCol] == 1 || v_board[newRow][newCol] == 3) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    //회전 함수
    private void rotateBlock(){
        if(canRotate()){
            clearBlock();
            currentBlock.rotate();
            drawBlock();
        }
    }

    //즉시 아래로 내려가는 함수
    private void moveBlockToLowestBlockRow() {
        if (canMoveDown()) {
            int currentBlockCol = this.currentBlockCol;
            int currentBlockRow = this.currentBlockRow;
        }
    }

    private void moveBlockDrop(){
        //ckBottom();
        clearBlock();//기존 위치의 블럭 삭제
        currentBlockRow = findDropPosition();//밑으로 갈 수 있는 위치 찾고 기준 행 선언
        drawBlock();
        fixBlock();//고정
        ck_clearRow();//행 다 차면 삭제
        spawnBlock();//블럭생성
    }

    //떨어질 위치를 찾는 함수
    private int findDropPosition(){
        int dropRow = currentBlockRow;//블럭과 보드의 현재 위치를 가지고옴

        while(canMoveTo(dropRow + 1, currentBlockCol)){
            dropRow++;
        }
        return dropRow;//블럭 혹은 벽돌로 되어있는 행을 반환
    }
    //실제로 행 열을 통해서 찾는 함수
    private boolean canMoveTo(int row, int col){
        int[][]shape = currentBlock.getCurrentShape();//블럭의 형태를 가지고 옴

        for(int i = 0; i<shape.length; i++){
            for(int j = 0; j<shape.length; j++){
                if(shape[i][j] != 0){
                    int newRow = row+i;
                    int newCol = col+j;

                    if(newRow>=ROWS||newCol<0||newCol>=COLS||v_board[newRow][newCol]==3 || v_board[newRow][newCol]==1){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    //한줄이 완성이 되면 행을 찾는 로직
    private void ck_clearRow(){
        System.out.println("행 검사 시작");
        boolean ckRow;
        int[] clsRow = new int[]{-1,-1,-1,-1};
        int indexRow=-1;

        for(int i = currentBlockRow; i<ROWS-1; i++){
            ckRow = true;
            for(int j = 1; j<COLS-1; j++){
                if(v_board[i][j] == 0){
                    ckRow = false;
                    break;
                }
            }
            if(ckRow == true){
                indexRow++;
                clsRow[indexRow]=i;
            }
        }
        clearRow(clsRow);//진짜 한줄 없애는 로직 함수
    }
    //실제 한줄을 없애는 로직
    private void clearRow(int[] clsRow){
        Drawable blockDrawable = ContextCompat.getDrawable(this, R.drawable.border);
        int score = 0;
        //Drawable blockRed = ContextCompat.getDrawable(this,R.drawable.border_red);
        for(int i = 0; i<clsRow.length; i++){
            if(clsRow[i]!=-1){
                score++;
                for(int j = 1; j<COLS-1; j++){
                    v_board[clsRow[i]][j] = 0;
                    cells[clsRow[i]][j].setBackground(blockDrawable);  //실제 보드판
                    for(int k = clsRow[i]; k>0; k--){
                        v_board[k][j] = v_board[k-1][j];
                        cells[k][j].setBackground(cells[k-1][j].getBackground());
                        v_board[k-1][j] = 0;
                        cells[k-1][j].setBackground(blockDrawable);
                    }
                }
            }
        }
        score_calculation(score);
    }

    //게임오버
    private void gameOverBlock(){
        //블럭이 겹치면 게임 오버
        popupView();
    }

    //게임오버 시간제한
    public void setTimer(){
        timerTV.setTextColor(Color.WHITE);
        countDownTimer = new CountDownTimer(START_TIME_IN_MILLIS, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int secondsRemaining = (int) (millisUntilFinished / 1000);
                timerTV.setText(String.format("%02d:%02d", secondsRemaining / 60, secondsRemaining % 60));
                if (secondsRemaining < 10) {
                    timerTV.setTextColor(Color.RED);
                }
                if(secondsRemaining == 0){
                    onFinish();
                    popupView();

                }
            }
            @Override
            public void onFinish() {
                timerTV.setText("종료");
            }
        }.start();
    }

    //점수 계산 함수
    private void score_calculation(int score){
        String formattedScore;
        switch(score){
            case 1:
                realScore+=100;
                formattedScore = String.format("%05d", realScore);
                scoreTV.setText(formattedScore);
                break;
            case 2:
                realScore+=300;
                formattedScore = String.format("%05d", realScore);
                scoreTV.setText(formattedScore);
                break;
            case 3:
                realScore+=500;
                formattedScore = String.format("%05d", realScore);
                scoreTV.setText(formattedScore);
                break;
            case 4:
                realScore+=800;
                formattedScore = String.format("%05d", realScore);
                scoreTV.setText(formattedScore);
                break;
            default:
                break;
        }
    }

    //게임 종료 후 팝업창
    private void popupView(){
        stopBlock();
        myDialog.setContentView(R.layout.inputranking);
        myDialog.setTitle("랭킹");
        myDialog.setCancelable(true);
        gameName = (TextView)myDialog.findViewById(R.id.inputGameName);
        inputScore = (TextView)myDialog.findViewById(R.id.inputRankingScore);
        EditText inputName = (EditText)myDialog.findViewById(R.id.inputName);

        Button rankingInput = (Button)myDialog.findViewById(R.id.inputRankingI);
        gameName.setText("테트리스");

        inputScore.setText(String.valueOf(realScore));

        rankingInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String playerName = inputName.getText().toString();
                //점수 DB전송
                int result = skThread.sendDataToServer(playerName, realScore, gameName.getText().toString());
                //result=1 입력 성공, 2 닉네임 중복
                if(result == 1){
                    Log.d("result", "입력 성공");
                    finish();
                }else{
                    Log.d("result", "입력 실패");
                    finish();
                }
            }
        });

        myDialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                inputName.requestFocus();
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    inputMethodManager.showSoftInput(inputName, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        }, 300);
    }

    //테트리스 블럭 움직임 멈추기 함수
    private void stopBlock(){
        if(handler != null && runnable != null){
            handler.removeCallbacks(runnable);
            handler = null;
            runnable = null;
        }
    }

    //뒤로가기 종료 시
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopBlock();
    }
}