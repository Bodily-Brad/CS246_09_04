package edu.byui.cs246.cs246_09_04;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    // CONSTANTS & SETTINGS
    private static final int DEFAULT_COUNT = 0;

    // Local Variable
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Get our count from shared preferences
        count = readCountFromPreferences();

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
        if ( writeCountToPreferences(count) ) {
            Snackbar.make(view, R.string.feedback_pref_save_successful, Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }
    }

    /**
     * Increments the count by 1
     */
    private void incrementCount() {
        count++;
    }

    /**
     * Resets the count to 0
     */
    private void resetCount() {
        count = 0;
    }

    /**
     * Attempts to retrieve a saved count value from SharedPreferences
     * @return the value saved in SharedPreferences, or a default value if no value is store in
     * SharedPreferences
     */
    private int readCountFromPreferences() {
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        int count = prefs.getInt(getString(R.string.pref_key_count), DEFAULT_COUNT);
        return count;
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
     * Updates the disply to reflect the current count
     */
    private void updateCountDisplay() {
        TextView textView = (TextView) this.findViewById(R.id.textCount);
        textView.setText(Integer.toString(count) );
    }
}
