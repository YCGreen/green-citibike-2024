package green.citibike.json;

public class StatusInfo {
    public String station_id;
    public int isRenting;
    public int num_bikes_available;
    public int num_docks_available;

    public StatusInfo(String station_id, int isRenting,
                      int num_bikes_available, int num_docks_available) {
        this.station_id = station_id;
        this.isRenting = isRenting;
        this.num_bikes_available = num_bikes_available;
        this.num_docks_available = num_docks_available;
    }
}
