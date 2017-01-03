package arsy.com.topperandroid;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
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
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity {
    private EventList eventList;
    private RelativeLayout ll_upper;
    private TextView totalEvents, api_quota;
    private RelativeLayout favouriteHistory;
    private RelativeLayout rl_refresh;
    private EventListCustomAdapter adapter;
    private ImageView refresh;
    private Animation rotationAnimation;
    private RecyclerView recyclerView = null;
    private Spinner spinner = null;
    private SwipeRefreshLayout swipeContainer;
    private Activity activity;
    private SharedPreferences prefs;
    private long lastPress = 0L;
    private SearchView searchView;
    private MenuItem mSearchMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        refresh = (ImageView) findViewById(R.id.refresh);
        ll_upper = (RelativeLayout) findViewById(R.id.ll_upper);
        totalEvents = (TextView) findViewById(R.id.totalEvents);
        favouriteHistory = (RelativeLayout) findViewById(R.id.rl_favhistory);
        rl_refresh = (RelativeLayout) findViewById(R.id.rl_refresh);
        spinner = (Spinner) findViewById(R.id.spinner);
        api_quota= (TextView) findViewById(R.id.api_quota);
        prefs= PreferenceManager.getDefaultSharedPreferences(activity);
        final ArrayList<String> list = new ArrayList<>();
        list.add("Category");
        list.add("Favourites");
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, list));
        spinner.setSelection(0, false);
        spinner.setOnItemSelectedListener(listener);
        rl_refresh.setVisibility(View.VISIBLE);
        rotationAnimation = AnimationUtils.loadAnimation(activity, R.anim.rotate_animation);
        refresh.startAnimation(rotationAnimation);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rotationAnimation.hasEnded()) {
                    refresh.startAnimation(rotationAnimation);
                    getAllEvents(false);
                }
            }
        });

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllEvents(true);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        favouriteHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity, FavouriteActivity.class));
            }
        });
        if (getFromDbifExists(activity)) {

            rl_refresh.setVisibility(View.GONE);
            ll_upper.setVisibility(View.VISIBLE);
            totalEvents.setText("" + eventList.getEvents().size());
            api_quota.setText("API Quota: "+(int)((eventList.getQuote_max()-eventList.getQuote_available())*100/eventList.getQuote_max())+"%");
            recyclerView.setLayoutManager(new LinearLayoutManager(activity));
            recyclerView.setHasFixedSize(true);
            sort(0);
            adapter = new EventListCustomAdapter(eventList.getEvents(), activity);
            recyclerView.setAdapter(adapter);


        } else
            getAllEvents(false);
        handleIntent(getIntent());

    }

    int check = 0;

    AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            try {
                check = check + 1;
                if (check > 1) {
                    sort(i);
                    if (eventList != null) {
                        if (eventList.getEvents() != null)
                            adapter.reconstructAdapter(eventList.getEvents());
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    private boolean getFromDbifExists(Context context) {
        DatabaseHandler dbHandler = new DatabaseHandler(context);
        Cursor cursor = dbHandler.getEventList();
        ArrayList<EventListItem> list;
        if (cursor.moveToFirst()) {
            list = new ArrayList<EventListItem>();
            eventList = new EventList();
            do {
                EventListItem eventlistitem = new EventListItem(context);
                eventlistitem.setId(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ID)));
                eventlistitem.setName(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_NAME)));
                eventlistitem.setCategory(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_CATEGORY)));
                eventlistitem.setDescription(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_DESCRIPTION)));
                eventlistitem.setExperience(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_EXPERIENCE)));
                eventlistitem.setImage(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_IMAGE)));
                eventlistitem.setFavourite(Integer.valueOf(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_FAVOURITE))));
                list.add(eventlistitem);
            } while (cursor.moveToNext());
            cursor.close();
            eventList.setEvents(list);
            eventList.setQuote_available(prefs.getLong(CommonGlobalVariable.QUOTA_AVAILABLE,-1));
            eventList.setQuote_max(prefs.getLong(CommonGlobalVariable.QUOTA_MAX,-1));
            dbHandler.close();
            return true;
        } else {
            dbHandler.close();
            return false;
        }

    }

    private void sort(final int i) {
        if (eventList != null) {
            Collections.sort(eventList.getEvents(), new Comparator<EventListItem>() {
                public int compare(EventListItem evt1, EventListItem evt2) {
                    // ## Ascending order
                    if (i == 0)
                        return evt1.getCategory().compareToIgnoreCase(evt2.getCategory()); // To compare string values
                    else
                        return Integer.valueOf(evt2.getFavourite()).compareTo(Integer.valueOf(evt1.getFavourite()));

                    // To compare string values
                    // return Integer.valueOf(emp1.getId()).compareTo(emp2.getId()); // To compare integer values

                    // ## Descending order
                    // return emp2.getFirstName().compareToIgnoreCase(emp1.getFirstName()); // To compare string values
                    // return Integer.valueOf(emp2.getId()).compareTo(emp1.getId()); // To compare integer values
                }

            });
        }

    }

    void getAllEvents(final boolean reconstructAdapter) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(CommonGlobalVariable.HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api_Calls service = retrofit.create(Api_Calls.class);

        Call<EventList> call = service.getContactsJson();
        call.enqueue(new Callback<EventList>() {
            @Override
            public void onResponse(Call<EventList> call, Response<EventList> response) {
                Log.v("onresponse", "true");
                eventList = response.body();
                rotationAnimation.cancel();
                rotationAnimation.reset();
                rl_refresh.setVisibility(View.GONE);
                ll_upper.setVisibility(View.VISIBLE);
                ll_upper.setVisibility(View.VISIBLE);
                totalEvents.setText("" + eventList.getEvents().size());
                api_quota.setText("API Quota: "+(int)((eventList.getQuote_max()-eventList.getQuote_available())*100/eventList.getQuote_max())+"%");
                if (reconstructAdapter) {
                    spinner.setSelection(0, false);
                    sort(spinner.getSelectedItemPosition());
                    adapter.reconstructAdapter(eventList.getEvents());

                } else {
                    adapter = new EventListCustomAdapter(eventList.getEvents(), activity);
                    spinner.setSelection(0, false);
                    recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                    recyclerView.setHasFixedSize(true);
                    sort(spinner.getSelectedItemPosition());
                    recyclerView.setAdapter(adapter);
                }
                swipeContainer.setRefreshing(false);
                prefs.edit().putLong(CommonGlobalVariable.QUOTA_AVAILABLE, eventList.getQuote_available()).commit();
                prefs.edit().putLong(CommonGlobalVariable.QUOTA_MAX, eventList.getQuote_max()).commit();
                DatabaseHandler dbHandler = new DatabaseHandler(activity);
                dbHandler.deleteAllEventsInfo();
                for (int i = 0; i < eventList.getEvents().size(); i++) {
                    EventListItem item = eventList.getEvents().get(i);
                    dbHandler.insertEventInfo(item.getId(), item.getName(), item.getImage(), item.getCategory(), item.getDescription(), item.getExperience());
                }
                dbHandler.close();

            }

            @Override
            public void onFailure(Call<EventList> call, Throwable t) {
                Log.v("onfailure", "true" + " " + t.getMessage());
                swipeContainer.setRefreshing(false);
                rotationAnimation.cancel();
                rotationAnimation.reset();
                rl_refresh.setVisibility(View.VISIBLE);
                ll_upper.setVisibility(View.GONE);

            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        // Inflate menu to add items to action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu);
        mSearchMenu = menu.findItem(R.id.menu_search);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                adapter.reconstructAdapter(eventList.getEvents());
                totalEvents.setText("" + eventList.getEvents().size());
                searchView.onActionViewCollapsed();
                mSearchMenu.collapseActionView();
                return true;
            }
        });

        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.v("query", ""+query);
            ArrayList<EventListItem> list=new ArrayList<>();
            for(int i=0; i<eventList.getEvents().size(); i++){
                EventListItem item=eventList.getEvents().get(i);
                if(item.getName().toLowerCase().contains(query.toLowerCase())||item.getCategory().toLowerCase().contains(query.toLowerCase()))
                    list.add(item);
            }
            totalEvents.setText("" + list.size());
            adapter.reconstructAdapter(list);
        }

    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            searchView.onActionViewCollapsed();
            mSearchMenu.collapseActionView();
        } else {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastPress > 3000) {
                Toast.makeText(activity, "Press back again to exit", Toast.LENGTH_SHORT).show();
                lastPress = currentTime;
            } else {
                super.onBackPressed();
            }
        }
    }


}
