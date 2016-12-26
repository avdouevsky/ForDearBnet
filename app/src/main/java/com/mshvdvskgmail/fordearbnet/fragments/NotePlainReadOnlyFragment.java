package com.mshvdvskgmail.fordearbnet.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mshvdvskgmail.fordearbnet.R;
import com.mshvdvskgmail.fordearbnet.activities.MainActivity;
import com.mshvdvskgmail.fordearbnet.data.NoteManager;
import com.mshvdvskgmail.fordearbnet.models.Note;

import java.io.Serializable;

import static android.R.attr.id;

/**
 * Created by mshvd_000 on 18.12.2016.
 */

public class NotePlainReadOnlyFragment extends Fragment{
    private View mRootView;
    private TextView mContentEditText;
    private Note mCurrentNote = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getCurrentNote();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_note_reader, container, false);
        mContentEditText = (TextView)mRootView.findViewById(R.id.read_text_note);
        return mRootView;
    }

    public static NotePlainReadOnlyFragment newInstance(Note note){
        NotePlainReadOnlyFragment fragment = new NotePlainReadOnlyFragment();

        if (note != null){
            Bundle bundle = new Bundle();
            bundle.putSerializable("note", note);
            fragment.setArguments(bundle);
        }
        return fragment;
    }


    private void getCurrentNote(){
        Bundle args = getArguments();
        if (args != null && args.containsKey("note")){
            Serializable sNote = args.getSerializable("note");
            Note note = (Note) sNote;
            if (note != null){
                mCurrentNote = note;
            }
        }
    }


    private void populateFields() {
        mContentEditText.setText(mCurrentNote.getContent());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mCurrentNote != null){
            populateFields();
        }
    }

}
