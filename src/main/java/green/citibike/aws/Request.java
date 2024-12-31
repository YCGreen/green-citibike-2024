package green.citibike.aws;

import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Waypoint;

public class Request {
    private Coordinate from;
    private Coordinate to;

    public Request(Coordinate from, Coordinate to) {
        this.from = from;
        this.to = to;
    }

    public Request(GeoPosition geoFrom, GeoPosition geoTo) {
        from = new Coordinate(geoFrom.getLatitude(), geoFrom.getLongitude());
        to = new Coordinate(geoTo.getLatitude(), geoTo.getLongitude());
    }

    public Coordinate getFrom() {
        return from;
    }

    public Coordinate getTo() {
        return to;
    }

    public String toString() {
        return String.format("""
        {
          "from": {
            "lat": %.4f,
            "lon": %.4f
          },
          "to": {
            "lat": %.4f,
            "lon": %.4f
          }
        }
        """, from.getLat(), from.getLon(), to.getLat(), to.getLon());
    }


}
