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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;


public class FriendsList extends ListActivity {
    private ArrayList<String> values = new ArrayList<String>();
    private int stringIndex = 0;
    ArrayAdapter<String> adapter;
    private static Intent data;
    public String friendFile = "friendFile";

    FileInputStream inputStream;


    public void onCreate(Bundle bundle) {

        super.onCreate(bundle);
        data = getIntent();
        try {
            readFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    public void readFiles() throws IOException {
        String str="";
        int i = 0;
        StringBuffer buf = new StringBuffer();
        inputStream = openFileInput(friendFile);
        InputStream is = inputStream;
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        if (is!=null) {
            while ((str = reader.readLine()) != null) {
                buf.append(str + "\n" );
                values.add(i, str);
                i++;
            }
        }
        is.close();


    }
    public void addFriends( MenuItem item ){


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New Friend");

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
                setListAdapter(adapter);
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
        try {

            OutputStreamWriter out = new OutputStreamWriter(openFileOutput(friendFile, 0));
            BufferedWriter bwriter = new BufferedWriter(out);
            for( int i = 0; i < values.size(); i++){
                bwriter.write(values.get(i));
                bwriter.newLine();
            }
            bwriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        packageIntent( data, values );
        setResult( 2, data );
        finish();
    }

}

