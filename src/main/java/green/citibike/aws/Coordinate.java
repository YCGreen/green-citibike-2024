package green.citibike.aws;

import org.jxmapviewer.viewer.GeoPosition;

public class Coordinate {
    private double lat;
    private double lon;

    public Coordinate(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String toString() {
        return String.format("(%f, %f)", lat, lon);
    }

}
