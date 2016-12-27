package com.mshvdvskgmail.fordearbnet.utilities;

/**
 *  A tool for tests.
 */

import com.mshvdvskgmail.fordearbnet.models.Note;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;


public class SampleData {

    public static List<Note> getSampleNotes(){
        List<Note> sampleNotes = new ArrayList<Note>();


        Note note1 = new Note();
        note1.setServerId("wiur");
        note1.setContent("Hey, it's me");

        Calendar calendar = GregorianCalendar.getInstance();
        Calendar calendar1 = GregorianCalendar.getInstance();

        calendar.setTimeInMillis(Long.parseLong("1482160224"));
        note1.setDateCreated(calendar);

        calendar1.setTimeInMillis(Long.parseLong("1482160224"));
        note1.setDataModified(calendar1);

        sampleNotes.add(note1);
        return sampleNotes;

    }

}