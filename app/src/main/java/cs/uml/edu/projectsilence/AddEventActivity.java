package cs.uml.edu.projectsilence;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import java.util.Calendar;
import java.util.Date;


public class AddEventActivity extends Activity {
    private static String startTimeString;
    private static String startDateString;
    private static String endTimeString;
    private static String endDateString;
    private static String tempTime;
    private static String tempDate;
    private static TextView startDateView;
    private static TextView startTimeView;
    private static TextView endDateView;
    private static TextView endTimeView;
    private static boolean mute_sound;
    private static boolean send_text;
    private Date mStartDate;
    private Date mEndDate;

    private static int timePickerID = 0;
    private static int datePickerID = 0;

    private EditText mTitleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DBAdapter database = new DBAdapter(getApplicationContext());
        database.open();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_details);
        mTitleText = (EditText) findViewById(R.id.title);
        startDateView = (TextView) findViewById(R.id.start_date);
        startTimeView = (TextView) findViewById(R.id.start_time);
        endDateView = (TextView) findViewById(R.id.end_date);
        endTimeView = (TextView) findViewById(R.id.end_time);
        setDefaultDateTime();
        final Button startDatePickerButton = (Button) findViewById(R.id.start_date_picker_button);
        startDatePickerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                datePickerID = 1;
                showDatePickerDialog();
                //fixes the main screen problem but requires double click to register
                //startDateString = tempDate;
                //startDateView.setText(startDateString);
            }
        });
        final Button endDatePickerButton = (Button) findViewById(R.id.end_date_picker_button);
        endDatePickerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                datePickerID = 2;
                showDatePickerDialog();
                //fixes the main screen problem but requires double click to register
                //endDateString = tempDate;
                //endDateView.setText(endDateString);
            }
        });
        final Button startTimePickerButton = (Button) findViewById(R.id.start_time_picker_button);
        startTimePickerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                timePickerID = 1;
                showTimePickerDialog();
                //fixes the main screen problem but requires double click to register
                //startTimeString = tempTime;
                //startTimeView.setText(startTimeString);
            }
        });
        final Button endTimePickerButton = (Button) findViewById(R.id.end_time_picker_button);
        endTimePickerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                timePickerID = 2;
                showTimePickerDialog();
                //fixes the main screen problem but requires double click to register
                //endTimeString = tempTime;
                //endTimeView.setText(endTimeString);
            }
        });
        final ToggleButton sendMessageTB = (ToggleButton)findViewById(R.id.sendTextTB);
        sendMessageTB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    send_text = true;
                else
                    send_text = false;
            }
        });
        final ToggleButton muteSoundsTB = (ToggleButton)findViewById(R.id.muteSoundTB);
        muteSoundsTB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    mute_sound = true;
                else
                    mute_sound = false;
            }
        });


         final Button cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                setResult(RESULT_CANCELED, data);
                finish();
            }
        });

        final Button resetButton = (Button) findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTitleText.setText("");
                setDefaultDateTime();
            }
        });

        final Button submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleString = mTitleText.getText().toString();
                String startFullDate = startDateString + " " + startTimeString;
                String endFullDate = endDateString + " " + endTimeString;
                if( startTimeString == null ){
                    startTimeString = tempTime;
                }
                if( endTimeString == null ){
                    endTimeString = tempTime;
                }
                String startTime = startTimeString;
                String endTime = endTimeString;
                boolean muteSounds = mute_sound;
                boolean sendText = send_text;

                Intent data = new Intent();
                EventItem.packageIntent(data, titleString, startFullDate, endFullDate,startTime, endTime, muteSounds, sendText );
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }

    private void setDefaultDateTime() {
        mStartDate = new Date();
        mEndDate = new Date();
        mStartDate = new Date(mStartDate.getTime());
        mEndDate = new Date(mEndDate.getTime());



        Calendar c = Calendar.getInstance();
        c.setTime(mStartDate);
        c.setTime(mEndDate);
        setDateString(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH));
        startDateView.setText(tempDate);
        endDateView.setText(tempDate);
        //startDateView.setText(startDateString);
        //endDateView.setText(endDateString);


        setTimeString(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
                c.get(Calendar.MILLISECOND));
        startTimeView.setText(tempTime);
        endTimeView.setText(tempTime);


        //startTimeView.setText(startTimeString);
        //endTimeView.setText(endTimeString);
    }

    private static void setDateString(int year, int monthOfYear, int dayOfMonth) {
        monthOfYear++;
        String mon = "" + monthOfYear;
        String day = "" + dayOfMonth;
        if (monthOfYear < 10)
            mon = "0" + monthOfYear;
        if (dayOfMonth < 10)
            day = "0" + dayOfMonth;

        if( datePickerID == 1 ){
            startDateString = year + "-" + mon + "-" + day;
            startDateView.setText(startDateString);
        }
        else if( datePickerID == 2 ){
            endDateString = year + "-" + mon + "-" + day;
            endDateView.setText(endDateString);
        }
        else {
            tempDate = year + "-" + mon + "-" + day;
        }
    }

    private static void setTimeString(int hourOfDay, int minute, int mili) {
        String hour = "" + hourOfDay;
        String min = "" + minute;
        if (hourOfDay < 10)
            hour = "0" + hourOfDay;
        if (minute < 10)
            min = "0" + minute;

        if( timePickerID == 1 ){
            startTimeString = hour + ":" + min + ":00";
            startTimeView.setText(startTimeString);
        }
        else if( timePickerID == 2 ){
            endTimeString = hour + ":" + min + ":00";
            endTimeView.setText(endTimeString);
        }
        else{
            tempTime = hour + ":" + min + ":00";
            //startTimeView.setText(tempTime);
            //endTimeView.setText(tempTime);
        }


    }
    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            setDateString(year, monthOfYear, dayOfMonth);


            //startDateView.setText(startDateString);
            //endDateView.setText(endDateString);
        }

    }
    public static class TimePickerFragment extends DialogFragment implements
            TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    true);
        }


        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            setTimeString(hourOfDay, minute, 0);


            //startTimeView.setText(startTimeString);
            //endTimeView.setText(endTimeString);
        }
    }
    private void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    private void showTimePickerDialog() {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }
}