package com.inhatc.minigame_application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class SocketThread extends Thread{
    //Main을 제외한 외부 클래스에서 인스턴스화를 방지하는 생성자 싱글톤 패턴
    private static SocketThread instance;
    private SocketThread(){
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
                            Thread.sleep(30000);
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

    public synchronized int sendDataToServer(String name, int score, String gamename){
        try{
            System.out.println("점수 삽입 메서드 진입 성공");
            outData = "2"+","+name + "," + score + "," + gamename;
            ckMethod=1;
            trigger=true;//스레드 동작 시작
            System.out.println("ThAccess="+ThAccess+"   ckMethod="+ckMethod);
            while(ThAccess.get() != 1){//아직 스레드가 끝나지 않음
                //System.out.println("소켓스레드 진행 중 메소드 wait");
                wait();
            }
            System.out.println("점수 삽입 메소드 끝");
            ThAccess.set(0);
            ckMethod=0;
        }catch(InterruptedException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public synchronized int SelectRankList(){
        try{
            System.out.println("랭킹 가져오기 메서드 진입 성공");
            outData = "3"+","+"Select Ranking";//3일경우 SQL SELECT문 실행
            ckMethod=1;
            trigger=true;//스레드 동작 시작
            System.out.println("ThAccess="+ThAccess+"  ckMethod="+ckMethod);
            while(ThAccess.get() != 1){
                //System.out.println("소켓스레드 진행 중 메소드 wait");
                wait();
            }
            System.out.println("랭킹 가져오기 메소드 끝");
            ThAccess.set(0);
            ckMethod=0;
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        return 1;
    }

}
