package cs.uml.edu.projectsilence;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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


            // If phone is Idle
            if (state.equals(TelephonyManager.EXTRA_STATE_IDLE))
            {
                // If phone was ringing(ring=true) and not received(callReceived=false) , then it is a missed call
                if(ring==true&&callReceived==false)
                {
                    Toast.makeText(context, "It was A MISSED CALL from : " + callerPhoneNumber, Toast.LENGTH_LONG).show();
                    sms.sendTextMessage(callerPhoneNumber, null, "I'm current busy", null, null);
                }
            }
    }

}
