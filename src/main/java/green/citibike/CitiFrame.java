package green.citibike;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import green.citibike.aws.CitiRequestHandler;
import green.citibike.aws.Request;
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
import java.util.List;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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
        HashSet<Waypoint> waypoints = new HashSet<>();
        waypoints.add(new DefaultWaypoint(new GeoPosition(40.77228687788679, -73.9842939376831)));
        waypointPainter.setWaypoints(waypoints);

        List<GeoPosition> track = new ArrayList<>();

        RoutePainter routePainter = new RoutePainter(track);

        List<org.jxmapviewer.painter.Painter<org.jxmapviewer.JXMapViewer>> painters = List.of(
                routePainter,
                waypointPainter
        );

        CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(painters);
        mapViewer.setOverlayPainter(painter);

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
                    Response response = mapPoints(waypoints);
                    System.out.println(response.toString());
                }

            }
        });
        main.add(next, BorderLayout.SOUTH);

    }

    private void addWaypoint(HashSet<Waypoint> waypoints, Waypoint waypoint) {
        if(waypoints.size() == 2) {
            waypoints.clear();
        }

        waypoints.add(waypoint);
    }

    private Response mapPoints(HashSet<Waypoint> waypoints) {
        Iterator<Waypoint> iterator = waypoints.iterator();
        Waypoint waypoint = iterator.next();
        GeoPosition geoPos1 = new GeoPosition(waypoint.getPosition());
        Request request = new Request(iterator.next(), iterator.next());
        String json = request.toString();
        APIGatewayProxyRequestEvent event = new APIGatewayProxyRequestEvent();
        event.setBody(json);

        return requestHandler.handleRequest(event, null);
    }


    public static void main(String[] args) {
        CitiFrame frame = new CitiFrame();
        frame.setVisible(true);
    }
}
