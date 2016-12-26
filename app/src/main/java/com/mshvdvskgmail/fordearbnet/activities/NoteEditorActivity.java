package com.mshvdvskgmail.fordearbnet.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.mshvdvskgmail.fordearbnet.R;
import com.mshvdvskgmail.fordearbnet.fragments.NotePlainEditorFragment;
import com.mshvdvskgmail.fordearbnet.fragments.NotePlainReadOnlyFragment;
import com.mshvdvskgmail.fordearbnet.models.Note;

import java.io.Serializable;

/**
 * Created by mshvd_000 on 12.12.2016.
 */

public class NoteEditorActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);


        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null){
            Bundle args = getIntent().getExtras();
            if (args != null && args.containsKey("note")){
                Serializable sNote = args.getSerializable("note");
                Note note = (Note)sNote;

                if (note != null){
                    NotePlainReadOnlyFragment fragment = NotePlainReadOnlyFragment.newInstance(note);
                    openFragment(fragment, "Old one");
                }
            } else{
                openFragment(new NotePlainEditorFragment(), "New one");
            }

        }

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
