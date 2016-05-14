package net.m_schwarz.lecturepotato.Network;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by michael on 14.05.16.
 */
public class Users {
    static String baseUrl = "http://localhost:8080/stuff";

    public static boolean existsUserForDevice(String deviceId) { // throws Exception{
        // URL url = new URL(baseUrl + "/device/"+ deviceId);
        // HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // conn.setRequestMethod("GET");
        // conn.connect();
        // return (conn.getResponseCode() == 200);
        return false;
    }

    public static boolean existsUser(String username) throws  Exception{
       // URL url = new URL(baseUrl + "/user/name/"+ username);
       // HttpURLConnection conn = (HttpURLConnection) url.openConnection();
       // conn.setRequestMethod("GET");
       // conn.connect();
       // return (conn.getResponseCode() == 200);

        return Math.random() < 0.5;

    }

    public static void createUser(String deviceId,String username,int uni){

    }
}
