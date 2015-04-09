package cs.uml.edu.projectsilence;

import android.content.Intent;
import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class MainScreen extends ListActivity {
    private static final int ADD_EVENT_REQUEST = 0;
    private static String Title;
    private static String StartDate;
    private static String StartTime;
    private static String EndDate;
    private static String EndTime;
    private static boolean MuteSound;
    private static boolean SendText;

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
       /* footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), AddEventActivity.class);
                startActivityForResult(intent, ADD_EVENT_REQUEST);
            }
        });*/
        getListView().setAdapter(mAdapter);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_EVENT_REQUEST) {
            if (resultCode == RESULT_OK) {
                EventItem eventItem = new EventItem(data);
                mAdapter.add(eventItem);
                database.insertRow(eventItem.getTitle(), EventItem.timeFORMAT.format(eventItem.getStartTime()),
                        EventItem.FORMAT.format(eventItem.getStarDate()), EventItem.timeFORMAT.format(eventItem.getEndTime()),
                        EventItem.FORMAT.format(eventItem.getEndDate()), eventItem.getMuteSounds(), eventItem.getSendText());
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

    }
}
