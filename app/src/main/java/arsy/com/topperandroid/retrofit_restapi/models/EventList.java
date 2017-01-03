package arsy.com.topperandroid.retrofit_restapi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class EventList {

    @SerializedName("websites")
    @Expose
    private ArrayList<EventListItem> events;

    @SerializedName("quote_max")
    @Expose
    private long quote_max;

    @SerializedName("quote_available")
    @Expose
    private long quote_available;

    public ArrayList<EventListItem> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<EventListItem> events){
        this.events = events;
    }

    public long getQuote_available() {
        return quote_available;
    }

    public void setQuote_available(long quote_available) {
        this.quote_available = quote_available;
    }

    public long getQuote_max() {
        return quote_max;
    }

    public void setQuote_max(long quote_max) {
        this.quote_max = quote_max;
    }
}
