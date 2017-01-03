package arsy.com.topperandroid.retrofit_restapi.interfaceCalls;


import arsy.com.topperandroid.common.CommonGlobalVariable;
import arsy.com.topperandroid.retrofit_restapi.models.EventList;
import retrofit2.Call;
import retrofit2.http.GET;

public interface Api_Calls {
    @GET(CommonGlobalVariable.API_EVENTS_LIST)
    public Call<EventList> getContactsJson();
}
