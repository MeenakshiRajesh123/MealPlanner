/*COMP3450: Calvin Thomschke, Jaxon Isnardy, and Meenakshi Rajesh*/
package com.example.mealplanner;

public class RecipeModal {
    private String id;
    private String image;
    private String title;
    private String description;
    private String ingredients;
    private String servings;
    private boolean fav;

    // Constructor
    public RecipeModal(String id, String image, String title, String description, String ingredients, String servings, boolean fav) {
        this.id = id;
        this.image = image;
        this.title = title;
        this.description = description;
        this.ingredients = ingredients;
        this.servings = servings;
        this.fav = fav;
    }

    // Getters and setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getServings() {
        return servings;
    }

    public void setServings(String servings) {
        this.servings = servings;
    }

    public boolean isFav() { return fav; }
    public void toggleFav() { fav = !fav; }
}

