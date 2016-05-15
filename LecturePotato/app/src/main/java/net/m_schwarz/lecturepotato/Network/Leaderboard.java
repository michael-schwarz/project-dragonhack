package net.m_schwarz.lecturepotato.Network;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by michael on 15.05.16.
 */
public class Leaderboard {
    static String baseUrl = "http://dragon.mbscware.com:5000";

    public static LeaderboardEntries getEntries(String userId,String range) throws Exception{
        URL url = new URL(baseUrl + "/leaderboard/user/"+userId+"/"+ range);
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
        LeaderboardEntries le = gson.fromJson(message, LeaderboardEntries.class);

        return le;
    }


    public class LeaderboardEntries{
        public Entry[] data;
    }

    public class Entry {
        public String nickname;
        public double stat;
    }
}
