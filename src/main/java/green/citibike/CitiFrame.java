package green.citibike;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import green.citibike.aws.*;
import green.citibike.json.StationInfo;
import io.reactivex.rxjava3.core.Single;
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
import java.util.*;
import java.util.List;

public class CitiFrame extends JFrame {

    CitiRequestHandler requestHandler = new CitiRequestHandler();

    public CitiFrame() {
        setSize(800, 600);
        setTitle("CitiBike Mapping Service");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel main = new JPanel();
        main.setLayout(new BorderLayout());
        setContentPane(main);

        JXMapViewer mapViewer = new JXMapViewer();
        main.add(mapViewer, BorderLayout.CENTER);

        JTextArea textArea = new JTextArea();
        main.add(textArea, BorderLayout.EAST);

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

        List<GeoPosition> track = new ArrayList<>();

        RoutePainter routePainter = new RoutePainter(track);

        List<org.jxmapviewer.painter.Painter<org.jxmapviewer.JXMapViewer>> painters = List.of(
                routePainter,
                waypointPainter
        );

        CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(painters);
        mapViewer.setOverlayPainter(painter);

        mapViewer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                Point2D.Double point = new Point2D.Double(x, y);
                GeoPosition position = mapViewer.convertPointToGeoPosition(point);
                addToTrack(track, position, routePainter);
                setWaypointPainter(track, waypointPainter);
                mapViewer.repaint();
            }
        });

        JButton next = new JButton("Map");
        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (track.size() == 2) {
                    Response response = mapPoints(track);
                    textArea.setText(response.toString());
                    setWaypointPainter(track, waypointPainter);
                    mapViewer.zoomToBestFit(
                            new HashSet<>(track),
                            1.0
                    );
                }

            }
        });
        main.add(next, BorderLayout.SOUTH);
    }


    private void addToTrack(List<GeoPosition> track, GeoPosition geoPos, RoutePainter rp) {
        if (track.size() > 1) {
            GeoPosition geoPosFirst = track.get(1);
            track.clear();
            track.add(geoPosFirst);
        }

        track.add(geoPos);
        rp.setTrack(track);
    }

    private void setWaypointPainter(List<GeoPosition> track, WaypointPainter wp) {
        HashSet<Waypoint> waypoints = new HashSet<>();

        for (GeoPosition geoPos : track) {
            waypoints.add(new DefaultWaypoint(geoPos));
        }

        wp.setWaypoints(waypoints);
    }

    private Response mapPoints(List<GeoPosition> track) {
        /*
        Request request = new Request(track.get(0), track.get(1));
        String json = request.toString();
        APIGatewayProxyRequestEvent event = new APIGatewayProxyRequestEvent();
        event.setBody(json);

        Response response = requestHandler.handleRequest(event, null);

        Disposable disposable = service.getStations(new Request(track.get(0), track.get(1)))
                .subscribeOn(Schedulers.io())
                .observeOn(SwingSchedulers.edt())
                .subscribe(
                        this::handleResponse,
                        Throwable::printStackTrace);
*/LambdaService service = new LambdaServiceFactory().getService();
        Response response = service.getStations(new Request(track.get(0), track.get(1))).blockingGet();
        StationInfo stationInfo = response.getStart();
        GeoPosition stationFrom = new GeoPosition(stationInfo.getLat(), stationInfo.getLon());
        stationInfo = response.getEnd();
        GeoPosition stationTo = new GeoPosition(stationInfo.getLat(), stationInfo.getLon());

        track.add(1, stationFrom);
        track.add(2, stationTo);

        return response;


    }

    public static void main(String[] args) {
        CitiFrame frame = new CitiFrame();
        frame.setVisible(true);
    }
}
