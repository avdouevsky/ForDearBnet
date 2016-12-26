package com.mshvdvskgmail.fordearbnet.models;

import android.database.Cursor;

import com.mshvdvskgmail.fordearbnet.utilities.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
/**
 * Created by mshvd_000 on 12.12.2016.
 */

public class Note implements Serializable {

    private Long id;
    private String serverId;
    private String title;
    private String content;
    private Calendar dateCreated;
    private Calendar dataModified;
    //private long dateCreatedMillis;


    public String getReadableCreatedDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy - kk:mm:ss", Locale.getDefault());
        sdf.setTimeZone(getDateCreated().getTimeZone());
        Date createdDate = getDateCreated().getTime();
        String displayDate = sdf.format(createdDate);
        return displayDate;
    }


    public String getReadableModifiedDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy - kk:mm:ss", Locale.getDefault());
        sdf.setTimeZone(getDataModified().getTimeZone());
        Date modifiedDate = getDataModified().getTime();
        String displayDate = sdf.format(modifiedDate);
        return displayDate;
    }

    public String getReadableDate(){
        String readableCreatedDate = getReadableCreatedDate();
        String readableModifiedDate = getReadableModifiedDate();

        if (!readableCreatedDate.equals(readableModifiedDate)){
            return new String (readableCreatedDate + "\n" + readableModifiedDate);
        } else return readableCreatedDate;

    }

    public String getTwoHundredSignsContent(){

        if(content.length()>200){
            String twoHundredSignsContent = content.substring(0,200);
            return twoHundredSignsContent;
        } else{
            return content;
        }
    }


    /*public long getDateCreatedMillis() {
        return dateCreatedMillis;
    }

    public void setDateCreatedMillis(long dateCreatedMillis) {
        this.dateCreatedMillis = dateCreatedMillis;
    }*/

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Calendar getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Calendar dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Calendar getDataModified() {
        return dataModified;
    }

    public void setDataModified(Calendar dataModified) {
        this.dataModified = dataModified;
    }

    public static Note getNoteFromJsonObject(JSONObject object) throws JSONException {
        Note note = new Note();
        note.setServerId(object.getString("id"));
        note.setContent(object.getString("body"));

        Calendar calendar = GregorianCalendar.getInstance();
        Calendar calendar1 = GregorianCalendar.getInstance();

        calendar.setTimeInMillis(Long.parseLong(object.getString("da")));
        note.setDateCreated(calendar);

        calendar1.setTimeInMillis(Long.parseLong(object.getString("dm")));
        note.setDataModified(calendar1);

        return note;
    }



    /*
    public static Note getNotefromCursor(Cursor cursor){
        Note note = new Note();
        note.setId(cursor.getLong(cursor.getColumnIndex(Constants.COLUMN_ID)));
        note.setTitle(cursor.getString(cursor.getColumnIndex(Constants.COLUMN_TITLE)));
        note.setContent(cursor.getString(cursor.getColumnIndex(Constants.COLUMN_CONTENT)) + "\n" + "date created in calender"
                + cursor.getLong(cursor.getColumnIndex(Constants.COLUMN_CREATED_TIME)) + "\n" + "date modified in calendar"
        + cursor.getLong(cursor.getColumnIndex(Constants.COLUMN_MODIFIED_TIME)));

        //get Calendar instance
        Calendar calendar = GregorianCalendar.getInstance();
        Calendar calendar1 = GregorianCalendar.getInstance();

        //set the calendar time to date created
        calendar.setTimeInMillis(cursor.getLong(cursor.getColumnIndex(Constants.COLUMN_CREATED_TIME)));
        note.setDateCreated(calendar);
        //note.setDateCreatedMillis(cursor.getLong(cursor.getColumnIndex(Constants.COLUMN_CREATED_TIME)));

        //set the calendar time to date modified
        calendar1.setTimeInMillis(cursor.getLong(cursor.getColumnIndex(Constants.COLUMN_MODIFIED_TIME)));
        note.setDataModified(calendar1);
        return note;
    }
     */


}
