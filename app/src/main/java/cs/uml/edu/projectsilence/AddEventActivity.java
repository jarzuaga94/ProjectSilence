package cs.uml.edu.projectsilence;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;


public class AddEventActivity extends Activity {

    // 7 days in milliseconds - 7 * 24 * 60 * 60 * 1000
    private static final int SEVEN_DAYS = 604800000;
    private static final String TAG = "Lab-UserInterface";
    private static String startTimeString;
    private static String startDateString;
    private static String endTimeString;
    private static String endDateString;
    private static TextView startDateView;
    private static TextView startTimeView;
    private static TextView endDateView;
    private static TextView endTimeView;
    private Date mStartDate;
    private Date mEndDate;
    private EditText mTitleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
                showDatePickerDialog();
            }
        });
        final Button endDatePickerButton = (Button) findViewById(R.id.end_date_picker_button);
        endDatePickerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                showDatePickerDialog();
            }
        });
        final Button startTimePickerButton = (Button) findViewById(R.id.start_time_picker_button);
        startTimePickerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });
        final Button endTimePickerButton = (Button) findViewById(R.id.end_time_picker_button);
        endTimePickerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                showTimePickerDialog();
            }
        });

         final Button cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                log("Entered cancelButton.OnClickListener.onClick()");
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
                Intent data = new Intent();
                EventItem.packageIntent(data, titleString, startFullDate, endFullDate);
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }

    private void setDefaultDateTime() {
        mStartDate = new Date();
        mEndDate = new Date();
        mStartDate = new Date(mStartDate.getTime() + SEVEN_DAYS);
        mEndDate = new Date(mEndDate.getTime() + SEVEN_DAYS);
        Calendar c = Calendar.getInstance();
        c.setTime(mStartDate);
        c.setTime(mEndDate);
        setDateString(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH));
        startDateView.setText(startDateString);
        endDateView.setText(endDateString);
        setTimeString(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
                c.get(Calendar.MILLISECOND));
        startTimeView.setText(startTimeString);
        endTimeView.setText(endTimeString);
    }

    private static void setDateString(int year, int monthOfYear, int dayOfMonth) {
        monthOfYear++;
        String mon = "" + monthOfYear;
        String day = "" + dayOfMonth;
        if (monthOfYear < 10)
            mon = "0" + monthOfYear;
        if (dayOfMonth < 10)
            day = "0" + dayOfMonth;
        startDateString = year + "-" + mon + "-" + day;
        endDateString = year + "-" + mon + "-" + day;
    }

    private static void setTimeString(int hourOfDay, int minute, int mili) {
        String hour = "" + hourOfDay;
        String min = "" + minute;
        if (hourOfDay < 10)
            hour = "0" + hourOfDay;
        if (minute < 10)
            min = "0" + minute;
        startTimeString = hour + ":" + min + ":00";
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

            startDateView.setText(startDateString);
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
            startTimeView.setText(startTimeString);
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

    private void log(String msg) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i(TAG, msg);
    }
}