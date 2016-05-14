package net.m_schwarz.lecturepotato;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by michael on 14.05.16.
 */
public class ScreenReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context arg0, Intent arg1){
        if (arg1.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            Log.d("SCREEN","On at " + System.currentTimeMillis());
        }
        else if(arg1.getAction().equals(Intent.ACTION_SCREEN_OFF)){
            Log.d("SCREEN","Off at " + System.currentTimeMillis());
        }
    }
}
