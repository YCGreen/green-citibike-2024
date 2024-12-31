package green.citibike.aws;

import green.citibike.json.StationInfo;
import green.citibike.json.StationStatus;

public class Response {
    private Coordinate from;
    private StationInfo start;
    private Coordinate to;
    private StationInfo end;

    public Response(Coordinate from, StationInfo start, Coordinate to, StationInfo end) {
        this.from = from;
        this.start = start;
        this.to = to;
        this.end = end;
    }

    public Coordinate getFrom() {
        return from;
    }

    public StationInfo getStart() {
        return start;
    }

    public Coordinate getTo() {
        return to;
    }

    public StationInfo getEnd() {
        return end;
    }

    public String toString() {
        return String.format("""
        {
          "from": {
            "lat": %.4f,
            "lon": %.4f
          },
          "start": {
            "lat": %.7f,
            "lon": %.8f,
            "name": "%s",
            "station_id": "%s"
          },
          "end": {
            "lat": %.7f,
            "lon": %.8f,
            "name": "%s",
            "station_id": "%s"
          },
          "to": {
            "lat": %.4f,
            "lon": %.4f
          }
        }
        """,
                from.getLat(), from.getLon(),
                start.getLat(), start.getLon(), start.getName(), start.getStationId(),
                end.getLat(), end.getLon(), end.getName(), end.getStationId(),
                to.getLat(), to.getLon());
    }
}
