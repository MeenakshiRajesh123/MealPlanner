/*COMP3450: Calvin Thomschke, Jaxon Isnardy, and Meenakshi Rajesh*/
package com.example.mealplanner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

public class navMenu extends AppCompatActivity {

    ImageButton returnButton;
    Button weekplanbutton;
    Button favoritebutton;
    Button recipebutton;
    Button grocerybutton;

    Button loginButton;
    Button signupButton;
    TextView userTextview;
    Button signoutButton;
    ImageView signoutIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_nav_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        returnButton = (ImageButton)findViewById(R.id.returnButton);
        weekplanbutton = (Button)findViewById(R.id.weekplanbutton);
        favoritebutton = (Button)findViewById(R.id.favoritebutton);
        recipebutton = (Button)findViewById(R.id.recipebutton);
        grocerybutton = (Button)findViewById(R.id.grocerybutton);

        loginButton = (Button)findViewById(R.id.login);
        signupButton = (Button)findViewById(R.id.SignUp);
        userTextview = (TextView)findViewById(R.id.editTextUsername);
        signoutButton = (Button)findViewById(R.id.signoutButton);
        signoutIcon = (ImageView)findViewById(R.id.signoutIcon);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String token = preferences.getString("sessionToken", "");
        if (!token.equals(""))
        {
            userTextview.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.INVISIBLE);
            signupButton.setVisibility(View.INVISIBLE);
            signoutIcon.setVisibility(View.VISIBLE);
            signoutButton.setVisibility(View.VISIBLE);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    InputStream iStream = QueryHelper.EstablishConnection("?Action=GetUser&Token=" + token);
                    String username = QueryHelper.BuildString(iStream);
                    String formattedUsername = username.replaceAll("\"", "");
                    userTextview.setText(formattedUsername);

                }
            });
            thread.start();
        }
        else
        {
            userTextview.setVisibility(View.INVISIBLE);
            loginButton.setVisibility(View.VISIBLE);
            signupButton.setVisibility(View.VISIBLE);
            signoutIcon.setVisibility(View.INVISIBLE);
            signoutButton.setVisibility(View.INVISIBLE);
        }

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        weekplanbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(navMenu.this, WeekPlan.class);
                startActivity(intent);
                finish();
            }
        });

        favoritebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(navMenu.this, Favorites.class);
                startActivity(intent);
                finish();
            }
        });

        recipebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(navMenu.this, Recipes.class);
                startActivity(intent);
                finish();
            }
        });

        grocerybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(navMenu.this, GroceryList.class);
                startActivity(intent);
                finish();
            }
        });

        //Log in and sign up
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(navMenu.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(navMenu.this, SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });

        signoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.remove("sessionToken");
                editor.apply();

                Intent intent = new Intent(navMenu.this, navMenu.class);
                startActivity(intent);
                finish();
            }
        });

    }
}