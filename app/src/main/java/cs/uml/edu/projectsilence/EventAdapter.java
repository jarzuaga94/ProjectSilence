package cs.uml.edu.projectsilence;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;


public class EventAdapter extends BaseAdapter {
    private final List<EventItem> mItems = new ArrayList<EventItem>();
    private final Context mContext;
    private static final String TAG = "Lab-UserInterface";
    public EventAdapter(Context context) {
        mContext = context;
    }
    public void add(EventItem item) {
        mItems.add(item);
        notifyDataSetChanged();
    }
    public void clear(){
        mItems.clear();
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return mItems.size();
    }
    @Override
    public Object getItem(int pos) {
        return mItems.get(pos);
    }
    @Override
    public long getItemId(int pos) {
        return pos;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final EventItem eventItem = (EventItem)getItem(position);
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout itemLayout = (RelativeLayout)inflater.inflate(R.layout.events, null);
        final TextView titleView = (TextView)itemLayout.findViewById(R.id.title);
        titleView.setText(eventItem.getTitle());
        final TextView startDateView = (TextView)itemLayout.findViewById(R.id.startDate);
        startDateView.setText(EventItem.FORMAT.format(eventItem.getStarDate()));
        final TextView endDateView = (TextView)itemLayout.findViewById(R.id.endDate);
        endDateView.setText(EventItem.FORMAT.format(eventItem.getEndDate()));

        final TextView startTimeView = (TextView)itemLayout.findViewById(R.id.startTime);
        startTimeView.setText(EventItem.timeFORMAT.format(eventItem.getStartTime()));

        final TextView endTimeView = (TextView)itemLayout.findViewById(R.id.endTime);
        endTimeView.setText(EventItem.timeFORMAT.format(eventItem.getEndTime()));

        return itemLayout;

    }
}
