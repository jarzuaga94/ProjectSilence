package cs.uml.edu.projectsilence;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

/**
 * Created by Jeremy on 4/13/2015.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean muteSound = intent.getBooleanExtra(EventItem.MUTE_SOUND, false);
        boolean sendMessage = intent.getBooleanExtra(EventItem.SEND_TEXT, false);
        boolean isStartAlarm = intent.getBooleanExtra(MainScreen.isStartAlarm, false);
        Toast.makeText(context, "Alarm receiver", Toast.LENGTH_LONG).show();
        if(isStartAlarm) {
            if (muteSound) {
                AudioManager amanager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
                amanager.setStreamMute(AudioManager.STREAM_ALARM, true);
                amanager.setStreamMute(AudioManager.STREAM_MUSIC, true);
                amanager.setStreamMute(AudioManager.STREAM_RING, true);
                amanager.setStreamMute(AudioManager.STREAM_SYSTEM, true);
            }
            if (sendMessage) {


            }
        }
        else{
            AudioManager amanager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
            amanager.setStreamMute(AudioManager.STREAM_ALARM, false);
            amanager.setStreamMute(AudioManager.STREAM_MUSIC, false);
            amanager.setStreamMute(AudioManager.STREAM_RING, false);
            amanager.setStreamMute(AudioManager.STREAM_SYSTEM, false);
        }
    }
}

