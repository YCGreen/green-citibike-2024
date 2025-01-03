package green.citibike.json;

public class StationStatus {
    private StationInfo stationInfo;
    private StatusInfo statusInfo;

    public StationStatus(StationInfo stationInfo, StatusInfo statusInfo) {
        this.stationInfo = stationInfo;
        this.statusInfo = statusInfo;
    }

    public StationStatus(StationInfo stationInfo) {
        this.stationInfo = stationInfo;
    }

    public void addStatus(StatusInfo statusInfo) {
        this.statusInfo = statusInfo;
    }

    public StationInfo getStationInfo() {
        return stationInfo;
    }

    public StatusInfo getStatusInfo() {
        return statusInfo;
    }

    public double getLon() {
        return stationInfo.lon;
    }

    public double getLat() {
        return stationInfo.lat;
    }

    public String getName() {
        return stationInfo.name;
    }

    public String getStationId() {
        //CHECKSTYLE:OFF
        return stationInfo.station_id;
        //CHECKSTYLE:ON
    }

    public int isRenting() {
        return statusInfo.isRenting;
    }

    public int getNumBikesAvailable() {
        return statusInfo.num_bikes_available;
    }

    public int getNumDocksAvailable() {
        return statusInfo.num_docks_available;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Longitude: " + getLon() + "\n");
        sb.append("Latitude: " + getLat() + "\n");
        sb.append("Name: " + getName() + "\n");
        sb.append("StationID: " + getStationId() + "\n");
        sb.append("IsRenting: " + isRenting() + "\n");
        sb.append("NumBikes: " + getNumBikesAvailable() + "\n");
        sb.append("NumDocks: " + getNumDocksAvailable() + "\n");
        return sb.toString();
    }
}
