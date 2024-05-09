package com.inhatc.minigame_application;

import android.util.Log;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpDB {
        String serverUrl = "http://119.197.155.172:50050";
        public HttpDB(String type){
            try{
                URL url = new URL(serverUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                if(type == "GET"){
                    connection.setRequestMethod("GET");
                    getMethod(connection);
                }else if(type == "POST"){
                    connection.setRequestMethod("POST");
                }else if(type == "PUT"){
                    connection.setRequestMethod("PUT");
                }else if(type == "DELETE"){
                    connection.setRequestMethod("PUT");
                }else{
                    System.out.println("요청 메소드 방식이 잘못됨");
                }

            }catch(IOException e){
                e.printStackTrace();
            }
        }

        public void getMethod(HttpURLConnection connection) throws IOException {
            int responseCode = connection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while((line = reader.readLine()) != null){
                    response.append(line);
                }
                reader.close();
                System.out.println("응답 값 : "+response.toString());
            }else{
                System.out.println("서버 접속 실패. 실패 코드 : "+responseCode);
            }

        }
}
