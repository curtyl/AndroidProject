package com.example.louis.androidproject.tools;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;

import com.example.louis.androidproject.R;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * AqicnAsyncTask
 *
 * Async task usefull to get data from URL and populate textView
 */
class AqicnAsyncTask extends AsyncTask<String, Integer, String> {

    private final Activity act;

    public AqicnAsyncTask(Activity act) {
        this.act = act;
    }

    protected String doInBackground(String... urlBase) {
        String strFileContents = null;
        try {
            URL url = new URL(urlBase[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            byte[] contents = new byte[1024];
            int bytesRead;

            while( (bytesRead = in.read(contents)) != -1){
                strFileContents = new String(contents, 0, bytesRead);
            }
            in.close();
            urlConnection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return strFileContents;
    }

    protected void onProgressUpdate(Integer... progress) {
    }

    protected void onPostExecute(String result) {
        TextView myTextView = (TextView) act.findViewById(R.id.info);
        myTextView.setText(result);
    }
}