/*COMP3450: Calvin Thomschke, Jaxon Isnardy, and Meenakshi Rajesh*/
package com.example.mealplanner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MealPagerAdapter extends FragmentStateAdapter {

    private static final int NUM_PAGES = 2; // Two tabs

    public MealPagerAdapter(FragmentActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new RecipeListFragment();
        } else {
            return new FavoriteRecipeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }
}

