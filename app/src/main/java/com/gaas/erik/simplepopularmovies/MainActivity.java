package com.gaas.erik.simplepopularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }

            MainActivityFragment mainActivityFragment = new MainActivityFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, mainActivityFragment).commit();
        } else {
            if (savedInstanceState != null) {
                return;
            }

            MainActivityFragment mainActivityFragment = new MainActivityFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.tablet_movies, mainActivityFragment).commit();
        }

    }


/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/

/*    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_share) {
            Toast.makeText(this, "I guess it worked", Toast.LENGTH_LONG).show();
            return true;

        }

        return super.onOptionsItemSelected(item);
    }*/


}
