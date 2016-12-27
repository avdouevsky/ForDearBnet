package com.mshvdvskgmail.fordearbnet.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.melnykov.fab.FloatingActionButton;
import com.mshvdvskgmail.fordearbnet.R;
import com.mshvdvskgmail.fordearbnet.activities.NoteEditorActivity;
import com.mshvdvskgmail.fordearbnet.adapters.NoteListAdapter;
import com.mshvdvskgmail.fordearbnet.data.NoteManager;
import com.mshvdvskgmail.fordearbnet.interfaces.CallBack;
import com.mshvdvskgmail.fordearbnet.models.Note;
import com.mshvdvskgmail.fordearbnet.utilities.SampleData;
import java.io.Serializable;
import java.util.List;

/**
 * The fragment displays the list of notes.
 */

public class NoteListFragment extends Fragment implements CallBack {

    private FloatingActionButton mFab;
    private View mRootView;
    private List<Note> mNotes;
    private RecyclerView mRecyclerView;
    private NoteListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private AlertDialog alert;

    public NoteListFragment() {}
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_note_list, container, false);
        mFab = (FloatingActionButton)mRootView.findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NoteEditorActivity.class));
            }
        });
        setupList();
        return mRootView;
    }

    private void setupList() {
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.note_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        final GestureDetector mGestureDetector =
                new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener(){
                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        return true;
                    }
                });

        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
                    int position = recyclerView.getChildLayoutPosition(child);
                    Note selectedNote = mNotes.get(position);
                    Serializable passNote = selectedNote;
                    Intent editorIntent = new Intent(getActivity(), NoteEditorActivity.class);
                    editorIntent.putExtra("note", passNote);
                    startActivity(editorIntent);
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {}

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}
        });

        mNotes = SampleData.getSampleNotes();  //
        mAdapter = new NoteListAdapter(mNotes);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setVisibility(View.GONE);
        refreshData();
    }

    // Method is called by AlertDialog by push on "refresh" button, in case there is no connection

    public void refreshData(){
        NoteManager.newInstance(getActivity()).getAllNotes(this);
    }


    @Override
    public void onTaskCompleted(List<Note> values) {
        mAdapter.refreshData(values);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFailedConnection(String result) {
        promptForConnectionCheck(result);
    }


    public void promptForConnectionCheck (String result){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("No internet connection");
        alertDialog.setMessage("Failed to connect to the server. Please, check you network.");
        alertDialog.setPositiveButton("Refresh", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                refreshData();
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

}
