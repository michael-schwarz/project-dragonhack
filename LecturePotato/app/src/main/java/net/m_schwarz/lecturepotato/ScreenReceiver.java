package net.m_schwarz.lecturepotato;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

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

    public void over(){
        Log.v("TIME","Is up");
        if(on) { active += (System.currentTimeMillis() - lastOn); }
        Log.v("RESULT","Active " + active + " / " + total);
    }

    private boolean inTime(){
        long current = System.currentTimeMillis();
        return (current < end);
    }
}
