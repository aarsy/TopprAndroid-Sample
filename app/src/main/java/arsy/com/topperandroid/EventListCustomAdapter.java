package arsy.com.topperandroid;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import arsy.com.topperandroid.database.DatabaseHandler;
import arsy.com.topperandroid.retrofit_restapi.models.EventListItem;


public class EventListCustomAdapter extends RecyclerView.Adapter<View_Holder_Events> {

    private List<EventListItem> list = Collections.emptyList();
    private Activity activity;

    public EventListCustomAdapter(List<EventListItem> list, Activity activity) {
        this.list = list;
        this.activity = activity;

    }

    public void reconstructAdapter(List<EventListItem> chatUserList) {
        Log.v("size11", ""+chatUserList.size());

        list=chatUserList;
        Log.v("size12", ""+list.size());
        notifyDataSetChanged();
        Log.v("size13", ""+list.size());
    }

    @Override
    public View_Holder_Events onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.v("createview", "1");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_event_list_layout, parent, false);
        View_Holder_Events holder = new View_Holder_Events(v);
        return holder;

    }

    @Override
    public void onBindViewHolder(final View_Holder_Events holder, int position) {
        Log.v("bind", "1");
        final EventListItem listItem = list.get(position);
        Picasso.with(activity).load(listItem.getImage()).into(holder.image);
        holder.company_name.setText(listItem.getName());
        holder.eventCategory.setText(listItem.getCategory());
        if (listItem.getFavourite() == 0)
            holder.favourite.setBackgroundResource(R.drawable.star);
        else
            holder.favourite.setBackgroundResource(R.drawable.star_favourite);
        holder.favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHandler dbHandler=new DatabaseHandler(activity);
                if (listItem.getFavourite() == 0) {
                    holder.favourite.setBackgroundResource(R.drawable.star_favourite);
                    dbHandler.updateFavourite(1, listItem.getId());
                    listItem.setFavourite(1);
                }
                else {
                    holder.favourite.setBackgroundResource(R.drawable.star);
                    dbHandler.updateFavourite(0, listItem.getId());
                    listItem.setFavourite(0);
                }
                dbHandler.close();
            }
        });
        holder.eventDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(activity, EventActivity.class);
                i.putExtra("id", listItem.getId());
                activity.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        Log.v("count", "1"+list.size());
        return list.size();

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


}


