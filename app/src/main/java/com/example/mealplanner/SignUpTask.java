/*COMP3450: Calvin Thomschke, Jaxon Isnardy, and Meenakshi Rajesh*/
package com.example.mealplanner;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SignUpTask extends AsyncTask<String, Void, Boolean> {

    private static final String TAG = "SignUpTask";
    private static final String DATABASE_URL = "http://162.156.183.109/food_controller.php?Action=CreateUser";
    private Context mContext;

    public SignUpTask(Context context) {
        mContext = context.getApplicationContext();
    }

    @Override
    protected Boolean doInBackground(String... credentials) {
        if (credentials.length != 2) {
            return false;
        }

        String username = credentials[0];
        String password = credentials[1];

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        try {
            // Construct the URL with parameters
            URL url = new URL(DATABASE_URL + "?Action=CreateUser&User=" + username + "&Password=" + password);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder builder = new StringBuilder();

            if (inputStream == null) {
                return false;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }

            if (builder.length() == 0) {
                return false;
            }

            String responseJsonStr = builder.toString();
            JSONObject responseJson = new JSONObject(responseJsonStr);

            // Check if the JSON response contains a key indicating success
            if (responseJson.has("Returns")) {
                return responseJson.getBoolean("Returns");
            } else {
                return false;
            }

        } catch (IOException | JSONException e) {
            Log.e(TAG, "Error ", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(TAG, "Error closing stream", e);
                }
            }
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if (success) {
            Toast.makeText(mContext, "Sign up successful", Toast.LENGTH_SHORT).show();
            // Handle successful sign-up, e.g., navigate to login activity
        } else {
            Toast.makeText(mContext, "Sign up failed", Toast.LENGTH_SHORT).show();
        }
    }
}
