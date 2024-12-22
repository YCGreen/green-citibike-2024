package green.citibike;

import io.reactivex.rxjava3.core.Single;
import green.citibike.json.StationInfo;
import green.citibike.json.Stations;
import green.citibike.json.StatusInfo;
import retrofit2.http.GET;

public interface CitiService {
    @GET("station_information.json")
    Single<Stations<StationInfo>> getStations();

    @GET("station_status.json")
    Single<Stations<StatusInfo>> getStatus();

}
