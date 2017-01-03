package arsy.com.topperandroid;


import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import android.webkit.WebView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import arsy.com.topperandroid.database.DatabaseHandler;


public class ShowWebChartActivity extends AppCompatActivity {
    WebView webView;
    int num1, num2, num3, num4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_webchart);
        DatabaseHandler dbHandler = new DatabaseHandler(this);

        num1 = dbHandler.getRowsCountForCategory("HACKATHON");
        num2 = dbHandler.getRowsCountForCategory("HIRING");
        num3 = dbHandler.getRowsCountForCategory("BOT");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        Log.v("balues", "" + num1 + " " + num2 + " " + num3 + " ");
        webView = (WebView) findViewById(R.id.web);
        webView.getSettings().setJavaScriptEnabled(true);
        String content = null;
        try {
            AssetManager assetManager = getAssets();
            InputStream in = assetManager.open("piechart.html");
            byte[] bytes = readFully(in);
            content = new String(bytes, "UTF-8");
        } catch (IOException e) {

        }
        String formattedContent = String.format(content, num1, num2, num3);
        webView.loadDataWithBaseURL("file:///android_asset/", formattedContent, "text/html", "utf-8", null);
        webView.requestFocusFromTouch();
    }

    private static byte[] readFully(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        for (int count; (count = in.read(buffer)) != -1; ) {
            out.write(buffer, 0, count);
        }
        return out.toByteArray();
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
