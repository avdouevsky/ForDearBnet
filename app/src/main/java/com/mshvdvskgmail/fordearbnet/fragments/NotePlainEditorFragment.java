package com.mshvdvskgmail.fordearbnet.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
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
import android.widget.Toast;

import com.mshvdvskgmail.fordearbnet.R;
import com.mshvdvskgmail.fordearbnet.activities.MainActivity;
import com.mshvdvskgmail.fordearbnet.data.NoteManager;
import com.mshvdvskgmail.fordearbnet.interfaces.CallBack;
import com.mshvdvskgmail.fordearbnet.models.Note;

import java.util.List;

/**
 * Created by mshvd_000 on 12.12.2016.
 */

public class NotePlainEditorFragment extends Fragment implements CallBack {
    private View mRootView;
    private EditText mContentEditText;
    private Note mCurrentNote = null;
    private AlertDialog alert;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_note_edit_plain, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_note_plain_editor, container, false);
        mContentEditText = (EditText)mRootView.findViewById(R.id.edit_text_note);
        return mRootView;
    }

    public NotePlainEditorFragment newInstance(long id){
        NotePlainEditorFragment fragment = new NotePlainEditorFragment();

        if (id > 0){
            Bundle bundle = new Bundle();
            bundle.putLong("id", id);
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_cancel:
                //startActivity(new Intent(getActivity(), MainActivity.class));
                //promptForDelete();

                if (!TextUtils.isEmpty(mContentEditText.getText())){
                    promptForDelete();
                }else {
                    startActivity(new Intent(getActivity(), MainActivity.class)); // может вызвать хуйню
                }

                break;
            case R.id.action_save:
                //save note
                saveNote();
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean saveNote(){

        String content = mContentEditText.getText().toString();
        if (TextUtils.isEmpty(content)){
            mContentEditText.setError("Content is required");
            return false;
        }

        Note note = new Note();
        note.setContent(content);
        NoteManager.newInstance(getActivity()).create(note, this);
        return true;

    }

    private void makeToast(String message){
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private void populateFields() {
        //mTitleEditText.setText(mCurrentNote.getTitle());
        mContentEditText.setText(mCurrentNote.getContent());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mCurrentNote != null ){
            populateFields();
        }
    }

    public void promptForDelete(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Cancel this note?");
        alertDialog.setMessage("Are you sure you want to cancel the note?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                makeToast("note canceled");
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public void promptForConnectionCheck(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("No internet connection");
        alertDialog.setMessage("Failed to connect to the server. Please, check you network.");
        alertDialog.setPositiveButton("Refresh", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveNote();
            }
        });
        alertDialog.setNegativeButton("Wi-Fi Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
            }
        });
        alert = alertDialog.create();
        alert.show();
    }


    @Override
    public void onTaskCompleted(List<Note> values) {
        makeToast("Note saved");
        startActivity(new Intent(getActivity(), MainActivity.class));
    }

    @Override
    public void onFailedConnection(String result) {
        promptForConnectionCheck();
    }
}
