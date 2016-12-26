package com.mshvdvskgmail.fordearbnet.interfaces;

import com.mshvdvskgmail.fordearbnet.models.Note;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mshvd_000 on 19.12.2016.
 */

public interface CallBack {
    void onTaskCompleted(List<Note> values);
    void onFailedConnection(String result);
}
