/*COMP3450: Calvin Thomschke, Jaxon Isnardy, and Meenakshi Rajesh*/
package com.example.mealplanner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.prefs.Preferences;

public class WeekPlan extends AppCompatActivity{

    ImageButton navButton;
    RecyclerView recyclerView;
    private MealViewModel mealViewModel;

    public enum DayOfWeek {
        Monday,
        Tuesday,
        Wednesday,
        Thursday,
        Friday,
        Saturday,
        Sunday
    }
    public enum MealTimes {
        Breakfast,
        Lunch,
        Dinner
    }

    @Override
    protected void onResume() {
        super.onResume();
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_week_plan);
        mealViewModel = new ViewModelProvider(this).get(MealViewModel.class);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        navButton = (ImageButton)findViewById(R.id.navButtonWeek);

        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeekPlan.this, navMenu.class);
                startActivity(intent);
            }
        });

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                recyclerView = findViewById(R.id.weekRecycler);
                ArrayList<WeekModal> list = new ArrayList<>();

                for (DayOfWeek day : DayOfWeek.values()) {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(WeekPlan.this);
                    WeekModal modal = new WeekModal();
                    modal.setDay (day.name());
                    for (MealTimes time : MealTimes.values())
                    {
                        String id = sharedPreferences.getString(day.toString().toLowerCase() + "_" + time.toString().toLowerCase(), "").trim();

                        switch (time)
                        {
                            case Breakfast:
                                modal.setbId(id);
                                break;
                            case Lunch:
                                modal.setlId(id);
                                break;
                            case Dinner:
                                modal.setdId(id);
                                break;
                        }
                    }
                    list.add(modal);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Create instance of adapter
                        WeekAdapter adapter = new WeekAdapter(list, WeekPlan.this);
                        //set adapter on recycler view
                        recyclerView.setAdapter(adapter);

                        //Linear layout vertical
                        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(linearLayoutManager);

                    }
                });

            }
        });
        thread.start();

        mealViewModel.getSelectedMealTitle().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String selectedMealTitle) {
                // Update UI with selected meal title
            }
        });

        mealViewModel.getSelectedMealImage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String selectedMealImage) {
                //Picasso.get().load(selectedMealImage).into(selectedMealImageView);
            }
        });
    }
}