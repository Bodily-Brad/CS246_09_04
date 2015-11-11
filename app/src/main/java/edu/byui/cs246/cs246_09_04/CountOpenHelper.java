package edu.byui.cs246.cs246_09_04;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by bradb on 11/11/2015.
 */
public class CountOpenHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_KEY = "key";
    private static final String DATABASE_VALUE = "value";

    private static final String KEY_COUNT = "count";

    private static final String COUNT_DB_NAME = "countDB";
    private static final String COUNT_TABLE_NAME = "countTable";
    private static final String COUNT_TABLE_CREATE =
            "CREATE TABLE " + COUNT_TABLE_NAME + " (" +
                    DATABASE_KEY + " TEXT, " +
                    DATABASE_VALUE + " TEXT);";

    CountOpenHelper(Context context) {
        super(context, COUNT_DB_NAME, null, DATABASE_VERSION );
        // Wish I knew more about Context, but I'm sure we'll stumble onto something sometime...
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create our database with two columns: key and value
        db.execSQL(COUNT_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Only one DB version at this point, so no upgrade handling necessary
    }

    public String getTableName() {
        return COUNT_TABLE_NAME;
    }

    public String getCountKey() {
        return KEY_COUNT;
    }

    public String getKeyColumnName() {
        return DATABASE_KEY;
    }

    public String getValueColumnName() {
        return DATABASE_VALUE;
    }
}
