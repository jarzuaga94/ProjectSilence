package cs.uml.edu.projectsilence;

import android.content.Intent;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class MainScreen extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create a new TodoListAdapter for this ListActivity's ListView
        //mAdapter = new ToDoListAdapter(getApplicationContext());

        // Put divider between ToDoItems and FooterView
        getListView().setFooterDividersEnabled(true);

        //TODO - Inflate footerView for footer_view.xml file
        TextView footerView = (TextView) this.getLayoutInflater().inflate(R.layout.footer, null);

        //TODO - Add footerView to ListView
        getListView().addFooterView(footerView);
        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO - Attach Listener to FooterView. Implement onClick().
                //Intent intent = new Intent(getBaseContext(), AddEvent.class);
                //startActivityForResult(intent, ADD_EVENT_R);

            }
        });

        //TODO - Attach the adapter to this ListActivity's ListView
        //getListView().setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
