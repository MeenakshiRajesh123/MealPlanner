/*COMP3450: Calvin Thomschke, Jaxon Isnardy, and Meenakshi Rajesh*/
package com.example.mealplanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RecipeInfo extends AppCompatActivity {

    ImageButton returnButton;
    ImageView recipeImage;
    TextView recipeTitle, recipeDescription, recipeIngredients, recipePrepTime, recipeCookTime, recipeServings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe_info);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        returnButton = findViewById(R.id.returnButton);
        recipeImage = findViewById(R.id.recipeImage);
        recipeTitle = findViewById(R.id.recipeTitle);
        recipeDescription = findViewById(R.id.recipeDescription);
        recipeIngredients = findViewById(R.id.recipeIngredients);
        recipeServings = findViewById(R.id.recipeServings);

        // Retrieve recipe details passed from Recipes activity
        Intent intent = getIntent();
        String image = intent.getStringExtra("image");
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");
        String ingredients = intent.getStringExtra("ingredients");
        String servings = intent.getStringExtra("servings");

        // Set recipe details to views
        Picasso.get().load(image).into(recipeImage);
        recipeTitle.setText(title);
        recipeDescription.setText(description);
        recipeIngredients.setText(ingredients);
        recipeServings.setText(servings);

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
