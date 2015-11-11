package edu.byui.cs246.cs246_09_04;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;

/**
 * Simple activity that allows for the advancement and storage of a count value
 */
public class MainActivity extends AppCompatActivity {
    // CONSTANTS & SETTINGS
    private static final int DEFAULT_COUNT = 0;
    private static final boolean USE_SQL_FOR_STORAGE = true;

    // Local Variable
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get our stored count
        loadStoredCount();

        // Update our Count textView
        updateCountDisplay();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    /**
     * Advances the local count by 1
     * @param view View object calling this method (unused)
     */
    public void AdvanceCount(View view) {
        incrementCount();
        updateCountDisplay();
    }

    /**
     * Saves the current count
     * @param view View object calling this method
     */
    public void SaveCount(View view) {
        if ( storeCount() ) {
            if (USE_SQL_FOR_STORAGE) {
                Snackbar.make(view, R.string.feedback_sql_save_successful, Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            } else {
                Snackbar.make(view, R.string.feedback_pref_save_successful, Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        }
    }

    /**
     * Increments the count by 1
     */
    private void incrementCount() {
        count++;
    }

    /**
     * Retrieves the stored count value and assigns it to the local count variable
     */
    private void loadStoredCount() {
        // Get our stored count
        if (USE_SQL_FOR_STORAGE) {
            count = readCountFromSQLite();
        } else {
            count = readCountFromPreferences();
        }
    }

    /**
     * Attempts to retrieve a saved count value from SharedPreferences
     * @return the value saved in SharedPreferences, or a default value if no value is stored in
     * SharedPreferences
     */
    private int readCountFromPreferences() {
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        int count = prefs.getInt(getString(R.string.pref_key_count), DEFAULT_COUNT);
        return count;
    }

    /**
     * Attempts to retrieve a saved count value from a SQLite store
     * @return the value saved in the SQLite store, or a default value if no value is found
     */
    private int readCountFromSQLite() {
        // First, use an openhelper
        // Hopefully, getBaseContext will do whatever we need...
        CountOpenHelper openHelper = new CountOpenHelper(getBaseContext());

        // Great, now get a readable DB
        SQLiteDatabase db = openHelper.getReadableDatabase();

        String sql = "select * from " + openHelper.getTableName();
        Cursor cursor = db.rawQuery(sql, null);

        // If a row exists, grab the count from it
        if (cursor.getCount() > 0 ) {
            cursor.moveToFirst();
            int count = Integer.parseInt(cursor.getString(cursor.getColumnIndex("value")));
            db.close();
            return count;

        } else {
            db.close();
            return DEFAULT_COUNT;
        }
    }

    /**
     * Resets the count to 0
     */
    private void resetCount() {
        count = 0;
    }

    /**
     * Stores the local count variable into persistent storage
     * @return
     */
    private boolean storeCount() {
        if (USE_SQL_FOR_STORAGE) {
            return writeCountToSQLite(count);
        } else {
            return writeCountToPreferences(count);
        }
    }

    /**
     * Attempts to store a count value in SharedPreferences
     * @param count the value to store in SharedPreferences
     * @return true if successful
     */
    private boolean writeCountToPreferences(int count) {
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(getString(R.string.pref_key_count), count);
        editor.commit();

        return true;
    }

    /**
     * Attempts to store a count value in a SQLite store
     * @param count the value to store in the SQLite store
     * @return true if successful; otherwise, false.
     */
    private boolean writeCountToSQLite(int count) {

        // First, use an openhelper
        // Again, hopefully, getBaseContext will do whatever we need...
        CountOpenHelper openHelper = new CountOpenHelper(getBaseContext());

        // Great, now get a writable DB
        SQLiteDatabase db = openHelper.getWritableDatabase();

        // See if the count row has been stored yet
        String sql = "select * from " + openHelper.getTableName();
        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.getCount() < 1) {
            // No existing row; we need to insert the default value
            ContentValues values = new ContentValues();
            values.put(openHelper.getKeyColumnName(), openHelper.getCountKey());
            values.put(openHelper.getValueColumnName(), DEFAULT_COUNT);

            // Insert values into db
            db.insert(openHelper.getTableName(), null, values);
        }

        // Update with current count
        // Make the update string
        sql = "UPDATE " + openHelper.getTableName() +
                " SET " + openHelper.getValueColumnName() + "=" + Integer.toString(count) +
                " WHERE " + openHelper.getKeyColumnName() + "='" + openHelper.getCountKey() + "';";
        // Execute
        db.execSQL(sql);

        // Close db
        db.close();

        return true;
    }

    /**
     * Updates the display to reflect the current count
     */
    private void updateCountDisplay() {
        TextView textView = (TextView) this.findViewById(R.id.textCount);
        textView.setText(Integer.toString(count) );
    }
}
