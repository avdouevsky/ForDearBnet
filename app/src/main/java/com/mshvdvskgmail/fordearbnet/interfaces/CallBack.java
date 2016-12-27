package com.mshvdvskgmail.fordearbnet.interfaces;

import com.mshvdvskgmail.fordearbnet.models.Note;

import java.util.ArrayList;
import java.util.List;

/**
 * interface used by NoteContentApiProvider to start fragment methods.
 */

public interface CallBack {
    void onTaskCompleted(List<Note> values);
    void onFailedConnection(String result);
}
