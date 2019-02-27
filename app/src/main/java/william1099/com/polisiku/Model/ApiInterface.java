package william1099.com.polisiku.Model;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface ApiInterface {
    @GET
    Call<MyPlace> getData(@Url String url);
}
