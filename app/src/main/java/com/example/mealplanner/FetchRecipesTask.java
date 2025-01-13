/*COMP3450: Calvin Thomschke, Jaxon Isnardy, and Meenakshi Rajesh*/
package com.example.mealplanner;

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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class FetchRecipesTask extends AsyncTask<Void, Void, ArrayList<RecipeModal>> {

    public enum FetchOptions {
        Search,
        Favourites
    };
    private static final String TAG = "FetchRecipesTask";
    private static final String DATABASE_URL = "http://162.156.183.109/food_controller.php";
    private RecipeAdapter mAdapter;
    private Context mContext;
    private FetchOptions mOptions;
    private int mPage;

    public FetchRecipesTask(Context context, RecipeAdapter adapter, FetchOptions options, int page) {
        mContext = context.getApplicationContext();
        mAdapter = adapter;
        mOptions = options;
        mPage = page;
    }
    public FetchRecipesTask(Context context, RecipeAdapter adapter, FetchOptions options) {
        mContext = context.getApplicationContext();
        mAdapter = adapter;
        mOptions = options;
        mPage = -1;
    }

    @Override
    protected ArrayList<RecipeModal> doInBackground(Void... voids) {
        ArrayList<RecipeModal> recipes = new ArrayList<>();
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        try {
            String finalUrl = DATABASE_URL;
            if (mOptions == FetchOptions.Search)
            {
                finalUrl += "?Action=RecipeSearch";
                finalUrl += "&Page=" + mPage;
            }
            else if (mOptions == FetchOptions.Favourites)
            {
                finalUrl += "?Action=GetFavourites&Token=" + fetchToken ();
            }

            URL url = new URL(finalUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder builder = new StringBuilder();

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

            String recipesJsonStr = builder.toString();
            JSONArray recipesArray = new JSONArray(recipesJsonStr);

            for (int i = 0; i < recipesArray.length(); i++) {
                JSONObject recipeJson = recipesArray.getJSONObject(i);
                String id = recipeJson.getString("id");
                String title = recipeJson.getString("Title");
                String description = recipeJson.getString("Description");
                String image = recipeJson.getString("Image");
                String ingredients = recipeJson.getString("Ingredients");
                String servings = recipeJson.getString("Servings");
                Boolean fav = checkIfFavourite(id);

                // Create RecipeModal object with all fields
                RecipeModal recipe = new RecipeModal(id, image, title, description, ingredients, servings, fav);
                recipes.add(recipe);
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
        return recipes;
    }

    @Override
    protected void onPostExecute(ArrayList<RecipeModal> recipes) {
        if (recipes != null) {
            // Log the JSON data
            Log.d(TAG, "JSON Data: " + recipes.toString());

            // Update the adapter with the fetched recipes
            if (mPage == -1)
                mAdapter.setRecipes(recipes);
            else
                mAdapter.addRecipes(recipes);
            mAdapter.notifyDataSetChanged();

            // Log the number of fetched recipes
            Log.d(TAG, "Number of fetched recipes: " + recipes.size());

            // Display a toast message indicating that recipes have been fetched
        } else {
            Toast.makeText(mContext, "Failed to fetch recipes", Toast.LENGTH_SHORT).show();
        }
    }
    private boolean checkIfFavourite (String id)
    {
        String token = fetchToken ();
        String favUrl = DATABASE_URL + "?Action=CheckFavourite&Recipe=" + id + "&Token=" + token;

        try {
            URL url = new URL(favUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
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


        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private String fetchToken ()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        String token = preferences.getString("sessionToken", "");
        return token;
    }
}