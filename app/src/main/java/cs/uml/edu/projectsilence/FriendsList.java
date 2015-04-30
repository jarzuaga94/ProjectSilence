package cs.uml.edu.projectsilence;

/**
 * Created by Cody on 4/29/2015.
 */


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.app.ListActivity;
import android.widget.Toast;

import java.util.ArrayList;


public class FriendsList extends ListActivity {

    //String[] values = new String[];
    private ArrayList<String> values = new ArrayList<String>();
    private int stringIndex = 0;
    ArrayAdapter<String> adapter;
    private static Intent data;


    public void onCreate(Bundle bundle) {

        super.onCreate(bundle);

        data = getIntent();
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.friend_menu, menu);
        return true;

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_friends:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addFriends( MenuItem item ){


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Title");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType( InputType.TYPE_CLASS_TEXT );
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                values.add(input.getText().toString());
                //stringIndex++;
                setListAdapter(adapter);
                //dialog.cancel();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String item = (String) getListAdapter().getItem(position);
        Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
        adapter.remove(item);
    }

    public static void packageIntent(Intent intent, ArrayList<String> list ) {

        data.putStringArrayListExtra( "friends", list);

    }

    public void saveFriends( MenuItem item ){
        packageIntent( data, values );
        setResult( 2, data );
        finish();
    }

}

