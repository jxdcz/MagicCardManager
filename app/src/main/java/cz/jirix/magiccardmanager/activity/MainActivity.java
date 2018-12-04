package cz.jirix.magiccardmanager.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cz.jirix.magiccardmanager.R;
import cz.jirix.magiccardmanager.fragments.SearchFragment;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().add(R.id.container, SearchFragment.newInstance(), SearchFragment.TAG).commit();
    }
}

