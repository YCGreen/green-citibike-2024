package green.citibike.aws;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LambdaService {
   @GET("/CitiBikeRequestFunction")
   Single<Response> getStations(
    @Query("from") Request request
   );

}
