package arsy.com.topperandroid.common;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CommonGlobalVariable {
    public static final String HOST = "https://hackerearth.0x10.info/";   //Server url
    public static final String API_EVENTS_LIST = "api/toppr_events?type=json&query=list_events";         //API name on server
    public static final String QUOTA_AVAILABLE= "quota_available";
    public static final String QUOTA_MAX= "quota_max";

    public static boolean checkInternetConnection(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (conn != null) {
            NetworkInfo[] info = conn.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

