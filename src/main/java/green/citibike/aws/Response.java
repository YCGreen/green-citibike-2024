package green.citibike.aws;

import green.citibike.json.StationInfo;
import green.citibike.json.StationStatus;

public class Response {
    private From from;
    private Start start;
    private To to;
    private End end;

    public Response(From from, Start start, To to, End end) {
        this.from = from;
        this.start = start;
        this.to = to;
        this.end = end;
    }

    public Response(StationStatus from, StationStatus to) {
        this.from = new From(from.getLat(), from.getLon());
        this.start = new Start(from.getLat(), from.getLon(), from.getName(), from.getStationId());
        this.to = new To(to.getLat(), to.getLon());
        this.end = new End(to.getLat(), to.getLon(), to.getName(), to.getStationId());
    }

    public From getFrom() {
        return from;
    }

    public To getTo() {
        return to;
    }

    public Start getStart() {
        return start;
    }

    public End getEnd() {
        return end;
    }

    public class From {
        private double lat;
        private double lon;

        public From(double lat, double lon) {
            this.lat = lat;
            this.lon = lon;
        }
    }

    public class Start {
        private double lat;
        private double lon;
        private String name;
        private String station_id;

        public Start(double lat, double lon, String name, String station_id) {
            this.lat = lat;
            this.lon = lon;
            this.name = name;
            this.station_id = station_id;
        }

    }

    public class To {
        private double lat;
        private double lon;

        public To(double lat, double lon) {
            this.lat = lat;
            this.lon = lon;
        }
    }

    public class End {
        private double lat;
        private double lon;
        private String name;
        private String station_id;

        public End(double lat, double lon, String name, String station_id) {
            this.lat = lat;
            this.lon = lon;
            this.name = name;
            this.station_id = station_id;
        }
    }

}
