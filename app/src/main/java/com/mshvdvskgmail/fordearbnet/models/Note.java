package com.mshvdvskgmail.fordearbnet.models;

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

}
