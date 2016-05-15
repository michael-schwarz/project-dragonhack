package net.m_schwarz.lecturepotato.Network;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by michael on 14.05.16.
 */
public class Users {
    static String baseUrl = "http://dragon.mbscware.com:5000";

    public static boolean existsUserForDevice(String deviceId) throws Exception{
        URL url = new URL(baseUrl + "/device/"+ deviceId);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        return (conn.getResponseCode() == 200);
    }

    public static UserDetails getUserDetails(String deviceId) throws Exception{
        URL url = new URL(baseUrl + "/device/"+ deviceId);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");


        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line+"\n");
        }
        br.close();
        String message = sb.toString();

        Gson gson = new Gson();
        UserDetails ud = gson.fromJson(message, UserDetails.class);

        return ud;
    }

    public class UserDetails{
        public int user_id;
        public String username;
        public int university_id;
    }


    public static boolean existsUser(String username) throws Exception {
       URL url = new URL(baseUrl + "/user/name/"+ username);
       HttpURLConnection conn = (HttpURLConnection) url.openConnection();
       conn.setRequestMethod("GET");
       conn.connect();
       return (conn.getResponseCode() == 200);
    }

    public static void createUser(String deviceId,String username,int uni) throws Exception{
        URL url = new URL(baseUrl + "/user/name/"+ username);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type","application/json");

        String str =  "{ \"device_id\" : \"" + deviceId +"\", \"university_id\" : "+uni+"}";
        byte[] outputInBytes = str.getBytes("UTF-8");
        OutputStream os = conn.getOutputStream();
        os.write( outputInBytes );
        os.close();
        conn.getInputStream();

    }
}
