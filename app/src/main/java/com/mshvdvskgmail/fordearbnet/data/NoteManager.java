package com.mshvdvskgmail.fordearbnet.data;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.mshvdvskgmail.fordearbnet.interfaces.CallBack;
import com.mshvdvskgmail.fordearbnet.interfaces.SessionOpener;
import com.mshvdvskgmail.fordearbnet.models.Note;
import com.mshvdvskgmail.fordearbnet.utilities.Constants;

import static android.R.attr.id;

/**
 * Created by mshvd_000 on 15.12.2016.
 */

public class  NoteManager implements SessionOpener {

    private Context mContext;
    private static NoteManager sNoteManagerInstance = null;
    private DatabaseHelper db;

    public static NoteManager newInstance(Context context){

        if (sNoteManagerInstance == null){
            sNoteManagerInstance = new NoteManager(context.getApplicationContext());
            //sNoteManagerInstance.opensession();
        }

        return sNoteManagerInstance;
    }

    private NoteManager(Context context){
        this.mContext = context.getApplicationContext();
        db = new DatabaseHelper(mContext);
    }

    public void create(Note note) {
        NoteContentApiProvider a = new NoteContentApiProvider(note.getContent(), db);
        a.create();
    }


    public void getAllNotes(CallBack listener) {
        NoteContentApiProvider a = new NoteContentApiProvider(listener, "qLxl9bGXELkCkfpHh7", this, db);
        a.getAllNotes();
    }


    /*
    public void opensession(){
        NoteContentApiProvider a = new NoteContentApiProvider(this);
        a.startSession();
    }
    */

    @Override
    public void onSessionKeyGenerated (String sessionKey) {
        this.saveSessionKey(sessionKey);
    }

    public void saveSessionKey(String sessionKey) {
        db.saveSessionKey(sessionKey);
    }

    public String getSessionKey() {
        return db.findSessionKey();
    }


}
