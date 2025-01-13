/*COMP3450: Calvin Thomschke, Jaxon Isnardy, and Meenakshi Rajesh*/
package com.example.mealplanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Favorites extends AppCompatActivity {

    ImageButton navButton;
    private RecyclerView recyclerView;
    private RecipeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favorites);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        navButton = findViewById(R.id.navButton);

        recyclerView = findViewById(R.id.favoriteRecipeRecycler);
        adapter = new RecipeAdapter(new ArrayList<>(), this);

        // Set adapter on recycler view
        recyclerView.setAdapter(adapter);

        // Linear layout vertical
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        // Fetch all recipes
        FetchRecipesTask fetchRecipesTask = new FetchRecipesTask(this, adapter, FetchRecipesTask.FetchOptions.Favourites);
        fetchRecipesTask.execute();

        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Favorites.this, navMenu.class);
                startActivity(intent);
            }
        });
    }
}
