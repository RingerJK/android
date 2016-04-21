package ringerjk.com.themoviedb.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import ringerjk.com.themoviedb.MyConst;

public class RequestListFilmActService extends IntentService {
    public RequestListFilmActService() {
        super("RequestListFilmActService");
        Log.i(MyConst.MY_LOG, "constructor RequestListFilmActService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(MyConst.MY_LOG, "onHandleIntent in RequestListFilmActService");
        String requestString = intent.getStringExtra(MyConst.EXTRA_REQUEST_URI_PATH);
        String intentFilter = intent.getStringExtra(MyConst.INTENT_FILTER);
        Log.i(MyConst.MY_LOG, "requestString = " + requestString);
        URL url = null;
        try {
            url = new URL(MyConst.urlDefault
                    + intent.getStringExtra(MyConst.EXTRA_REQUEST_URI_PATH)
                    + "?api_key=" + MyConst.apiKey);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection urlConnection = null;
        String responseJson = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Accept", "application/json");
//            urlConnection.setRequestProperty("page", "1");
            urlConnection.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder buf = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                buf.append(line + "\n");
            }
            responseJson = buf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(intentFilter);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastIntent.putExtra(MyConst.EXTRA_RESPONSE_JSON, responseJson);
        sendBroadcast(broadcastIntent);
    }
}
