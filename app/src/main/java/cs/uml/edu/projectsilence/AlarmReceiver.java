package cs.uml.edu.projectsilence;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Vibrator;
import android.telephony.SmsManager;
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
        Toast.makeText(context, "Alarm receiver", Toast.LENGTH_LONG).show();
        if(isStartAlarm){
            if (muteSound) {
                AudioManager amanager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
                amanager.setStreamMute(AudioManager.STREAM_ALARM, true);
                amanager.setStreamMute(AudioManager.STREAM_MUSIC, true);
                amanager.setStreamMute(AudioManager.STREAM_RING, true);
                amanager.setStreamMute(AudioManager.STREAM_SYSTEM, true);
            }
            if(sendMessage){
                String phoneNumber = "16034758425";
                String message = "Test";
                sendSMS( phoneNumber, message);
                Vibrator v;
                v=(Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(2000);
                Toast.makeText(context, "Alarm Triggered and SMS Sent", Toast.LENGTH_LONG).show();
            }
        }
        else {
            AudioManager amanager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
            amanager.setStreamMute(AudioManager.STREAM_ALARM, false);
            amanager.setStreamMute(AudioManager.STREAM_MUSIC, false);
            amanager.setStreamMute(AudioManager.STREAM_RING, false);
            amanager.setStreamMute(AudioManager.STREAM_SYSTEM, false);

            Toast.makeText(context, "EndTime alarm", Toast.LENGTH_LONG).show();
        }
      }

    private void sendSMS(String phoneNumber, String message)
    {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }


}

