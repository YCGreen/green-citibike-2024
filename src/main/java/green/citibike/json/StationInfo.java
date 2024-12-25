package green.citibike.json;

import com.google.gson.annotations.SerializedName;

public class StationInfo {
    public double lon;
    public double lat;
    public String name;
    @SerializedName("station_id")
    public String station_id;

    public StationInfo(double lon, double lat, String name, String station_id) {
        this.lon = lon;
        this.lat = lat;
        this.name = name;
        this.station_id = station_id;
    }
}
