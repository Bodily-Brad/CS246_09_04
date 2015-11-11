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

    public void AdvanceCount(View view) {
        incrementCount();
        updateCountDisplay();
    }

    public void SaveCount(View view) {
        if ( writeCountToPreferences(count) ) {
            Snackbar.make(view, "Count Saved", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    private void incrementCount() {
        count++;
    }

    private int readCountFromPreferences() {
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        int count = prefs.getInt("count", 10);
        return count;
    }

    private boolean writeCountToPreferences(int count) {
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("count", count);
        editor.commit();

        return true;
    }

    private void updateCountDisplay() {
        TextView textView = (TextView) this.findViewById(R.id.textCount);
        textView.setText(Integer.toString(count) );
    }
}
