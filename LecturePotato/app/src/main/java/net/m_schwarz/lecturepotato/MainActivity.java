package net.m_schwarz.lecturepotato;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import net.m_schwarz.lecturepotato.Network.Users;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        if(value){
            Intent intent = new Intent(this,ChooseUni.class);
            startActivity(intent);
        }
    }


    public String getDeviceId(){
        SharedPreferences settings = getSharedPreferences("LecturePotato", 0);
        if(!settings.getBoolean("started",false)){
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("started",true);

            UUID id = UUID.randomUUID();
            editor.putString("deviceId",id.toString());
            editor.commit();
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
                        mReceiver.over();
                    }
                });
            }
        };

        timer = new Timer();
        timer.schedule(timerTask,end.getTime()-System.currentTimeMillis());
    }


}
