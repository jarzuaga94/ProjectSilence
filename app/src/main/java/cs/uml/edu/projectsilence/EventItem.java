package cs.uml.edu.projectsilence;

import android.content.Intent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Jeremy on 3/25/2015.
 */
public class EventItem {
    static final String ITEM_SEP = System.getProperty("line.separator");
    public final static String TITLE = "title";
    public final static String START_TIME = "start_time";
    public final static String START_DATE = "start_date";
    public final static String END_DATE = "end_date";
    public final static String END_TIME = "end_date";

    public final static SimpleDateFormat FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss", Locale.US);

    private String mTitle = new String();
    private Date mStartDate = new Date();
    private Date mEndDate = new Date();

    EventItem(String title, Date start_date, Date end_date) {
        this.mTitle = title;
        this.mStartDate = start_date;
        this.mEndDate = end_date;
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

    public void End(Date date) {
        mEndDate = date;
    }
    public static void packageIntent(Intent intent, String title,
                                     String start_date, String end_date) {

        intent.putExtra(EventItem.TITLE, title);
        intent.putExtra(EventItem.START_DATE, start_date);
        intent.putExtra(EventItem.END_DATE, end_date);

    }

    public String toString() {
        return mTitle + ITEM_SEP + FORMAT.format(mStartDate) + ITEM_SEP + FORMAT.format(mEndDate);
    }
}
