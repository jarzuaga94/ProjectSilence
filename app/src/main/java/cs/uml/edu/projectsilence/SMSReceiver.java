package cs.uml.edu.projectsilence;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

/**
 * Created by Jeremy on 4/22/2015.
 */
public class SMSReceiver extends BroadcastReceiver {
    final SmsManager sms = SmsManager.getDefault();
    @Override
    public void onReceive(Context context, Intent intent){
        final Bundle bundle = intent.getExtras();
        //String eventName = intent.getStringExtra(EventItem.TITLE);
        //String endTime = intent.getStringExtra(EventItem.END_TIME);

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    //String message = "I'm currently busy at " + MainScreen.t + ". I'll be available again at " + endTime + ".";
                    String message = "I'm currently busy";
                    // Show Alert
                    int duration = Toast.LENGTH_LONG;
                    boolean isFriend = false;
                    //Toast toast = Toast.makeText(context,"senderNum: " + phoneNumber + ", message: " + message, duration);
                    //toast.show();

                    for(int j = 0; j < MainScreen.friends.size(); j++){
                        //Toast.makeText(context, MainScreen.friends.get(j), Toast.LENGTH_SHORT).show();
                        if( !phoneNumber.equals(MainScreen.friends.get(j)) ){

                            isFriend = false;
                        }
                        else if( phoneNumber.equals(MainScreen.friends.get(j))){
                            Toast.makeText(context, "Its a friend", Toast.LENGTH_SHORT).show();
                            long pattern[] = { 400, 2000, 400, 2000, 400, 2000, 400, 2000, 400};

                            Vibrator vibrator = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
                            vibrator.vibrate(pattern, -1 );
                            isFriend = true;
                            break;
                        }
                    }
                    if( isFriend == false){
                        Toast.makeText(context, "Not a Friend", Toast.LENGTH_SHORT).show();
                        sms.sendTextMessage(phoneNumber, null, message, null, null);
                    }


                } // end for loop
            } // bundle is null

        } catch (Exception e) {

        }
    }
}
