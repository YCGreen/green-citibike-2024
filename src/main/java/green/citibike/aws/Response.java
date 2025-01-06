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
        return String.format(
                """
                        Begin at coordinates
                        
                        %s
                        
                        Walk to the CitiBike station\s
                        
                        %s
                        
                        From there, bike to the CitiBike station\s
                         
                        %s
                        
                        and walk to the end location at\s
                        
                        %s.""",
                from.toString(),
                start.toString(),
                end.toString(),
                to.toString()
        );
    }


}
