package net.m_schwarz.lecturepotato;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

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
