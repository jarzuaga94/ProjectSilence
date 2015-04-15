package cs.uml.edu.projectsilence;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.Calendar;


public class MainScreen extends ListActivity {
    private static final int ADD_EVENT_REQUEST = 0;
    private static String Title;
    private static String StartDate;
    private static String StartTime;
    private static String EndDate;
    private static String EndTime;
    private static boolean MuteSound;
    private static boolean SendText;
    public static String isStartAlarm;
    public static int alarmID = 0;
    private float x1, y1, x2, y2;

    EventAdapter mAdapter;
    DBAdapter database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = new DBAdapter(getApplication());
        database.open();
        mAdapter = new EventAdapter(getApplicationContext());
        //getListView().setFooterDividersEnabled(true);
        //TextView footerView = (TextView) this.getLayoutInflater().inflate(R.layout.footer, null);
        //getListView().addFooterView(footerView);
        Cursor cursor = database.getAllRows();
        cursor.moveToFirst();
        Intent data = new Intent();
        EventItem eventItem;
        while(!cursor.isAfterLast()) {
            Title = cursor.getString(DBAdapter.COL_NAME);
            StartDate = cursor.getString(DBAdapter.COL_STARTDATE);
            StartTime = cursor.getString(DBAdapter.COL_STARTTIME);
            EndDate = cursor.getString(DBAdapter.COL_ENDDATE);
            EndTime = cursor.getString(DBAdapter.COL_ENDTIME);
            MuteSound = cursor.getInt(DBAdapter.COL_MUTESOUND) > 0;
            SendText = cursor.getInt(DBAdapter.COL_SENDMESSAGE) > 0;
            EventItem.packageIntent(data, Title, StartDate, EndDate, StartTime, EndTime, MuteSound, SendText);
            eventItem = new EventItem(data);
            mAdapter.add(eventItem);
            cursor.moveToNext();
        }
        cursor.close();
        getListView().setAdapter(mAdapter);

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mAdapter.remove(position);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_EVENT_REQUEST) {
            if (resultCode == RESULT_OK) {
                EventItem eventItem = new EventItem(data);
                mAdapter.add(eventItem);
                StartDate = EventItem.FORMAT.format(eventItem.getStarDate());
                StartTime = EventItem.timeFORMAT.format(eventItem.getStartTime());
                EndDate = EventItem.FORMAT.format(eventItem.getEndDate());
                EndTime = EventItem.timeFORMAT.format(eventItem.getEndTime());
                database.insertRow(eventItem.getTitle(),StartTime,
                        StartDate, EndTime, EndDate, eventItem.getMuteSounds(), eventItem.getSendText());
                scheduleAlarm(getListView(), data);
            }
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


    //
    //-Called when ADD Event is clicked from actionbar/settings tab.
    //-Creates an intent and calls it to switch to addEvent activity.
    //
    public void addEventSelected( MenuItem item){

        Intent intent = new Intent(getBaseContext(), AddEventActivity.class);
        startActivityForResult(intent, ADD_EVENT_REQUEST);

    }

    //
    //-Called when Remove All Events is clicked from actionbar/settings tab
    //-Clears all events from screen also deletes events from database.
    //
    public void removeEvents( MenuItem item){
        mAdapter.clear();
        database.deleteAll();
        alarmID = 0;

    }
    public void scheduleAlarm(View V, Intent data)
    {
        Intent startIntent = new Intent(MainScreen.this, AlarmReceiver.class);
        Intent endIntent = new Intent(MainScreen.this, AlarmReceiver.class);
        startIntent.replaceExtras(data);
        endIntent.replaceExtras(data);
        startIntent.putExtra(isStartAlarm, true );
        PendingIntent startPIntent = PendingIntent.getBroadcast(MainScreen.this, alarmID, startIntent, 0);
        alarmID++;
        endIntent.putExtra(isStartAlarm, false);
        PendingIntent endPIntent = PendingIntent.getBroadcast(MainScreen.this, alarmID, endIntent, 0);
        alarmID++;
        AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        //set the alarm for particular time
        alarm.set(alarm.RTC,getMilli(StartDate, StartTime),startPIntent);
        alarm.set(alarm.RTC, getMilli(EndDate, EndTime),endPIntent);
        Toast.makeText(this, "Alarm created", Toast.LENGTH_LONG).show();

    }
    private long getMilli(String dateString, String timeString) {
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
}
