package arsy.com.topperandroid;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import arsy.com.topperandroid.common.CommonGlobalVariable;
import arsy.com.topperandroid.database.DatabaseHandler;
import arsy.com.topperandroid.retrofit_restapi.interfaceCalls.Api_Calls;
import arsy.com.topperandroid.retrofit_restapi.models.EventList;
import arsy.com.topperandroid.retrofit_restapi.models.EventListItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FavouriteActivity extends AppCompatActivity {

    private RelativeLayout rl_upper, rl_lower;
    private TextView totalEvents;
    private EventListCustomAdapter adapter;
    private RecyclerView recyclerView = null;
    private ArrayList<EventListItem> listItem = null;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        activity = this;
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        rl_upper = (RelativeLayout) findViewById(R.id.ll_upper);
        rl_lower = (RelativeLayout) findViewById(R.id.rl_lower);
        totalEvents = (TextView) findViewById(R.id.totalEvents);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getFavouriteEvents(activity);
        if (listItem == null) {
            rl_lower.setVisibility(View.VISIBLE);
        } else {
            rl_upper.setVisibility(View.VISIBLE);
            adapter = new EventListCustomAdapter(listItem, activity);
            totalEvents.setText(""+listItem.size());
            recyclerView.setLayoutManager(new LinearLayoutManager(activity));
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);
        }
    }

    private void getFavouriteEvents(Context context) {
        DatabaseHandler dbHandler = new DatabaseHandler(context);
        Cursor cursor = dbHandler.getFavouriteEventList();
        if (cursor.moveToFirst()) {
            listItem = new ArrayList<EventListItem>();
            do {
                EventListItem eventlistitem = new EventListItem(context);
                eventlistitem.setId(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ID)));
                eventlistitem.setName(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_NAME)));
                eventlistitem.setCategory(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_CATEGORY)));
                eventlistitem.setDescription(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_DESCRIPTION)));
                eventlistitem.setExperience(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_EXPERIENCE)));
                eventlistitem.setImage(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_IMAGE)));
                eventlistitem.setFavourite(Integer.valueOf(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_FAVOURITE))));
                listItem.add(eventlistitem);
            } while (cursor.moveToNext());
            cursor.close();

        }
        dbHandler.close();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

