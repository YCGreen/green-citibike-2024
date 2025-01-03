package green.citibike.json;

public class StationInfo {
    public double lon;
    public double lat;
    public String name;
    //CHECKSTYLE:OFF
    public String station_id;


    public StationInfo(double lon, double lat, String name, String station_id) {
        //CHECKSTYLE:ON
        this.lon = lon;
        this.lat = lat;
        this.name = name;
        //CHECKSTYLE:OFF
        this.station_id = station_id;
        //CHECKSTYLE:ON
    }
}
