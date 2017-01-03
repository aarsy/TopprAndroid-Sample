
package arsy.com.topperandroid.retrofit_restapi.models;


import android.content.Context;
import android.database.Cursor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import arsy.com.topperandroid.database.DatabaseHandler;


public class EventListItem {


    public EventListItem(Context context, String id) {
        DatabaseHandler dbHandler = new DatabaseHandler(context);
        Cursor cursor = dbHandler.getEvent(id);
        if (cursor.moveToFirst()) {
            setId(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ID)));
            setName(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_NAME)));
            setCategory(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_CATEGORY)));
            setDescription(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_DESCRIPTION)));
            setExperience(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_EXPERIENCE)));
            setImage(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_IMAGE)));
            setFavourite(Integer.valueOf(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_FAVOURITE))));
        }
        dbHandler.close();
    }
    public EventListItem(Context context) {

    }

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("image")
    @Expose
    private String image;

    @SerializedName("category")
    @Expose
    private String category;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("experience")
    @Expose
    private String experience;

    private int favourite;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public int getFavourite() {
        return favourite;
    }

    public void setFavourite(int favourite) {
        this.favourite = favourite;
    }


}
