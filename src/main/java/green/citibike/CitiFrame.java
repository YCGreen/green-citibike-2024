package green.citibike;

import green.citibike.aws.Response;
import green.citibike.json.StationInfo;
import green.citibike.json.Stations;
import green.citibike.json.StatusInfo;
import hu.akarnokd.rxjava3.swing.SwingSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanKeyListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.viewer.*;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Set;

public class CitiFrame extends JFrame {

    CitiService service = new CitiServiceFactory().getService();
    Stations<StationInfo> stations = service.getStations().blockingGet();
    Stations<StatusInfo> statuses = service.getStatus().blockingGet();
    CitiController controller = new CitiController(stations, statuses);=

    public CitiFrame() {
        setSize(800, 600);
        setTitle("CitiBike Mapping Service");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel main = new JPanel();
        main.setLayout(new BorderLayout());
        setContentPane(main);

        JXMapViewer mapViewer = new JXMapViewer();
        main.add(mapViewer, BorderLayout.CENTER);

        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapViewer.setTileFactory(tileFactory);

        tileFactory.setThreadPoolSize(8);
        mapViewer.setZoom(5);
        GeoPosition newYork = new GeoPosition(40.730610, -73.935242);
        mapViewer.setAddressLocation(newYork);

        MouseInputListener mia = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(mia);
        mapViewer.addMouseMotionListener(mia);
        mapViewer.addMouseListener(new CenterMapListener(mapViewer));
        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCursor(mapViewer));
        mapViewer.addKeyListener(new PanKeyListener(mapViewer));

        WaypointPainter<Waypoint> waypointPainter = new WaypointPainter<Waypoint>();
        Set<Waypoint> waypoints = Set.of(
                new DefaultWaypoint(new GeoPosition(40.77228687788679, -73.9842939376831))
        );
        waypointPainter.setWaypoints(waypoints);

       /* List<Painter<JXMapViewer>> painters = List.of(
                routePainter,
                waypointPainter
        );*/

      //  CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(painters);
       // mapViewer.setOverlayPainter(painter);

        GeoPosition currPosition = new GeoPosition(0, 0);


        mapViewer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                Point2D.Double point = new Point2D.Double(x, y);
                GeoPosition position = mapViewer.convertPointToGeoPosition(point);
                addWaypoint(waypoints, new DefaultWaypoint(position));
            }
        });

       /* mapViewer.zoomToBestFit(
                Set.of(from, startStation, endStation, to),
                1.0
        );*/

        JButton next = new JButton("Map");
        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(waypoints.size() == 2) {
                    mapPoints(waypoints);
                }

            }
        });
        main.add(next, BorderLayout.SOUTH);

    }

    private void addWaypoint(Set<Waypoint> set, Waypoint waypoint) {
        if(set.size() == 2) {
            set.clear();
        }

        set.add(waypoint);
    }

    private Response[] mapPoints(Set<Waypoint> set) {
        createDisposables();
        controller.replaceStationsInfo(stations, statuses);

        Response[] responses = new Response[set.size()];

        for(Waypoint waypoint : set) {
            responses[0] = controller.
        }





    }

    private void createDisposables() {
        Disposable disposable1 = service.getStations()
                .subscribeOn(Schedulers.io())
                .observeOn(SwingSchedulers.edt())
                .subscribe(
                        this::handleStationsResponse,
                        Throwable::printStackTrace);

        Disposable disposable2 = service.getStatus()
                .subscribeOn(Schedulers.io())
                .observeOn(SwingSchedulers.edt())
                .subscribe(
                        this::handleStatusResponse,
                        Throwable::printStackTrace);
    }

    private void handleStationsResponse(Stations<StationInfo> stationInfo) {
        stations = stationInfo;
    }

    private void handleStatusResponse(Stations<StatusInfo> statusInfo) {
        statuses = statusInfo;
    }

    public static void main(String[] args) {
        CitiFrame frame = new CitiFrame();
        frame.setVisible(true);
    }
}
