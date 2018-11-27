package cz.jirix.magiccardmanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cz.jirix.magiccardmanager.fragments.SearchFragment;

/**
 * A login screen that offers login via email/password.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Set up the login form.

        getSupportFragmentManager().beginTransaction().add(R.id.container, SearchFragment.newInstance(), SearchFragment.TAG).commit();

    }





}

