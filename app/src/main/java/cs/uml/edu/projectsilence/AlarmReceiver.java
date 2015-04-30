package cs.uml.edu.projectsilence;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.widget.Toast;

/**
 * Created by Jeremy on 4/13/2015.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){
        boolean muteSound = intent.getBooleanExtra(EventItem.MUTE_SOUND, false);
        boolean sendMessage = intent.getBooleanExtra(EventItem.SEND_TEXT, false);
        boolean isStartAlarm = intent.getBooleanExtra(MainScreen.isStartAlarm, false);

        //Will get used when we send texts based on location
        //String eventName = intent.getStringExtra(EventItem.TITLE);
        //String endTime = intent.getStringExtra( EventItem.END_TIME );


        //Toast.makeText(context, "Alarm receiver", Toast.LENGTH_LONG).show();
        if(isStartAlarm){
            if (muteSound) {
                //Toast.makeText(context, "Mute Sound Triggered", Toast.LENGTH_LONG).show();
                AudioManager amanager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
                amanager.setStreamMute(AudioManager.STREAM_ALARM, true);
                amanager.setStreamMute(AudioManager.STREAM_MUSIC, true);
                amanager.setStreamMute(AudioManager.STREAM_RING, true);
                amanager.setStreamMute(AudioManager.STREAM_SYSTEM, true);
                amanager.setStreamMute(AudioManager.STREAM_VOICE_CALL, true);
                amanager.setStreamMute(AudioManager.STREAM_DTMF, true);
                amanager.setRingerMode( 0 );
            }
            if(sendMessage) {
                /*String phoneNumber = "16034758425";
                String message = "Test";
                sendSMS(phoneNumber, message);
                Vibrator v;
                v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(2000);
                Toast.makeText(context, "Alarm Triggered and SMS Sent", Toast.LENGTH_LONG).show();*/
                context.getApplicationContext().registerReceiver(MainScreen.smsReceiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
                context.getApplicationContext().registerReceiver(MainScreen.callReceiver, new IntentFilter("android.intent.action.PHONE_STATE"));
            }
        }
        else {
            AudioManager amanager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
            amanager.setStreamMute(AudioManager.STREAM_ALARM, false);
            amanager.setStreamMute(AudioManager.STREAM_MUSIC, false);
            amanager.setStreamMute(AudioManager.STREAM_RING, false);
            amanager.setStreamMute(AudioManager.STREAM_SYSTEM, false);
            amanager.setStreamMute(AudioManager.STREAM_VOICE_CALL, false);
            amanager.setStreamMute(AudioManager.STREAM_DTMF, false);
            //Toast.makeText(context, "EndTime alarm", Toast.LENGTH_LONG).show();
            context.getApplicationContext().unregisterReceiver(MainScreen.smsReceiver);
            context.getApplicationContext().unregisterReceiver(MainScreen.callReceiver);
        }
      }
}

