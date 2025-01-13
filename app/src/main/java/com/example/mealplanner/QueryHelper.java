/*COMP3450: Calvin Thomschke, Jaxon Isnardy, and Meenakshi Rajesh*/
package com.example.mealplanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class QueryHelper
{
    private static final String DATABASE_URL = "http://162.156.183.109/food_controller.php";

    public static InputStream EstablishConnection (String parameters)
    {
        HttpURLConnection urlConnection = null;
        try {
            String finalUrl = DATABASE_URL + parameters;

            URL url = new URL(finalUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();

            return inputStream;

        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static InputStream EstablishConnection(Map<String, String> parameters) {
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(DATABASE_URL);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");

            // Set content type
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // Construct the encoded parameters string
            StringBuilder encodedParams = new StringBuilder();
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                if (encodedParams.length() > 0) {
                    encodedParams.append("&");
                }
                encodedParams.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                encodedParams.append("=");
                encodedParams.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }

            // Set content length
            urlConnection.setRequestProperty("Content-Length", String.valueOf(encodedParams.length()));

            // Enable output for sending data
            urlConnection.setDoOutput(true);

            // Write parameters to the output stream
            try (OutputStream outputStream = urlConnection.getOutputStream()) {
                outputStream.write(encodedParams.toString().getBytes("UTF-8"));
            }

            InputStream inputStream = urlConnection.getInputStream();

            return inputStream;
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }
    public static JSONArray BuildJsonArray(InputStream inputStream)
    {
        try
        {
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = null;

            if (inputStream == null) {
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }

            if (builder.length() == 0) {
                return null;
            }

            String jsonStr = builder.toString();
            JSONArray jsonArray = new JSONArray(jsonStr);
            return jsonArray;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static JSONObject BuildJsonObject (InputStream inputStream)
    {
        try
        {
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = null;

            if (inputStream == null) {
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }

            if (builder.length() == 0) {
                return null;
            }

            String jsonStr = builder.toString();
            JSONArray jsonArray = new JSONArray(jsonStr);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            return jsonObject;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static String BuildString (InputStream inputStream)
    {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = null;

        if (inputStream == null) {
            return "";
        }

        reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        while (true) {
            try {
                if (!((line = reader.readLine()) != null)) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            builder.append(line).append("\n");
        }

        if (builder.length() == 0) {
            return "";
        }

        String responseToken = builder.toString();
        return responseToken;
    }
    public static boolean ReturnBoolean(InputStream inputStream)
    {
        try
        {
            StringBuilder builder = new StringBuilder();

            if (inputStream == null) {
                return false;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }

            if (builder.length() == 0) {
                return false;
            }

            String responseString = builder.toString().trim();
            return Boolean.parseBoolean(responseString);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
