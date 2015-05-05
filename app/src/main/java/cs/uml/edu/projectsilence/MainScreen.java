package cs.uml.edu.projectsilence;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.app.ListActivity;
import android.database.Cursor;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;


public class MainScreen extends ListActivity {
    private static final int ADD_EVENT_REQUEST = 0;
    private static final int EDIT_REQUEST_CODE = 1;
    private static final int ADD_FRIEND_CODE = 2;
    private static String Title;
    private static String StartDate;
    private static String StartTime;
    private static String EndDate;
    private static String EndTime;
    private static boolean MuteSound;
    private static boolean SendText;
    public String friendFile = "friendFile";
    public static ArrayList<String> friends = new ArrayList<String>();
    public static String isStartAlarm = "isStartAlarm";
    public static long id;
    public static String POSITION = "pos";
    public static SMSReceiver smsReceiver = new SMSReceiver();
    public  static CallReceiver callReceiver = new CallReceiver();
    public static EventAdapter mAdapter;
    public  static DBAdapter database;


    AlarmManager alarm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = new DBAdapter(getApplication());
        database.open();
        mAdapter = new EventAdapter(getApplicationContext());
        Cursor cursor = database.getAllRows();
        cursor.moveToFirst();
        Intent data = new Intent();
        data.putExtra(POSITION, -1);
        EventItem eventItem;

        try {
            readFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Read in data from the database
        while(!cursor.isAfterLast()) {
            Title = cursor.getString(DBAdapter.COL_NAME);
            StartDate = cursor.getString(DBAdapter.COL_STARTDATE);
            StartTime = cursor.getString(DBAdapter.COL_STARTTIME);
            EndDate = cursor.getString(DBAdapter.COL_ENDDATE);
            EndTime = cursor.getString(DBAdapter.COL_ENDTIME);
            MuteSound = cursor.getInt(DBAdapter.COL_MUTESOUND) > 0;
            SendText = cursor.getInt(DBAdapter.COL_SENDMESSAGE) > 0;
            id = cursor.getLong(cursor.getColumnIndex("_id"));
            EventItem.packageIntent(data, Title, id,StartDate, EndDate, StartTime, EndTime, MuteSound, SendText);
            eventItem = new EventItem(data);
            mAdapter.add(eventItem);
            cursor.moveToNext();
        }
        cursor.close();
        getListView().setAdapter(mAdapter);
        //On click listener for each item in the list
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editEvent(position);
            }
        });
        //Long click listener for each item in the list
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        MainScreen.this);

                // set title
                alertDialogBuilder.setTitle("Delete Event");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Are you sure you want to delete this event?")
                        .setCancelable(true)
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                deleteItem(position);
                                mAdapter.remove(position);
                            }
                        })
                                // if this button is clicked, just close
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
                return true;
            }
        });
    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        database.close();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK)
        {
            EventItem eventItem = new EventItem(data);
            StartDate = EventItem.FORMAT.format(eventItem.getStarDate());
            StartTime = EventItem.timeFORMAT.format(eventItem.getStartTime());
            EndDate = EventItem.FORMAT.format(eventItem.getEndDate());
            EndTime = EventItem.timeFORMAT.format(eventItem.getEndTime());
            if (requestCode == ADD_EVENT_REQUEST) {
                id = database.insertRow(eventItem.getTitle(), StartTime,
                        StartDate, EndTime, EndDate, eventItem.getMuteSounds(), eventItem.getSendText());
                eventItem.setID(id);
                data.putExtra(EventItem.ID, id);
                mAdapter.add(eventItem);
                scheduleAlarm(getListView(), data);
            }
            if(requestCode == EDIT_REQUEST_CODE){
                database.updateRow(eventItem.getID(),eventItem.getTitle(),StartTime,
                        StartDate, EndTime, EndDate, eventItem.getMuteSounds(), eventItem.getSendText());
               mAdapter.updateEvent(eventItem, data.getIntExtra(POSITION, -1));
               scheduleAlarm(getListView(), data);

            }
        }
        if( resultCode == ADD_FRIEND_CODE ){
            friends = data.getStringArrayListExtra( "friends" );
            /*for( int i = 0; i < friends.size(); i++ ){
                Toast.makeText(this, friends.get(i) , Toast.LENGTH_SHORT).show();
            }*/

            saveFriends();
        }


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_screen, menu);
        return true;

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_add:
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void saveFriends( ){
        try {

            OutputStreamWriter out = new OutputStreamWriter(openFileOutput(friendFile, 0));
            BufferedWriter bwriter = new BufferedWriter(out);
            for( int i = 0; i < friends.size(); i++){
                bwriter.write(friends.get(i));
                bwriter.newLine();
            }
            bwriter.close();
            //Toast.makeText(this,"Here" , Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void readFiles() throws IOException {
        String str="";
        int i = 0;
        StringBuffer buf = new StringBuffer();

        InputStream is = openFileInput(friendFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        if (is!=null) {
            while ((str = reader.readLine()) != null) {
                buf.append(str + "\n" );
                friends.add(i, str);
                i++;
            }
        }
        is.close();


    }
    //
    //-Called when ADD Event is clicked from actionbar/settings tab.
    //-Creates an intent and calls it to switch to addEvent activity.
    //
    public void addEventSelected( MenuItem item){

        Intent intent = new Intent(getBaseContext(), AddEventActivity.class);
        intent.putExtra(POSITION, -1);
        startActivityForResult(intent, ADD_EVENT_REQUEST);

    }

    //
    //-Called when ADD Event is clicked from actionbar/settings tab.
    //-Creates an intent and calls it to switch to addEvent activity.
    //
    public void addFriends( MenuItem item){

        Intent intent = new Intent(getBaseContext(), FriendsList.class);

        startActivityForResult(intent, ADD_FRIEND_CODE);

    }

    //
    //-Called when Remove All Events is clicked from actionbar/settings tab
    //-Clears all events from screen also deletes events from database.
    //
    public void removeEvents( MenuItem item){
        if(mAdapter.getCount() > 0) {
            for (int i = 0; i < mAdapter.getCount(); i++)
                deleteItem(i);
            mAdapter.clear();
            database.deleteAll();
        }
    }
    public void scheduleAlarm(View V, Intent data)
    {
        //Creates a pending intent for the start an end alarm clock
        Intent startIntent = new Intent(MainScreen.this, AlarmReceiver.class);
        Intent endIntent = new Intent(MainScreen.this, AlarmReceiver.class);
        startIntent.replaceExtras(data);
        endIntent.replaceExtras(data);
        startIntent.putExtra(isStartAlarm, true);
        id = data.getLongExtra(EventItem.ID, id);
        if(data.getBooleanExtra(EventItem.SEND_TEXT, false))
        {

        }
        PendingIntent startPIntent = PendingIntent.getBroadcast(MainScreen.this, (int)id, startIntent, 0);
        endIntent.putExtra(isStartAlarm, false);
        PendingIntent endPIntent = PendingIntent.getBroadcast(MainScreen.this, (int)id*10 , endIntent, 0);
        alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        //set the alarm for particular time
        alarm.set(alarm.RTC,getMilli(StartDate, StartTime),startPIntent);
        alarm.set(alarm.RTC, getMilli(EndDate, EndTime),endPIntent);
        Toast.makeText(this, "Alarm created", Toast.LENGTH_LONG).show();

    }
    private long getMilli(String dateString, String timeString) {
        //Parses the string to get the time in milliseconds which is used for the alarm manager
        String year = "";
        String month = "";
        String day = "";
        String hour = "";
        String minutes = "";
        int i = 0;
        while (dateString.charAt(i) != '-') {
            year += dateString.charAt(i);
            i++;
        }
        i++;
        while (dateString.charAt(i) != '-') {
            if (i == 5 && dateString.charAt(i) == '0') {

            } else
                month += dateString.charAt(i);
            i++;
        }
        i++;
        while (dateString.length() != i) {
            if (i == 8 && dateString.charAt(i) == '0') {

            } else
                day += dateString.charAt(i);
            i++;
        }
        i = 0;
        while (timeString.charAt(i) != ':') {
            if (i == 0 && timeString.charAt(i) == '0') {

            } else
                hour += timeString.charAt(i);
            i++;
        }
        i++;
        while (timeString.charAt(i) != ':') {
            if (i == 3 && timeString.charAt(i) == '0') {

            } else
                minutes += timeString.charAt(i);
            i++;
        }
        Calendar calendar = Calendar.getInstance();
        int yearInt = Integer.parseInt(year);
        int monthInt = Integer.parseInt(month);
        int dayInt = Integer.parseInt(day);
        int hourInt = Integer.parseInt(hour);
        int minutesInt = Integer.parseInt(minutes);
        calendar.set(yearInt, monthInt - 1, dayInt, hourInt,
                minutesInt, 0);

        // create the object

        long time = calendar.getTimeInMillis();
        return time;

    }
    private void deleteItem(int position)
    {
        //Deletes item from mAdapter and database and cancels alarm for that event
        EventItem event = mAdapter.getItem(position);
        Intent intent = new Intent(MainScreen.this, AlarmReceiver.class);
        EventItem.packageIntent(intent,event.getTitle(), event.getID(), EventItem.FORMAT.format(event.getStarDate()),
                EventItem.FORMAT.format(event.getEndDate()), EventItem.timeFORMAT.format(event.getStartTime()),
                EventItem.timeFORMAT.format(event.getEndTime()), event.getMuteSounds(), event.getSendText());
        intent.putExtra(isStartAlarm, true );
        intent.putExtra(POSITION, position);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainScreen.this, (int)event.getID(), intent, 0);
        alarm.cancel(pendingIntent);
        Toast.makeText(this, "Alarm deleted", Toast.LENGTH_LONG).show();
        intent.putExtra(isStartAlarm, false );
        pendingIntent = PendingIntent.getBroadcast(MainScreen.this, (int)event.getID()*10, intent, 0);
        alarm.cancel(pendingIntent);
        Toast.makeText(this, "Alarm delete 2", Toast.LENGTH_LONG).show();
        if(event.getMuteSounds()) {
            AudioManager amanager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
            amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
            amanager.setStreamMute(AudioManager.STREAM_ALARM, false);
            amanager.setStreamMute(AudioManager.STREAM_MUSIC, false);
            amanager.setStreamMute(AudioManager.STREAM_RING, false);
            amanager.setStreamMute(AudioManager.STREAM_SYSTEM, false);
            amanager.setStreamMute(AudioManager.STREAM_VOICE_CALL, false);
            amanager.setStreamMute(AudioManager.STREAM_DTMF, false);
        }
        database.deleteRow(event.getID());
    }
    public void editEvent(int position)
    {
        //cancels alarm and opens the AddEventActiivity for editing
        EventItem event = mAdapter.getItem(position);
        Intent intent = new Intent(getBaseContext(), AddEventActivity.class);
        EventItem.packageIntent(intent,event.getTitle(), event.getID(), EventItem.FORMAT.format(event.getStarDate()),
                EventItem.FORMAT.format(event.getEndDate()), EventItem.timeFORMAT.format(event.getStartTime()),
                EventItem.timeFORMAT.format(event.getEndTime()), event.getMuteSounds(), event.getSendText());
        intent.putExtra(isStartAlarm, true );
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainScreen.this, (int)event.getID(), intent, 0);
        alarm.cancel(pendingIntent);
        Toast.makeText(this, "Alarm deleted", Toast.LENGTH_LONG).show();
        intent.putExtra(isStartAlarm, false );
        pendingIntent = PendingIntent.getBroadcast(MainScreen.this, (int)event.getID()*10, intent, 0);
        alarm.cancel(pendingIntent);
        intent.putExtra(POSITION, position);
        startActivityForResult(intent, EDIT_REQUEST_CODE);
    }
}