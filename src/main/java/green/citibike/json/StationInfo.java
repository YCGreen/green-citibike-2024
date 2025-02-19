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

    public double getLon() {
        return lon;
    }

    public double getLat() {
        return lat;
    }

    public String getName() {
        return name;
    }

    public String getStationId() {
        //CHECKSTYLE:OFF
        return station_id;
        //CHECKSTYLE:ON
    }

    public String toString() {
        return String.format(
                "Station: {\n"
                        + "  Name: %s,\n"
                        + "  Station ID: %s,\n"
                        + "  Latitude: %.6f,\n"
                        + "  Longitude: %.6f\n"
                        + "}",
                name,
                station_id,
                lat,
                lon
        );
    }
}
