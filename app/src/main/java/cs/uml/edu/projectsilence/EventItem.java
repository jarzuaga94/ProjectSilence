package cs.uml.edu.projectsilence;

import android.content.Intent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
public class EventItem {
    static final String ITEM_SEP = System.getProperty("line.separator");
    public final static String TITLE = "title";
    public final static String START_DATE = "start_date";
    public final static String END_DATE = "end_date";
    public final static String START_TIME = "start_time";
    public final static String END_TIME = "end_time";
    public final static String MUTE_SOUND = "mute_sounds";
    public final static String SEND_TEXT = "send_text";


    public final static SimpleDateFormat FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd", Locale.US);

    public final static SimpleDateFormat timeFORMAT = new SimpleDateFormat(
            "HH:mm:ss", Locale.US);

    private String mTitle = new String();
    private Date mStartDate = new Date();
    private Date mEndDate = new Date();
    private Date mStartTime = new Date();
    private Date mEndTime = new Date();
    private boolean mSendText = false;
    private boolean mMuteSounds = false;

    EventItem(String title, Date start_date, Date end_date, Date start_time, Date end_time, boolean mute_sounds, boolean send_text) {
        this.mTitle = title;
        this.mStartDate = start_date;
        this.mEndDate = end_date;
        this.mStartTime = start_time;
        this.mEndTime = end_time;
        this.mSendText = send_text;
        this.mMuteSounds = mute_sounds;
    }
    EventItem(Intent intent) {

        mTitle = intent.getStringExtra(EventItem.TITLE);
        try {
            mStartDate = EventItem.FORMAT.parse(intent.getStringExtra(EventItem.START_DATE));
        } catch (ParseException e) {
            mStartDate = new Date();
        }
        try{
            mEndDate = EventItem.FORMAT.parse(intent.getStringExtra(EventItem.END_DATE));
        }catch (ParseException e){
            mEndDate = new Date();
        }
        try {
            mStartTime = EventItem.timeFORMAT.parse(intent.getStringExtra(EventItem.START_TIME));
        } catch (ParseException e) {
            mStartTime = new Date();
        }
        try{
            mEndTime = EventItem.timeFORMAT.parse(intent.getStringExtra(EventItem.END_TIME));
        }catch (ParseException e){
            mEndTime = new Date();
        }
        mMuteSounds = intent.getBooleanExtra(EventItem.MUTE_SOUND, false);
        mSendText = intent.getBooleanExtra(EventItem.MUTE_SOUND, false);
    }
    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }
    public Date getStarDate() {
        return mStartDate;
    }

    public void setStartDate(Date date) {
        mStartDate = date;
    }
    public Date getEndDate() {
        return mEndDate;
    }

    public void setStartTime(Date date) {  mStartTime = date; }
    public Date getStartTime() {
        return mStartTime;
    }

    public void setEndTime(Date date) {
        mEndTime = date;
    }
    public Date getEndTime() {
        return mEndTime;
    }

    public void setEndDate(Date date) {
        mEndDate = date;
    }
    public boolean getMuteSounds(){
        return mMuteSounds;

    }
    public boolean getSendText(){
        return mSendText;
    }
    public static void packageIntent(Intent intent, String title,
                                     String start_date, String end_date,
                                     String start_time, String end_time, boolean mute_sounds, boolean send_text) {

        intent.putExtra(EventItem.TITLE, title);
        intent.putExtra(EventItem.START_DATE, start_date);
        intent.putExtra(EventItem.END_DATE, end_date);
        intent.putExtra(EventItem.START_TIME, start_time);
        intent.putExtra(EventItem.END_TIME, end_time );
        intent.putExtra(EventItem.SEND_TEXT, send_text);
        intent.putExtra(EventItem.MUTE_SOUND, mute_sounds);

    }
    public String toString() {
        return mTitle + ITEM_SEP + FORMAT.format(mStartDate) + ITEM_SEP + FORMAT.format(mEndDate) + ITEM_SEP + timeFORMAT.format(mStartTime) + ITEM_SEP + timeFORMAT.format(mEndTime);
    }
}
