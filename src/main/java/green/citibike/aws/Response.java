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

    public Response(StationStatus from, StationStatus to) {
        Coordinate setFrom = new Coordinate(from.getLat(), from.getLon());
        Coordinate setTo = new Coordinate(to.getLat(), to.getLon());

        this.from = setFrom;
        this.to = setTo;
        start = from.getStationInfo();
        end = to.getStationInfo();
    }

    public Coordinate getFrom() {
        return from;
    }

    public Coordinate getTo() {
        return to;
    }

    public StationInfo getStart() {
        return start;
    }

    public StationInfo getEnd() {
        return end;
    }

}
