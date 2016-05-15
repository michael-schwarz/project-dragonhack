package net.m_schwarz.lecturepotato.Network;

import java.io.DataInputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by michael on 14.05.16.
 */
public class Users {
    static String baseUrl = "http://193.2.179.219:5000";

    public static boolean existsUserForDevice(String deviceId) throws Exception{
        URL url = new URL(baseUrl + "/device/"+ deviceId);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        getUserDetails(deviceId);
        return (conn.getResponseCode() == 200);
    }

    public static UserDetails getUserDetails(String deviceId) throws Exception{
        URL url = new URL(baseUrl + "/device/"+ deviceId);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        String message = new DataInputStream(conn.getInputStream()).readUTF();

        return null;
    }

    public class UserDetails{
        int user_id;
        String username;
        int university_id;
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
