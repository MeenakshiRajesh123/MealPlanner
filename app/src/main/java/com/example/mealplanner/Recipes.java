/*COMP3450: Calvin Thomschke, Jaxon Isnardy, and Meenakshi Rajesh*/
package com.example.mealplanner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

public class Recipes extends AppCompatActivity {

    ImageButton navButton;
    boolean loadingRecipes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipes);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        navButton = findViewById(R.id.navButton);
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Recipes.this, navMenu.class);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recipeRecycler);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                int totalItemCount = layoutManager.getItemCount();
                int pageTotal = Math.round(totalItemCount / 10) + 1;

                if (!loadingRecipes && lastVisibleItemPosition >= totalItemCount - 1) {
                    loadingRecipes = true;
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (layoutManager.findLastVisibleItemPosition() >= layoutManager.getItemCount())
                                return;

                            String parameters = "?Action=RecipeSearch&Page=" + pageTotal;
                            InputStream inputStream = QueryHelper.EstablishConnection(parameters);
                            JSONArray json = QueryHelper.BuildJsonArray(inputStream);

                            ArrayList<RecipeModal> recipes = new ArrayList<>();
                            try {
                                for (int i = 0; i < json.length(); i++) {
                                    JSONObject recipeJson = json.getJSONObject(i);
                                    String id = recipeJson.getString("id");
                                    String title = recipeJson.getString("Title");
                                    String description = recipeJson.getString("Description");
                                    String image = recipeJson.getString("Image");
                                    String ingredients = recipeJson.getString("Ingredients");
                                    String servings = recipeJson.getString("Servings");
                                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                    String token = preferences.getString("sessionToken", "");
                                    InputStream favStream = QueryHelper.EstablishConnection("?Action=CheckFavourite&Recipe=" + id + "&Token=" + token);
                                    boolean fav = QueryHelper.ReturnBoolean(favStream);

                                    // Create RecipeModal object with all fields
                                    RecipeModal recipe = new RecipeModal(id, image, title, description, ingredients, servings, fav);
                                    recipes.add(recipe);
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            RecipeAdapter adapter = (RecipeAdapter)recyclerView.getAdapter();
                            Recipes.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.addRecipes(recipes);
                                }
                            });

                            loadingRecipes = false;
                        }
                    });
                    thread.start();
                }
            }
        });
        ArrayList<RecipeModal> list = new ArrayList<>();
        // Create instance of adapter
        RecipeAdapter adapter=new RecipeAdapter(list, this);

        FetchRecipesTask fetchRecipesTask = new FetchRecipesTask(this, adapter, FetchRecipesTask.FetchOptions.Search, 1);
        fetchRecipesTask.execute();


        //set adapter on recycler view
        recyclerView.setAdapter(adapter);

        //Linear layout vertical
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
    }
}