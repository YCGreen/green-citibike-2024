package green.citibike.json;

import com.google.gson.annotations.SerializedName;

public class StationInfo {
    public double lon;
    public double lat;
    public String name;
    @SerializedName("station_id")
    public String station_id;
}
