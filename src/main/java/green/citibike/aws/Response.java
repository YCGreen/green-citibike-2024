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
}
