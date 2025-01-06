package green.citibike.mvc;

import green.citibike.aws.LambdaService;
import green.citibike.aws.LambdaServiceFactory;
import green.citibike.aws.Request;
import green.citibike.aws.Response;
import green.citibike.json.StationInfo;
import hu.akarnokd.rxjava3.swing.SwingSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Waypoint;
import org.jxmapviewer.viewer.WaypointPainter;

import javax.swing.*;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.List;

public class CitiController {

    private final LambdaService service = new LambdaServiceFactory().getService();
    private JXMapViewer mapViewer;
    RoutePainter routePainter;
    List<GeoPosition> track;
    WaypointPainter waypointPainter;
    JTextArea textArea;

    public CitiController(JXMapViewer viewer, RoutePainter routePainter, List<GeoPosition> track,
                          WaypointPainter waypointPainter, JTextArea textArea) {
        this.mapViewer = viewer;
        this.routePainter = routePainter;
        this.track = track;
        this.waypointPainter = waypointPainter;
        this.textArea = textArea;
    }

    public void addGeoPosition(int x, int y) {
        Point2D.Double point = new Point2D.Double(x, y);
        GeoPosition position = mapViewer.convertPointToGeoPosition(point);
        addToTrack(position);
        setWaypointPainter();
        mapViewer.repaint();
    }

    public void addToTrack(GeoPosition geoPos) {
        if (track.size() > 1) {
            GeoPosition geoPosFirst = track.get(1);
            track.clear();
            track.add(geoPosFirst);
        }

        track.add(geoPos);
        routePainter.setTrack(track);
    }

    public void mapPoints(List<GeoPosition> track) {
        Disposable disposable = service.getStations(new Request(track.get(0), track.get(1)))
                .subscribeOn(Schedulers.io())
                .observeOn(SwingSchedulers.edt())
                .subscribe(
                        (response) -> handleResponse(response),
                        Throwable::printStackTrace);
    }

    private void handleResponse(Response response) {
        StationInfo stationInfo = response.getStart();
        GeoPosition stationFrom = new GeoPosition(stationInfo.getLat(), stationInfo.getLon());
        stationInfo = response.getEnd();
        GeoPosition stationTo = new GeoPosition(stationInfo.getLat(), stationInfo.getLon());

        track.add(1, stationFrom);
        track.add(2, stationTo);

        textArea.setText(response.toString());
    }

    public void updateMap() {
        if (track.size() == 2) {
            mapPoints(track);
            setWaypointPainter();
            mapViewer.zoomToBestFit(
                    new HashSet<>(track),
                    1.0
            );
        }
    }

    public void setWaypointPainter() {
        HashSet<Waypoint> waypoints = new HashSet<>();

        for (GeoPosition geoPos : track) {
            waypoints.add(new DefaultWaypoint(geoPos));
        }

        waypointPainter.setWaypoints(waypoints);
    }
}
