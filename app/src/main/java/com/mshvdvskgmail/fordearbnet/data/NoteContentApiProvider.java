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
 * Created by mshvd_000 on 19.12.2016.
 */

public class NoteContentApiProvider {

    private CallBack listener;
    private SessionOpener openSessionListener;
    private boolean isConnected = false;
    private String noteContent;
    private String result;
    private String API_URL = "https://bnet.i-partner.ru/testAPI/";
    private String id;
    private ArrayList<JSONObject> jsonList = new ArrayList<>();
    private String sessionKey = "qLxl9bGXELkCkfpHh7";
    private String managerSessionKey;
    private DatabaseHelper db;
    private String sessionKeyFromDb;




    public NoteContentApiProvider(CallBack listener, String managerSessionKey, SessionOpener openSessionListener, DatabaseHelper db) {
        this.listener = listener;
        this.managerSessionKey = managerSessionKey;
        this.openSessionListener = openSessionListener;
        this.db = db;
    }

    NoteContentApiProvider(String noteContent, DatabaseHelper db) {
        this.noteContent = noteContent;
        this.db = db;
    }

    public String create() {
        NoteCreator a = new NoteCreator();
        a.execute();
        return id;
    }

    public void getAllNotes(){
        try{
            sessionKeyFromDb = db.findSessionKey();
        } catch (Exception e){}

        if (sessionKeyFromDb==null||sessionKey.equals("")){
            SessionKeyGetter b = new SessionKeyGetter();
            b.execute();
        }else {
            AllNotesGetter a = new AllNotesGetter();
            a.execute(sessionKeyFromDb);
        }
    }

    /*
    public void startSession(){
        SessionKeyGetter a = new SessionKeyGetter();
        a.execute();
    }
    */

    class NoteCreator extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {

            try{
                sessionKeyFromDb = db.findSessionKey();
            } catch (Exception e){}


            try {

                URL url = new URL(API_URL);
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("token", "IBZPqL9-Gl-wcS56BM");
                urlConnection.setDoOutput(true);

                String argLine = new String("a=add_entry&session=" + sessionKeyFromDb + "&body=" + noteContent);
                byte[] out = argLine.getBytes();

                OutputStream outputPost = new BufferedOutputStream(urlConnection.getOutputStream());
                outputPost.write(out);

                outputPost.flush();
                outputPost.close();

                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    JSONObject JSONInfoOnActor = new JSONObject(stringBuilder.toString());
                    id = JSONInfoOnActor.getString("id");
                    //hook = stringBuilder.toString();
                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                result = e.getMessage();
                //hook += "\n" + result;

                return null;
            }
        }

        protected void onPostExecute(Object response) {
            if (response == null) {
                response = "THERE WAS AN ERROR";
            }
            Log.i("INFO", (String) response);

        }
    }

    class AllNotesGetter extends AsyncTask <String, String, String> {

        List<Note> notes = new ArrayList<Note>();


        @Override
        protected String doInBackground(String... params)  {
            String s = params[0];


            try {

                URL url = new URL(API_URL);
                HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("token", Constants.TOKEN);
                urlConnection.setDoOutput(true);

                String argLine = new String("a=get_entries&session="+s);
                byte[] out = argLine.getBytes();

                result = "argLine is: "+argLine;

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

                    //result = stringBuilder.toString();

                    JSONObject JSONInfoOnActor = new JSONObject(stringBuilder.toString());
                    JSONArray array0 = JSONInfoOnActor.getJSONArray("data");

                    //result = array0.toString();

                    String array = array0.toString().substring(1, array0.toString().length() - 1);

                    //result = array;

                    JSONArray array1 = new JSONArray(array);

                    for (int i = 0; i < array1.length(); i++) {
                        jsonList.add(array1.getJSONObject(i));
                    }

                    result += "\n"+"and string builder got "+stringBuilder.toString();

                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                result+= "\n"+e.getMessage();
                //return null;
            }



            try{
                if (jsonList.size()>0){
                    for (int i = 0; i < jsonList.size();i++){
                        notes.add(Note.getNoteFromJsonObject(jsonList.get(i)));
                    }
                }
            } catch (Exception e) {

            }

            return result;


        }


        protected void onPostExecute(String response) {
            if (response == null) {
                response = "THERE WAS AN ERROR";
            }
            //progressBar.setVisibility(View.GONE);
            Log.i("INFO", response);
            if (isConnected == true){
                listener.onTaskCompleted(notes);
            } else {
                listener.onFailedConnection(result);
            }
            //listener.onFailedConnection(response);
        }
    }
    class SessionKeyGetter extends AsyncTask <String, String, String> {

        String sessionKeyHolder;
        @Override
        protected String doInBackground(String... params) {
            try {

                URL url1 = new URL(API_URL);
                HttpsURLConnection urlConnection1 = (HttpsURLConnection) url1.openConnection();
                urlConnection1.setRequestMethod("POST");
                urlConnection1.setRequestProperty("token","IBZPqL9-Gl-wcS56BM");
                urlConnection1.setDoOutput(true);

                String argLine1 = new String("a=new_session");
                byte[] out1 = argLine1.getBytes();

                OutputStream outputPost1 = new BufferedOutputStream(urlConnection1.getOutputStream());
                outputPost1.write(out1);
                outputPost1.flush();
                outputPost1.close();

                try {
                    BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(urlConnection1.getInputStream()));
                    StringBuilder stringBuilder1 = new StringBuilder();
                    String line1;
                    while ((line1 = bufferedReader1.readLine()) != null) {
                        stringBuilder1.append(line1).append("\n");
                    }
                    bufferedReader1.close();

                    JSONObject JSONInfoOnActor1 = new JSONObject(stringBuilder1.toString());
                    JSONObject temp1 = JSONInfoOnActor1.getJSONObject("data");
                    sessionKeyHolder = temp1.getString("session");
                    sessionKey = temp1.getString("session");
                    return  sessionKeyHolder;
                } finally{
                    urlConnection1.disconnect();
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




