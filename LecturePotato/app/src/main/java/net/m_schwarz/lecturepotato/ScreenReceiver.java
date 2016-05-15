package net.m_schwarz.lecturepotato;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

import net.m_schwarz.lecturepotato.Network.Heartbeat;

/**
 * Created by michael on 14.05.16.
 */
public class ScreenReceiver extends BroadcastReceiver{
    private long end;
    private long lastOn;
    private boolean on = true;
    private long total;
    private long active;

    public void setEnd(long end){
        on = true;
        this.end = end;
        this.total = end-System.currentTimeMillis();
        this.lastOn = System.currentTimeMillis();
        this.active = 0;
    }

    @Override
    public void onReceive(Context arg0, Intent arg1){
        if (arg1.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            if(inTime()){
                Log.v("SCREEN","On at " + System.currentTimeMillis());
                lastOn = System.currentTimeMillis();
            }
            else {
                Log.v("SCREEN","(Not recorded) On at " + System.currentTimeMillis());
            }
            on = true;
        }
        else if(arg1.getAction().equals(Intent.ACTION_SCREEN_OFF)){
            if(inTime()){
                Log.v("SCREEN","Off at " + System.currentTimeMillis());

                active += (System.currentTimeMillis() - lastOn);
            }
            else {
                Log.v("SCREEN","(Not recorded) Off at " + System.currentTimeMillis());
            }

            on = false;
        }
    }

    public double over(int userid){
        Log.v("TIME","Is up");
        if(on) { active += (System.currentTimeMillis() - lastOn); }

        AsyncTask<ThreeInts,Void,Void> task = new AsyncTask<ThreeInts, Void, Void>() {
            @Override
            protected Void doInBackground(ThreeInts... params) {
                try {
                    Heartbeat.push(params[0].first,params[0].second,params[0].third);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void arg){

            }
        };



        task.execute(new ThreeInts(userid,(int)(total/1000),(int)(active/1000)));
        Log.v("RESULT","Active " + active + " / " + total);

        return active/(total*1.0);
    }



    public class ThreeInts {
        int first,second,third;

        public ThreeInts(int first,int second,int third){
            this.first = first;
            this.second = second;
            this.third = third;
        }
    }

    private boolean inTime(){
        long current = System.currentTimeMillis();
        return (current < end);
    }
}
