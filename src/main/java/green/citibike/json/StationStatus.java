package green.citibike.json;

public class StationStatus {
    public double lon;
    public double lat;
    public String name;
    public String station_id;
    public int isRenting;
    public int num_bikes_available;
    public int num_docks_available;

    public StationStatus(double lon, double lat, String name, String station_id) {
        this.lon = lon;
        this.lat = lat;
        this.name = name;
        this.station_id = station_id;
    }

    public void addStatus(int isRenting, int num_bikes_available, int num_docks_available) {
        this.isRenting = isRenting;
        this.num_bikes_available = num_bikes_available;
        this.num_docks_available = num_docks_available;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Longitude: " + lon + "\n");
        sb.append("Latitude: " + lat + "\n");
        sb.append("Name: " + name + "\n");
        sb.append("StationID: " + station_id + "\n");
        sb.append("IsRenting: " + isRenting + "\n");
        sb.append("NumBikes: " + num_bikes_available + "\n");
        sb.append("NumDocks: " + num_docks_available + "\n");
        return sb.toString();
    }
}
