package cs.uml.edu.projectsilence;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

/**
 * Created by Jeremy on 4/22/2015.
 */
public class CallReceiver extends BroadcastReceiver {
    static boolean ring=false;
    static boolean callReceived=false;
    String callerPhoneNumber;
    SmsManager sms = SmsManager.getDefault();
    @Override
    public void onReceive(Context context, Intent intent){


            // Get the current Phone State
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            String title = intent.getStringExtra(EventItem.TITLE);

            if(state==null)
                return;

            // If phone state "Rininging"
            if(state.equals(TelephonyManager.EXTRA_STATE_RINGING))
            {
                ring =true;
                // Get the Caller's Phone Number
                Bundle bundle = intent.getExtras();
                callerPhoneNumber= bundle.getString("incoming_number");

            }



            // If incoming call is received
            if(state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))
            {
                callReceived=true;
            }

            boolean isFriend = false;
            // If phone is Idle
            if (state.equals(TelephonyManager.EXTRA_STATE_IDLE))
            {
                // If phone was ringing(ring=true) and not received(callReceived=false) , then it is a missed call
                if(ring==true&&callReceived==false)
                {
                    Toast.makeText(context, "It was A MISSED CALL from : " + callerPhoneNumber, Toast.LENGTH_LONG).show();
                    for(int j = 0; j < MainScreen.friends.size(); j++){
                        if(!callerPhoneNumber.equals(MainScreen.friends.get(j)) ){
                            isFriend = false;
                        }
                        else if( callerPhoneNumber.equals(MainScreen.friends.get(j))){
                            Toast.makeText(context, "Its a friend", Toast.LENGTH_SHORT);
                            long pattern[] = { 400, 2000, 400, 2000, 400, 2000, 400, 2000, 400};

                            Vibrator vibrator = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
                            vibrator.vibrate(pattern, -1 );
                            isFriend = true;
                            break;
                        }
                    }
                    if( isFriend == false ){
                        Toast.makeText(context, "Its a friend", Toast.LENGTH_SHORT);
                        sms.sendTextMessage(callerPhoneNumber, null, "I'm current busy at " + title , null, null);
                    }

                }
            }
    }

}
