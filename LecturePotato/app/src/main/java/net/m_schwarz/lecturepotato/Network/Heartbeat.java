package net.m_schwarz.lecturepotato.Network;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by michael on 15.05.16.
 */
public class Heartbeat {
    static String baseUrl = "http://dragon.mbscware.com:5000";

    public static void push(int userid,int lecture_time,int slack_time) throws Exception{
        URL url = new URL(baseUrl + "/heartbeat/"+ userid);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type","application/json");

        String str =  "{ \"lecture_time\" : " + lecture_time +", \"slack_time\" : "+ slack_time +"}";
        byte[] outputInBytes = str.getBytes("UTF-8");
        OutputStream os = conn.getOutputStream();
        os.write( outputInBytes );
        os.close();
        conn.getInputStream();
    }
}
