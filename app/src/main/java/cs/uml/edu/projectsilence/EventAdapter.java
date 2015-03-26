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

    // Add a ToDoItem to the adapter
    // Notify observers that the data set has changed

    public void add(EventItem item) {

        mItems.add(item);
        notifyDataSetChanged();

    }

    // Clears the list adapter of all items.

    public void clear(){

        mItems.clear();
        notifyDataSetChanged();

    }

    // Returns the number of ToDoItems

    @Override
    public int getCount() {

        return mItems.size();

    }

    // Retrieve the number of ToDoItems

    @Override
    public Object getItem(int pos) {

        return mItems.get(pos);

    }

    // Get the ID for the ToDoItem
    // In this case it's just the position

    @Override
    public long getItemId(int pos) {

        return pos;

    }

    //Create a View to display the ToDoItem
    // at specified position in mItems

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        //TODO - Get the current ToDoItem
        final EventItem eventItem = (EventItem)getItem(position);

        //TODO - Inflate the View for this ToDoItem
        // from todo_item.xml.
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       RelativeLayout itemLayout = (RelativeLayout)inflater.inflate(R.layout.events, null);


        final TextView titleView = (TextView)itemLayout.findViewById(R.id.title);
        titleView.setText(eventItem.getTitle());


        final TextView startDateView = (TextView)itemLayout.findViewById(R.id.startDate);
        startDateView.setText(EventItem.FORMAT.format(eventItem.getStarDate()));



       final TextView endDateView = (TextView)itemLayout.findViewById(R.id.endDate);
       endDateView.setText(EventItem.FORMAT.format(eventItem.getEndDate()));


        // Return the View you just created
        return itemLayout;

    }



}
