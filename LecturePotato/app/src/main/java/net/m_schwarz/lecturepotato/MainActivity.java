package net.m_schwarz.lecturepotato;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import net.m_schwarz.lecturepotato.Network.Users;

import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;


import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    ScreenReceiver mReceiver;
    TimerTask timerTask;
    Timer timer;
    final Handler handler = new Handler();

    static boolean FORCE = false;
    Users.UserDetails uDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Register intents for getting screen on/off events
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, filter);

        notifySlackness(0.56996);

        TimePicker tp = (TimePicker) findViewById(R.id.lectureDurationPicker);
        tp.setIs24HourView(true);

        AsyncTask<String,Void,Boolean> deviceKnownTask = new AsyncTask<String, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(String... params) {
                try {
                    return Users.existsUserForDevice(params[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean value) {
                userExistsForDevice(value);
            }
        };
        deviceKnownTask.execute(getDeviceId());

        Log.v("DEVICE_ID",getDeviceId());
    }

    public void userExistsForDevice(Boolean value){
        if(!value){
            Intent intent = new Intent(this,ChooseUni.class);
            startActivity(intent);
            return;
        }


        AsyncTask<String,Void,Users.UserDetails> getUserDetailsTask = new AsyncTask<String, Void, Users.UserDetails>() {
            @Override
            protected Users.UserDetails doInBackground(String... params) {
                try {
                    return Users.getUserDetails(params[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Users.UserDetails details){
                gotUserDetails(details);
            }
        };

        getUserDetailsTask.execute(getDeviceId());
    }


    public void gotUserDetails(Users.UserDetails details) {
        this.uDetails = details;
    }

    public String getDeviceId(){
        SharedPreferences settings = getSharedPreferences("LecturePotato", 0);
        if(!settings.getBoolean("started",false) || FORCE){
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("started",true);

            UUID id = UUID.randomUUID();
            editor.putString("deviceId",id.toString());
            editor.commit();

            FORCE = false;
        }

        return settings.getString("deviceId","");
    }

    public void startButtonClicked(View v){
        TimePicker tp = (TimePicker) findViewById(R.id.lectureDurationPicker);
        Date now = new Date();
        Date end = new Date(now.getYear(),now.getMonth(),now.getDate(),tp.getCurrentHour(),tp.getCurrentMinute());
        mReceiver.setEnd(end.getTime());


        timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        toggleVis(true);
                        mReceiver.over(uDetails.user_id);
                    }
                });
            }
        };

        if(end.getTime()-System.currentTimeMillis() < 1){
            Toast.makeText(MainActivity.this, "Lectures can not be in the past", Toast.LENGTH_SHORT).show();
            return;
        }

        timer = new Timer();
        timer.schedule(timerTask,end.getTime()-System.currentTimeMillis());


        toggleVis(false);
    }

    protected void openLeaderboard(View v){
        Intent intent = new Intent(this,LeaderboardActivity.class);
        startActivity(intent);
    }

    protected void toggleVis(boolean value){
        findViewById(R.id.startButton).setAlpha(value?1.0f:.1f);
        findViewById(R.id.startButton).setClickable(value);
        findViewById(R.id.lectureDurationPicker).setAlpha(value?1.0f:.1f);
        findViewById(R.id.lectureDurationPicker).setEnabled(value);
    }

    protected void notifySlackness(double percent) {
        Notification notification  = new Notification.Builder(this).setCategory(Notification.CATEGORY_MESSAGE)
                .setContentTitle("LecturePotato")
                .setContentText("During this lecture, you spent " +
                        (int) Math.ceil(percent* 100) + "% slacking.")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.logo1)
                .setVisibility(Notification.VISIBILITY_PUBLIC).build();
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(457, notification );

    }
}
