package com.mshvdvskgmail.fordearbnet.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import com.mshvdvskgmail.fordearbnet.R;
import com.mshvdvskgmail.fordearbnet.fragments.NoteListFragment;

/**
 *  main activity loads NoteListFragment, which provides the user with the list of notes and "creat_new" button
 */

public class MainActivity extends AppCompatActivity {


    private Toolbar mToolbar;
    private NoteListFragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        fragment = new NoteListFragment();
        openFragment(fragment, "Notes");

    }

    private void openFragment(final Fragment fragment, String title){
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
        getSupportActionBar().setTitle(title);
    }


}
