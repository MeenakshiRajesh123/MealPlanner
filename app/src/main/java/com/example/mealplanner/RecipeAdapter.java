/*COMP3450: Calvin Thomschke, Jaxon Isnardy, and Meenakshi Rajesh*/
package com.example.mealplanner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.viewholder> {

    private ArrayList<RecipeModal> originalList;
    Context mContext;

    public RecipeAdapter(ArrayList<RecipeModal> list, Context context) {
        originalList = new ArrayList<>(list);
        mContext = context;
    }

    public void setRecipes(ArrayList<RecipeModal> recipes) {
        originalList.clear();
        originalList.addAll(recipes);
        notifyDataSetChanged();
    }

    public void addRecipes (ArrayList<RecipeModal> recipes)
    {
        originalList.addAll (recipes);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_recipe_card, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        RecipeModal modal = originalList.get(position);

        Picasso.get().load(modal.getImage()).into(holder.rImage);
        holder.rTitle.setText(modal.getTitle());
        //holder.rDesc.setText(modal.getDescription());

        if (modal.isFav())
        {
            holder.favoriteButton.setColorFilter(Color.rgb(255,235,59));
        }
        else
        {
            holder.favoriteButton.setColorFilter (Color.rgb (120, 120, 120));
        }

        holder.favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread thread = new Thread(new Runnable () {
                   @Override
                   public void run()
                   {
                       if (toggleFavourite(modal.getId()))
                       {
                           modal.toggleFav();
                           if (modal.isFav())
                           {
                               holder.favoriteButton.setColorFilter(Color.rgb(255,235,59));
                           }
                           else
                           {
                               holder.favoriteButton.setColorFilter (Color.rgb (120, 120, 120));
                           }
                       }
                   }
                });
                thread.start();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, RecipeInfo.class);
                intent.putExtra("image", modal.getImage());
                intent.putExtra("title", modal.getTitle());
                intent.putExtra("description", modal.getDescription());
                intent.putExtra("ingredients", modal.getIngredients());
                intent.putExtra("servings", modal.getServings());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return originalList.size();
    }

    private boolean toggleFavourite (String id)
    {
        String token = fetchToken ();
        String favUrl = "http://162.156.183.109/food_controller.php?Action=ToggleFavourite&Recipe=" + id + "&Token=" + token;

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

    public class viewholder extends RecyclerView.ViewHolder {
        ImageView rImage;
        TextView rTitle;
        TextView rDesc;
        ImageButton favoriteButton;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            rImage = itemView.findViewById(R.id.image);
            rTitle = itemView.findViewById(R.id.title);
            //rDesc = itemView.findViewById(R.id.details);
            favoriteButton = itemView.findViewById(R.id.favoriteRecipeButton);
        }
    }
}