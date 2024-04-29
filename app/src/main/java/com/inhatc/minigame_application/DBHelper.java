package com.inhatc.minigame_application;
import android.util.Log;

import java.util.*;
import java.io.*;
import java.net.*;

public class DBHelper {
    private static final String IP_server = "119.197.155.172";
    private static final int PORT = 50050;
    private static String response = null;
    public int DBcon(String name, int score, String gamename){
        Thread socketThread = new Thread(() -> {
            try{
                Log.d("스레드 소켓 실행","소켓 실행");
                Socket socket = new Socket(IP_server, PORT);

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                // 서버 응답 제출
                out.println(name + "," + score + "," + gamename);

                // Receive and process response from the server
                this.response = in.readLine();

                socket.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        });
        socketThread.start();

        if (Integer.parseInt(this.response) == 1) {
            return 1;       //소켓 통신 성공
        }
        return 0;//소켓통신 실패
    }

    private int ckResponse(String response){
        if (Integer.parseInt(response) == 1) {
            return 1;//소켓 성공 1
        }
        return 0; //소켓 실패 0
    }
}


    /*
    public int DBconMain(String name, int score, String gamename) {
        String serverAddress = "119.197.155.172";
        int serverPort = 50050;
        Log.d("함수 실행", "함수실행");
        try {
            //서버 연결
            Socket socket = new Socket(serverAddress, serverPort);
            Log.d("DB 확인", "서버에 연결되었습니다. ");

            //서버와 통신 입출력 스트림
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            //서버에 데이터 전송
            out.println(name + "," + score + "," + gamename);

            //서버로부터 받은 응답
            String response = in.readLine();
            System.out.println("서버응답:" + response);

            if (Integer.parseInt(response) == 1) {
                socket.close();
                return 1;
            }
            //연결 종료
            socket.close();
        } catch (IOException e) {
            Log.d("DB 실패", "서버에 연결실패. ");
            e.printStackTrace();
        }
        return 0;
    }
     */

