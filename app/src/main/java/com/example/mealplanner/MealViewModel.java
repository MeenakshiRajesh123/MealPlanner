/*COMP3450: Calvin Thomschke, Jaxon Isnardy, and Meenakshi Rajesh*/
package com.example.mealplanner;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MealViewModel extends ViewModel {
    private MutableLiveData<String> selectedDay = new MutableLiveData<>();
    private MutableLiveData<String> selectedMealType = new MutableLiveData<>();
    private MutableLiveData<String> selectedMealTitle = new MutableLiveData<>();
    private MutableLiveData<String> selectedMealImage = new MutableLiveData<>();

    // Methods to set and get selected meal data
    public void setSelectedDay(String day) {
        selectedDay.setValue(day);
    }

    public LiveData<String> getSelectedDay() {
        return selectedDay;
    }

    public void setSelectedMealType(String mealType) {
        selectedMealType.setValue(mealType);
    }

    public LiveData<String> getSelectedMealType() {
        return selectedMealType;
    }

    public void setSelectedMealTitle(String mealTitle) {
        selectedMealTitle.setValue(mealTitle);
    }

    public LiveData<String> getSelectedMealTitle() {
        return selectedMealTitle;
    }

    public void setSelectedMealImage(String mealImage) {
        selectedMealImage.setValue(mealImage);
    }

    public LiveData<String> getSelectedMealImage() {
        return selectedMealImage;
    }
}