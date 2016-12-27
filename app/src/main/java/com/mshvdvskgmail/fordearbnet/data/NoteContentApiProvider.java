package com.mshvdvskgmail.fordearbnet.data;

import android.os.AsyncTask;
import android.util.Log;

import com.mshvdvskgmail.fordearbnet.interfaces.CallBack;
import com.mshvdvskgmail.fordearbnet.interfaces.SessionOpener;
import com.mshvdvskgmail.fordearbnet.models.Note;
import com.mshvdvskgmail.fordearbnet.utilities.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;


/**
 * This class handles all API communication, like getting session key, uploading a note
 * and getting the list of the notes.
 * NoteManager is managing the NoteContentApiProvider instances.
 */

public class NoteContentApiProvider {

    private CallBack listener; // for callback methods in fragments
    private SessionOpener openSessionListener; // for callback method in NoteManager; saving seeion key in db
    private String noteContent;
    private String result;
    private String API_URL = "https://bnet.i-partner.ru/testAPI/";
    //private String id; // нужна если хотим прочесть id созданной заметки для редактирования, удаления или сохранения в локальной бд
    private ArrayList<JSONObject> jsonList = new ArrayList<>();
    private DatabaseHelper db;
    private String sessionKeyFromDb;


    public NoteContentApiProvider(CallBack listener, SessionOpener openSessionListener, DatabaseHelper db) {
        this.listener = listener;
        this.openSessionListener = openSessionListener;
        this.db = db;
    }

    NoteContentApiProvider(String noteContent, DatabaseHelper db, CallBack listener) {
        this.noteContent = noteContent;
        this.db = db;
        this.listener = listener;
    }

    // creates a note by using nested NoteCreator AsyncTask

    public void create() {
        try{
            sessionKeyFromDb = db.findSessionKey();
        } catch (Exception e){}
        if (sessionKeyFromDb==null){
            sessionKeyFromDb = "";
        }
        NoteCreator creatorTask = new NoteCreator();
        creatorTask.execute();
    }


    // gets all notes by using nested AllNotesGetter AsyncTask

    public void getAllNotes(){
        try{
            sessionKeyFromDb = db.findSessionKey();
        } catch (Exception e){}

        if (sessionKeyFromDb==null||sessionKeyFromDb.equals("")){
            SessionKeyGetter sessionTask = new SessionKeyGetter();
            sessionTask.execute();
        }else {
            AllNotesGetter notesTask = new AllNotesGetter();
            notesTask.execute(sessionKeyFromDb);
        }
    }

    // AsyncTask for "add_entry" POST request

    class NoteCreator extends AsyncTask {

        private boolean isConnected = false; // is used for callback method if there is no connection

        @Override
        protected Object doInBackground(Object[] params) {
            try {

                URL url = new URL(API_URL);
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("token", Constants.TOKEN);
                urlConnection.setDoOutput(true);

                String argLine = new String("a=add_entry&session=" + sessionKeyFromDb + "&body=" + noteContent);
                byte[] out = argLine.getBytes();

                OutputStream outputPost = new BufferedOutputStream(urlConnection.getOutputStream());
                outputPost.write(out);

                outputPost.flush();
                outputPost.close();

                isConnected = true;

                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();

                    //JSONObject JSONInfoOnActor = new JSONObject(stringBuilder.toString()); // нужна если хотим прочесть id созданной заметки для редактирования, удаления или сохранения в локальной бд;
                    //id = JSONInfoOnActor.getString("id"); // нужна если хотим прочесть id созданной заметки для редактирования, удаления или сохранения в локальной бд;

                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                result = e.getMessage();
                return null;
            }
        }

        protected void onPostExecute(Object response) {
            if (response == null) {
                response = "THERE WAS AN ERROR";
            }
            Log.i("INFO", (String) response);

            if (!isConnected){
                listener.onFailedConnection(null); // these are the fragment callback methods
            } else {
                listener.onTaskCompleted(null); // these are the fragment callback methods
            }
        }
    }

    // AsyncTask for "get_entries" POST request

    class AllNotesGetter extends AsyncTask <String, String, String> {

        private List<Note> notes = new ArrayList<Note>();
        private boolean isConnected = false; // the same thing

        @Override
        protected String doInBackground(String... params)  {
            String sessionKey = params[0]; // argument is passed for a case where there is no session key yet, and AllNotesGetter is fired by SessionKeyGetter
            try {

                URL url = new URL(API_URL);
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("token", Constants.TOKEN);
                urlConnection.setDoOutput(true);

                String argLine = new String("a=get_entries&session="+sessionKey);
                byte[] out = argLine.getBytes();

                result = "argLine is: " + argLine;

                OutputStream outputPost = new BufferedOutputStream(urlConnection.getOutputStream());
                outputPost.write(out);
                outputPost.flush();
                outputPost.close();
                isConnected = true;

                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();

                    JSONObject APIresponse = new JSONObject(stringBuilder.toString());
                    JSONArray JSONData = APIresponse.getJSONArray("data");
                    String DataFormatterToArray = JSONData.toString().substring(1, JSONData.toString().length() - 1);
                    JSONArray notesArray = new JSONArray(DataFormatterToArray);

                    for (int i = 0; i < notesArray.length(); i++) {
                        jsonList.add(notesArray.getJSONObject(i));
                    }
                    result += "\n"+"and string builder got "+stringBuilder.toString();

                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                result+= "\n"+e.getMessage();
            }


            try{
                if (jsonList.size()>0){
                    for (int i = 0; i < jsonList.size();i++){
                        notes.add(Note.getNoteFromJsonObject(jsonList.get(i)));
                    }
                }
            } catch (Exception e) {}

            return result;

        }


        protected void onPostExecute(String response) {
            if (response == null) {
                response = "THERE WAS AN ERROR";
            }
            Log.i("INFO", response);
            if (isConnected == true){
                listener.onTaskCompleted(notes);
            } else {
                listener.onFailedConnection(result);
            }
        }
    }

    // AsyncTask for "new_session" POST request

    class SessionKeyGetter extends AsyncTask <String, String, String> {

        private String sessionKeyHolder;

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(API_URL);
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("token", Constants.TOKEN);
                urlConnection.setDoOutput(true);

                String argLine = new String("a=new_session");
                byte[] out = argLine.getBytes();

                OutputStream outputPost = new BufferedOutputStream(urlConnection.getOutputStream());
                outputPost.write(out);
                outputPost.flush();
                outputPost.close();

                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line1;
                    while ((line1 = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line1).append("\n");
                    }
                    bufferedReader.close();

                    JSONObject JSONInfoOnActor = new JSONObject(stringBuilder.toString());
                    JSONObject temp = JSONInfoOnActor.getJSONObject("data");
                    sessionKeyHolder = temp.getString("session");
                    return  sessionKeyHolder;
                } finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return e.getMessage();
            }
        }

        protected void onPostExecute(String response) {
            openSessionListener.onSessionKeyGenerated(sessionKeyHolder);
            AllNotesGetter b = new AllNotesGetter();
            b.execute(response);
        }

    }



}




