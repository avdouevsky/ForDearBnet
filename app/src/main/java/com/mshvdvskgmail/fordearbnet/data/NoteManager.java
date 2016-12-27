package com.mshvdvskgmail.fordearbnet.data;
import android.content.Context;
import com.mshvdvskgmail.fordearbnet.interfaces.CallBack;
import com.mshvdvskgmail.fordearbnet.interfaces.SessionOpener;
import com.mshvdvskgmail.fordearbnet.models.Note;

/**
 * Singleton, that operated with API and DB communicating classes.
 */

public class  NoteManager implements SessionOpener {

    private Context mContext;
    private DatabaseHelper db;

    private static NoteManager sNoteManagerInstance = null;


    public static NoteManager newInstance(Context context){
        if (sNoteManagerInstance == null){
            sNoteManagerInstance = new NoteManager(context.getApplicationContext());
        }
        return sNoteManagerInstance;
    }

    private NoteManager(Context context){
        this.mContext = context.getApplicationContext();
        db = new DatabaseHelper(mContext);
    }

    public void create(Note note, CallBack listener) {
        NoteContentApiProvider a = new NoteContentApiProvider(note.getContent(), db, listener);
        a.create();
    }


    public void getAllNotes(CallBack listener) {
        NoteContentApiProvider a = new NoteContentApiProvider(listener, this, db);
        a.getAllNotes();
    }


    @Override
    public void onSessionKeyGenerated (String sessionKey) {
        this.saveSessionKey(sessionKey);
    }

    public void saveSessionKey(String sessionKey) {
        db.saveSessionKey(sessionKey);
    }


}
