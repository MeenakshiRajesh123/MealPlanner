/*COMP3450: Calvin Thomschke, Jaxon Isnardy, and Meenakshi Rajesh*/
package com.example.mealplanner;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView recyclerView;
    private pickRecipeAdapter adapter;
    private ArrayList<RecipeModal> recipesList;
    private boolean loadingRecipes = false;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RecipeListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecipeListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecipeListFragment newInstance(String param1, String param2) {
        RecipeListFragment fragment = new RecipeListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        // Initialize RecyclerView and adapter
        recyclerView = view.findViewById(R.id.pickRecycler);

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
                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(adapter.mContext);
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
                        pickRecipeAdapter adapter = (pickRecipeAdapter)recyclerView.getAdapter();
                        getActivity().runOnUiThread(new Runnable() {
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

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recipesList = new ArrayList<>();
        adapter = new pickRecipeAdapter(recipesList, getActivity());
        recyclerView.setAdapter(adapter);

        // Fetch recipes and populate the list
        FetchRecipesPickTask fetchRecipesPickTask = new FetchRecipesPickTask(getActivity(), adapter, FetchRecipesPickTask.FetchOptions.Search);
        fetchRecipesPickTask.execute();

        return view;
    }
}