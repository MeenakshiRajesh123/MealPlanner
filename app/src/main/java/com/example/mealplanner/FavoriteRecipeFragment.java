/*COMP3450: Calvin Thomschke, Jaxon Isnardy, and Meenakshi Rajesh*/
package com.example.mealplanner;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoriteRecipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoriteRecipeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView recyclerView;
    private pickRecipeAdapter adapter;
    private ArrayList<RecipeModal> recipesList;
    private MealViewModel mealViewModel;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FavoriteRecipeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavoriteRecipeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavoriteRecipeFragment newInstance(String param1, String param2) {
        FavoriteRecipeFragment fragment = new FavoriteRecipeFragment();
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
        mealViewModel = new ViewModelProvider(requireActivity()).get(MealViewModel.class);
    }

    private void setSelectedMeal(String selectedDay, String mealType, String mealTitle, String mealImage) {
        mealViewModel.setSelectedDay(selectedDay);
        mealViewModel.setSelectedMealType(mealType);
        mealViewModel.setSelectedMealTitle(mealTitle);
        mealViewModel.setSelectedMealImage(mealImage);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        // Initialize RecyclerView and adapter
        recyclerView = view.findViewById(R.id.pickRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recipesList = new ArrayList<>();
        adapter = new pickRecipeAdapter(recipesList, getActivity());
        recyclerView.setAdapter(adapter);

        // Fetch recipes and populate the list
        FetchRecipesPickTask fetchRecipesPickTask = new FetchRecipesPickTask(getActivity(), adapter, FetchRecipesPickTask.FetchOptions.Favourites);
        fetchRecipesPickTask.execute();

        return view;
    }
}