/*COMP3450: Calvin Thomschke, Jaxon Isnardy, and Meenakshi Rajesh*/
package com.example.mealplanner;

import android.app.Activity;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.prefs.Preferences;

public class WeekAdapter extends RecyclerView.Adapter<WeekAdapter.viewholder>{

    ArrayList<WeekModal> list;
    //we have created our own datatype that is a RecipeModal. So our arraylist will be of type RecipeModal
    Context mContext;
    //Generate Constructor as we need in main java file

    public WeekAdapter(ArrayList<WeekModal> list, Context context) {
        this.list = list;
        mContext = context;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate the view
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_week_card,parent, false);
        // Layout:layout that we want to display
        // parent – The ViewGroup into which the new View will be added after it is bound to an adapter position.
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        // Bind the data
        //set the values of imageview and textView
        WeekModal modal=list.get(position);
        //holder.rImage.setImageResource(modal.getPic());
        holder.txtDay.setText(modal.getDay());
        holder.bTitle.setText(modal.getbName());
        holder.lTitle.setText(modal.getlName());
        holder.dTitle.setText(modal.getdName());
        if (!modal.getbPic().equals("")) {
            Picasso.get().load(modal.getbPic()).into(holder.bImage);
            holder.bButton.setImageResource (R.drawable.icon_remove);
        }
        else {
            Picasso.get().load(R.drawable.no_image).into(holder.bImage);
            holder.bButton.setImageResource (R.drawable.icon_add);
        }
        holder.bButton.setColorFilter(Color.rgb(200,200,200));

        if (!modal.getlPic().equals("")) {
            Picasso.get().load(modal.getlPic()).into(holder.lImage);
            holder.lButton.setImageResource (R.drawable.icon_remove);
        }
        else {
            Picasso.get().load(R.drawable.no_image).into(holder.lImage);
            holder.lButton.setImageResource (R.drawable.icon_add);
        }
        holder.lButton.setColorFilter(Color.rgb(200,200,200));

        if (!modal.getdPic().equals("")) {
            Picasso.get().load(modal.getdPic()).into(holder.dImage);
             holder.dButton.setImageResource (R.drawable.icon_remove);
        }
        else {
            Picasso.get().load(R.drawable.no_image).into(holder.dImage);
            holder.dButton.setImageResource (R.drawable.icon_add);
        }
        holder.dButton.setColorFilter(Color.rgb(200,200,200));

        holder.bTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewRecipe(modal.getDay().toLowerCase(), "breakfast");
            }
        });
        holder.lTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewRecipe(modal.getDay().toLowerCase(), "lunch");
            }
        });
        holder.dTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewRecipe(modal.getDay().toLowerCase(), "dinner");
            }
        });

        holder.bButton.setOnClickListener(view -> {
            // If there is no recipe assigned
            if (modal.getbId().equals(""))
            {
                selectRecipe(modal.getDay().toLowerCase(), "breakfast");
            }
            else
            {
                removeRecipe(modal.getDay().toLowerCase(), "breakfast");
            }
        });
        holder.lButton.setOnClickListener(view -> {
            // If there is no recipe assigned
            if (modal.getlId().equals(""))
            {
                selectRecipe(modal.getDay().toLowerCase(), "lunch");
            }
            else
            {
                removeRecipe(modal.getDay().toLowerCase(), "lunch");
            }
        });
        holder.dButton.setOnClickListener(view -> {
            // If there is no recipe assigned
            if (modal.getdId().equals(""))
            {
                selectRecipe(modal.getDay().toLowerCase(), "dinner");
            }
            else
            {
                removeRecipe(modal.getDay().toLowerCase(), "dinner");
            }
        });
    }
    private void selectRecipe (String day, String time)
    {
        Intent intent = new Intent(mContext, MealSelectionActivity.class);
        intent.putExtra("selected_day", day);
        intent.putExtra("meal_type", time);
        mContext.startActivity(intent);
    }
    private void removeRecipe (String day, String time)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(day + "_" + time);
        editor.apply();
        ((Activity) mContext).recreate();
    }
    private void viewRecipe (String day, String time)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        String id = sharedPreferences.getString(day + "_" + time, "");

        if (id.equals(""))
            selectRecipe(day, time);
        else {
            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    Intent intent = new Intent(mContext, RecipeInfo.class);
                    InputStream inputStream = QueryHelper.EstablishConnection("?Action=GetRecipe&Recipe=" + id);
                    JSONObject json = QueryHelper.BuildJsonObject(inputStream);

                    try {
                        intent.putExtra("image", json.getString("Image"));
                        intent.putExtra("title", json.getString("Title"));
                        intent.putExtra("description", json.getString("Description"));
                        intent.putExtra("ingredients", json.getString("Ingredients"));
                        intent.putExtra("servings", json.getString("Servings"));
                        mContext.startActivity(intent);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }


                }
            });
            thread.start();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{
        //declare variables
        TextView txtDay;
        TextView bTitle;
        TextView lTitle;
        TextView dTitle;
        ImageView bImage;
        ImageView lImage;
        ImageView dImage;
        ImageButton bButton;
        ImageButton lButton;
        ImageButton dButton;
        //Create Constructor
        public viewholder(@NonNull View itemView) {
            super(itemView);
            //get views in the item of the viewholder
            txtDay=itemView.findViewById(R.id.day);
            bTitle=itemView.findViewById(R.id.txtBreakfast);
            lTitle=itemView.findViewById(R.id.txtLunch);
            dTitle=itemView.findViewById(R.id.txtDinner);
            bImage=itemView.findViewById(R.id.imgBreakfast);
            lImage=itemView.findViewById(R.id.imgLunch);
            dImage=itemView.findViewById(R.id.imgDinner);
            bButton=itemView.findViewById(R.id.rmvBreakfast);
            lButton=itemView.findViewById(R.id.rmvLunch);
            dButton=itemView.findViewById(R.id.rmvDinner);
        }
    }
}