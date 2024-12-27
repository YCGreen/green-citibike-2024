package green.citibike;

import green.citibike.json.StationInfo;
import green.citibike.json.StationStatus;
import green.citibike.json.Stations;
import green.citibike.json.StatusInfo;

import java.util.HashMap;
import java.util.List;

public class CitiController {
    private Stations<StationInfo> stationsInfo;
    private Stations<StatusInfo> statusInfo;
    HashMap<String, StationStatus> stationStatusMap = new HashMap<>();
    List<StationStatus> stationStatusList;
    final double RADIUS = 3958.8;

    public CitiController(Stations<StationInfo> stationsInfo, Stations<StatusInfo> statusInfo) {
        replaceStationsInfo(stationsInfo, statusInfo);
    }

    public void replaceStationsInfo(Stations<StationInfo> newStationsInfo, Stations<StatusInfo> newStatusInfo) {
        stationsInfo = newStationsInfo;
        statusInfo = newStatusInfo;
        mergeStationStatus();
        stationStatusList = stationStatusMap.values().stream().toList();
    }

    private void mergeStationStatus() {
        List<StationInfo> stationInfoList = stationsInfo.data.stations;
        List<StatusInfo> statusInfoList = statusInfo.data.stations;

        insertStationInfo(stationInfoList);
        insertStatusInfo(statusInfoList);
    }

    private void insertStationInfo(List<StationInfo> stationInfoList) {
        for (StationInfo stationInfo : stationInfoList) {
            stationStatusMap.put(stationInfo.station_id, new StationStatus(stationInfo));
        }
    }

    private void insertStatusInfo(List<StatusInfo> statusInfoList) {
        for (StatusInfo statusInfo : statusInfoList) {
            StationStatus stationStatus = stationStatusMap.get(statusInfo.station_id);
            stationStatus.addStatus(statusInfo);
            stationStatusMap.replace(statusInfo.station_id, stationStatus);
        }
    }

    public List<StationStatus> getStationStatus() {
        return stationStatusList;
    }

    public StationStatus getStatusInfo(String stationId) {
        return stationStatusMap.get(stationId);
    }

    private String findClosestStationCoords(double lat, double lon) {
        double closest = Double.MAX_VALUE;
        String closestStationId = stationStatusList.get(0).getStationId();

        for (StationStatus stationStatus : stationStatusList) {
            double distance = haversine(stationStatus.getLat(), stationStatus.getLon(), lat, lon);
            if (distance < closest) {
                closest = distance;
                closestStationId = stationStatus.getStationId();
            }
        }

        return closestStationId;
    }

    //reference https://www.geeksforgeeks.org/haversine-formula-to-find-distance-between-two-points-on-a-sphere/
    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.sin(dLon / 2) * Math.sin(dLon / 2)
                * Math.cos(lat1) * Math.cos(lat2);

        double c = 2 * Math.asin(Math.sqrt(a));

        return RADIUS * c * 0.621371;
    }

    private double haversine(String station1, String station2) {
        double lat1 = stationStatusMap.get(station1).getLat();
        double lat2 = stationStatusMap.get(station2).getLat();
        double lon1 = stationStatusMap.get(station1).getLon();
        double lon2 = stationStatusMap.get(station2).getLon();
        return haversine(lat1, lon1, lat2, lon2);
    }

    private StationStatus findClosestStationId(double lat, double lon, boolean hasBike) {
        String stationId = findClosestStationCoords(lat, lon);

        StationStatus stationStatus = stationStatusMap.get(stationId);
        int available = hasBike ? stationStatus.getNumDocksAvailable()
                : stationStatus.getNumBikesAvailable();
        if (available == 0) {
            String closestBefore = findClosestStationDirected(stationId, hasBike, true);
            String closestAfter = findClosestStationDirected(stationId, hasBike, false);
            return haversine(closestBefore, stationId) > haversine(closestAfter, stationId)
                    ? stationStatusMap.get(closestBefore) :  stationStatusMap.get(closestAfter);
        }

        return stationStatusMap.get(stationId);
    }

    private String findClosestStationDirected(String stationId, boolean hasBike, boolean forwards) {
        int currIx = stationStatusList.indexOf(stationStatusMap.get(stationId));

        while (!checkAvailability(stationStatusList.get(currIx), hasBike)) {
            currIx = getNextIx(currIx, forwards);
        }

        return stationStatusList.get(currIx).getStationId();
    }

    private int getNextIx(int currIx, boolean forwards) {
        int step = forwards ? 1 : -1;
        return (currIx + step + stationStatusList.size()) % stationStatusList.size();
    }

    private boolean checkAvailability(StationStatus stationStatus, boolean hasBike) {
        return hasBike ? stationStatus.getNumDocksAvailable() > 0 : stationStatus.getNumBikesAvailable() > 0;
    }

    public StationStatus findClosestStationWithBike(double lat, double lon) {
        return findClosestStationId(lat, lon, false);
    }

    public StationStatus findClosestStationWithDock(double lat, double lon) {
        return findClosestStationId(lat, lon, true);
    }

}
