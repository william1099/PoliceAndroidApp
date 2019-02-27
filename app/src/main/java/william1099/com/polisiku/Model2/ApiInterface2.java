package william1099.com.polisiku.Model2;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;


public interface ApiInterface2 {

        @GET
        Call<MyPlace2> getData(@Url String url);

}
