package cs.uml.edu.projectsilence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
	private static final String TAG = "DBAdapter";
	
	// DB Fields
	public static final String KEY_ROWID = "_id";
	public static final int COL_ROWID = 0;
	public static final String KEY_NAME = "name";
	public static final String KEY_STARTTIME = "startTime";
	public static final String KEY_STARTDATE = "startDate";
    public static final String KEY_ENDTIME = "endTime";
    public static final String KEY_ENDDATE = "endDate";
    public static final String KEY_MUTESOUND = "muteSound";
    public static final String KEY_SENDMESSAGE = "sendMessage";

	public static final int COL_NAME = 1;
	public static final int COL_STARTTIME = 2;
	public static final int COL_STARTDATE = 3;
    public static final int COL_ENDTIME = 4;
    public static final int COL_ENDDATE = 5;
    public static final int COL_MUTESOUND = 6;
    public static final int COL_SENDMESSAGE = 7;

	
	public static final String[] ALL_KEYS = new String[] {KEY_ROWID, KEY_NAME, KEY_STARTTIME,
        KEY_STARTDATE, KEY_ENDTIME, KEY_ENDDATE, KEY_MUTESOUND, KEY_SENDMESSAGE};
	

	public static final String DATABASE_NAME = "Project_Silence";
	public static final String DATABASE_TABLE = "mainTable";
	public static final int DATABASE_VERSION = 1;
	
	private static final String DATABASE_CREATE_SQL = 
			"create table " + DATABASE_TABLE 
			+ " (" + KEY_ROWID + " integer primary key autoincrement, "
			+ KEY_NAME + " text not null, "
			+ KEY_STARTTIME + " string not null, "
			+ KEY_STARTDATE + " string not null, "
            + KEY_ENDTIME + " string not null, "
            + KEY_ENDDATE + " string not null, "
            + KEY_MUTESOUND + " boolean not null, "
            + KEY_SENDMESSAGE + " boolean not null"
			+ ");";

	private final Context context;
	
	private DatabaseHelper myDBHelper;
	private SQLiteDatabase db;


	
	public DBAdapter(Context ctx) {
		this.context = ctx;
		myDBHelper = new DatabaseHelper(context);
	}

	public DBAdapter open() {
		db = myDBHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		myDBHelper.close();
	}
	

	public long insertRow(String name, String startTime, String startDate, String endTime,
                          String endDate, boolean muteSound, boolean sendMessage) {

		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NAME, name);
		initialValues.put(KEY_STARTTIME, startTime);
		initialValues.put(KEY_STARTDATE, startDate);
        initialValues.put(KEY_ENDTIME, endTime);
        initialValues.put(KEY_ENDDATE, endDate);
        initialValues.put(KEY_MUTESOUND, muteSound);
        initialValues.put(KEY_SENDMESSAGE, sendMessage);
		return db.insert(DATABASE_TABLE, null, initialValues);
	}
	public boolean deleteRow(long rowId) {
		String where = KEY_ROWID + "=" + rowId;
		return db.delete(DATABASE_TABLE, where, null) != 0;
	}
	
	public void deleteAll() {
		Cursor c = getAllRows();
		long rowId = c.getColumnIndexOrThrow(KEY_ROWID);
		if (c.moveToFirst()) {
			do {
				deleteRow(c.getLong((int) rowId));				
			} while (c.moveToNext());
		}
		c.close();
	}

	public Cursor getAllRows() {
		String where = null;
		Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS, 
							where, null, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}

	public Cursor getRow(long rowId) {
		String where = KEY_ROWID + "=" + rowId;
		Cursor c = 	db.query(true, DATABASE_TABLE, ALL_KEYS, 
						where, null, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}

	public boolean updateRow(long rowId, String name, String startTime, String startDate,
                             String endDate, String endTime, boolean muteSound, boolean sendMessage) {
		String where = KEY_ROWID + "=" + rowId;
		ContentValues newValues = new ContentValues();
		newValues.put(KEY_NAME, name);
		newValues.put(KEY_STARTDATE, startDate);
		newValues.put(KEY_STARTTIME, startTime);
        newValues.put(KEY_ENDDATE, endDate);
        newValues.put(KEY_ENDTIME, endTime);
        newValues.put(KEY_MUTESOUND, muteSound);
        newValues.put(KEY_SENDMESSAGE, sendMessage);
		

		return db.update(DATABASE_TABLE, newValues, where, null) != 0;
	}
	private static class DatabaseHelper extends SQLiteOpenHelper
	{
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase _db) {
			_db.execSQL(DATABASE_CREATE_SQL);			
		}

		@Override
		public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading application's database from version " + oldVersion
					+ " to " + newVersion + ", which will destroy all old data!");
			
			// Destroy old database:
			_db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			
			// Recreate new database:
			onCreate(_db);
		}
	}
}
