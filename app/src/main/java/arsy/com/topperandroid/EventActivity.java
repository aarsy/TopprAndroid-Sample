package arsy.com.topperandroid;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import arsy.com.topperandroid.retrofit_restapi.models.EventListItem;

public class EventActivity extends AppCompatActivity {
    ImageView image;
    TextView company, description, experience;
    RelativeLayout rl_link, rl_share, rl_statictics;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_layout);
        image= (ImageView) findViewById(R.id.imageView);
        company= (TextView) findViewById(R.id.company_name);
        experience= (TextView) findViewById(R.id.experience);
        description= (TextView) findViewById(R.id.description);
        rl_link= (RelativeLayout) findViewById(R.id.rl_link);
        rl_share= (RelativeLayout) findViewById(R.id.rl_share);
        rl_statictics= (RelativeLayout) findViewById(R.id.rl_statistics);
        String id=getIntent().getStringExtra("id");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        activity=this;
        final EventListItem item=new EventListItem(activity, id);
        Picasso.with(activity).load(item.getImage()).into(image);
        company.setText(item.getName());
        String experi=item.getExperience();
        if(experi.contains("yrs")||experi.contains("years")) {
            experience.setText("EXPERIENCE: " + experi);
        }else{
            experience.setText("EXPERIENCE: " + experi+" years");
        }
        description.setText(item.getDescription());
        rl_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.toppr.com/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        rl_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String sharingbody = "Job Description:\n Company Name: "+ item.getName()+"\nExperience: "+item.getExperience()+"\nDescription: "+item.getDescription();
                intent.putExtra(Intent.EXTRA_SUBJECT, "Company Job");
                intent.putExtra(Intent.EXTRA_TEXT, sharingbody);
                startActivity(intent);
            }
        });
        rl_statictics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(activity, ShowWebChartActivity.class);
                startActivity(i);
            }
        });

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
