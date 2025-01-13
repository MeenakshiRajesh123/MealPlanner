/*COMP3450: Calvin Thomschke, Jaxon Isnardy, and Meenakshi Rajesh*/
package com.example.mealplanner;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.prefs.Preferences;

public class LogInTask extends AsyncTask<String, Void, Boolean> {

    private static final String TAG = "LogInTask";
    private static final String DATABASE_URL = "http://162.156.183.109/food_controller.php";
    private String mUsername;
    private String mPassword;
    private Context mContext;

    public LogInTask(Context context) {
        mContext = context.getApplicationContext();
    }

    @Override
    protected Boolean doInBackground(String... credentials) {
        if (credentials.length != 2) {
            return false;
        }

        String username = credentials[0];
        String password = credentials[1];

        InputStream inputStream = QueryHelper.EstablishConnection("?Action=LoginUser&User=" + username + "&Password=" + password);
        String responseToken = QueryHelper.BuildString(inputStream);

        // Check if the JSON response contains a key indicating success
        if (responseToken.isEmpty()) {
            return false;
        } else {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("sessionToken", responseToken);
            editor.apply();
            return true;
        }
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (success) {
            ((Activity)mContext).finish();
            // Save username and password locally using SharedPreferences
            SharedPreferences sharedPreferences = mContext.getSharedPreferences("Login", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username", mUsername);
            editor.putString("password", mPassword);
            editor.apply();

            Toast.makeText(mContext, "Login successful", Toast.LENGTH_SHORT).show();
            // Navigate to the next activity or perform necessary actions
        } else {
            Toast.makeText(mContext, "Login failed", Toast.LENGTH_SHORT).show();
        }
    }
}