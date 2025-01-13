/*COMP3450: Calvin Thomschke, Jaxon Isnardy, and Meenakshi Rajesh*/
package com.example.mealplanner;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

public class WeekModal {
    String day;
    String bPic;
    String bName;
    String bId;
    String lPic;
    String lName;
    String lId;
    String dPic;
    String dName;
    String dId;

    //constructor for modal
    public WeekModal()
    {
        this.day = "";
        this.bPic = "";
        this.bName = "";
        this.lPic = "";
        this.lName = "";
        this.dPic = "";
        this.dName = "";
        this.bId = "";
        this.lId = "";
        this.dId = "";
    }
    public WeekModal(String day, String bPic, String bName, String lPic, String lName, String dPic, String dName) {
        this.day = day;
        this.bPic = bPic;
        this.bName = bName;
        this.lPic = lPic;
        this.lName = lName;
        this.dPic = dPic;
        this.dName = dName;
    }

    //getters and setters


    public String getDay() {
        return day;
    }

    public String getbPic() {
        return bPic;
    }

    public String getbName() {
        return bName;
    }
    public String getbId() {
        return bId;
    }
    public String getlId() {
        return lId;
    }
    public String getdId() {
        return dId;
    }

    public String getlPic() {
        return lPic;
    }

    public String getlName() {
        return lName;
    }

    public String getdPic() {
        return dPic;
    }

    public String getdName() {
        return dName;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setbName(String bName) {
        this.bName = bName;
    }

    public void setbPic(String bPic) {
        this.bPic = bPic;
    }

    public void setdName(String dName) {
        this.dName = dName;
    }

    public void setdPic(String dPic) {
        this.dPic = dPic;
    }

    public void setlPic(String lPic) {
        this.lPic = lPic;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }
    public void setbId(String id) {
        if (id.equals(""))
        {
            setbName("Click to add a meal");
        }
        else
        {
            InputStream iStream = QueryHelper.EstablishConnection("?Action=GetRecipe&Recipe=" + id);
            JSONObject json = QueryHelper.BuildJsonObject(iStream);
            try {
                setbName(json.getString("Title"));
                setbPic(json.getString("Image"));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        this.bId = id;
    }
    public void setlId(String id) {
        if (id.equals(""))
        {
            setlName("Click to add a meal");
        }
        else
        {
            InputStream iStream = QueryHelper.EstablishConnection("?Action=GetRecipe&Recipe=" + id);
            JSONObject json = QueryHelper.BuildJsonObject(iStream);
            try {
                setlName(json.getString("Title"));
                setlPic(json.getString("Image"));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        this.lId = id;
    }
    public void setdId(String id) {
        if (id.equals(""))
        {
            setdName("Click to add a meal");
        }
        else
        {
            InputStream iStream = QueryHelper.EstablishConnection("?Action=GetRecipe&Recipe=" + id);
            JSONObject json = QueryHelper.BuildJsonObject(iStream);
            try {
                setdName(json.getString("Title"));
                setdPic(json.getString("Image"));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        this.dId = id;
    }
}
