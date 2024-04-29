package com.inhatc.minigame_application;
import android.util.Log;

import java.util.*;
import java.net.*;
import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SocketThread extends Thread{
    private static SocketThread instance;
    private SocketThread(){
        //Main을 제외한 외부 클래스에서 인스턴스화를 방지하는 생성자
    }
    public static synchronized SocketThread getInstance(){
        if(instance == null){
            instance = new SocketThread();
        }
        return instance;
    }

    private static final String IP_server = "119.197.155.172";
    private static final int PORT_server = 50050;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private boolean running;
    private static int ckMethod=0;
    private static AtomicInteger ThAccess=new AtomicInteger(0);
    //private static int ThAccess=0;
    private static String outData = null;
    private volatile static boolean trigger = false;



    @Override
    public void run() {
        try {
            socket = new Socket(IP_server, PORT_server);
            System.out.println("서버 접속 시작");
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter((socket.getOutputStream()),true);

            while(true) {
                System.out.println("스레드 실행 ckMethod:"+ckMethod);
                if(ckMethod==0) {
                    System.out.println("접근");
                    while(!trigger) {
                        try {
                            Thread.sleep(1000);
                            System.out.println("스레드 슬립중..");
                        }catch(InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                //통신 종료 요청 시
                if(outData.charAt(0)=='-') {
                    out.println(outData);
                    in.close();
                    out.close();
                    socket.close();
                    break;
                }

                //데이터 전송,응답
                out.println(outData);
                System.out.println("스레드 슬립 탈출");
                String inData = in.readLine();

                if(trigger) {
                    synchronized (this) {
                        ThAccess.set(1);
                        notify();
                    }
                }
                System.out.println("ThAccess값 : "+ThAccess);
                trigger=false;
                ckMethod=0;
            }
            //trigger=false;
        }catch(IOException e) {
            System.out.println("스레드 전체 에러");
            e.printStackTrace();
        }
    }


    /*
    public int SocketLogic(){
        try{
            //소켓 접속
            socket = new Socket(IP_server, PORT_server);
            Log.d("소켓접속 성공","소켓접속 성공");
            //소켓 요청 out, 응답 in 생성
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter((socket.getOutputStream()),true);
            return 1;//소켓접속 성공
        }catch(IOException e){
            e.printStackTrace();
        }
        return 0;//소켓접속 실패
    }
    */
    public synchronized String sendDataToServer(String data){
        out.println(data);
        try {
            String inData = in.readLine();
            return inData;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public synchronized int sendDataToServer(String name, int score, String gamename){
        try{
            System.out.println("메서드 진입 성공");
            outData = "2"+","+name + "," + score + "," + gamename;
            ckMethod=1;
            trigger=true;//스레드 동작 시작
            System.out.println("ThAccess="+ThAccess+"   ckMethod="+ckMethod);
            while(ThAccess.get() != 1){//아직 스레드가 끝나지 않음
                //System.out.println("응 아니야");
                wait();
            }
            System.out.println("메소드 끝");
            ThAccess.set(0);
            ckMethod=0;
        }catch(InterruptedException e) {
            e.printStackTrace();
        }
        return 1;
    }


    public void stopThread(){
        running = false;
    }

    public void closeSocket(){
        try{
            if(socket != null && !socket.isClosed()){
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
