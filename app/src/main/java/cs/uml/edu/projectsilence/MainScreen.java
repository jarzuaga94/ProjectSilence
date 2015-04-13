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
                database.insertRow(eventItem.getTitle(),StartTime,
                        StartDate, EventItem.timeFORMAT.format(eventItem.getEndTime()),
                        EventItem.FORMAT.format(eventItem.getEndDate()), eventItem.getMuteSounds(), eventItem.getSendText());

                scheduleAlarm(getListView());
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

        /*if (id == R.id.action_settings) {
            return true;
        }*/
        switch (item.getItemId()) {
            case R.id.action_add:
                //addEventSelected();
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        //return super.onOptionsItemSelected(item);
    }

    public void addEventSelected( MenuItem item){

        Intent intent = new Intent(getBaseContext(), AddEventActivity.class);
        startActivityForResult(intent, ADD_EVENT_REQUEST);

    }

    public void removeEvents( MenuItem item){
        mAdapter.clear();
        database.deleteAll();

    }
    public void scheduleAlarm(View V)
    {
        String year = "";
        String month = "";
        String day = "";
        String hour = "";
        String minutes = "";
        int i = 0;
        while(StartDate.charAt(i)!= '-') {
            year += StartDate.charAt(i);
            i++;
        }
        i++;
        while(StartDate.charAt(i)!= '-') {
            month += StartDate.charAt(i);
            i++;
        }
        i++;
        while(StartDate.length() !=  i) {
            day += StartDate.charAt(i);
            i++;
        }
        i = 0;
        while(StartTime.charAt(i)!= ':') {
            hour += StartTime.charAt(i);
            i++;
        }
        i++;
        while(StartTime.charAt(i)!= ':') {
            minutes += StartTime.charAt(i);
            i++;
        }
        Intent startIntent = new Intent(MainScreen.this, AlarmReceiver.class);
        PendingIntent startPIntent = PendingIntent.getBroadcast(MainScreen.this, 0, startIntent, 0);
        AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day), Integer.parseInt(hour),
                Integer.parseInt(minutes), 0);

        // create the object


        //set the alarm for particular time
        alarm.set(alarm.RTC_WAKEUP,calendar.getTimeInMillis(),startPIntent);
        Toast.makeText(this, "Alarm created", Toast.LENGTH_LONG).show();

    }
}
