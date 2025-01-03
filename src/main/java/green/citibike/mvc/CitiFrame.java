package green.citibike.mvc;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.input.CenterMapListener;
import org.jxmapviewer.input.PanKeyListener;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.*;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

public class CitiFrame extends JFrame {
    private CitiController citiController = new CitiController();

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

        List<Painter<JXMapViewer>> painters = List.of(
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
                citiController.addGeoPosition(x, y, mapViewer, routePainter, track, waypointPainter);
            }
        });

        JButton next = new JButton("Map");
        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                citiController.updateMap(track, textArea, waypointPainter, mapViewer);
            }
        });

        main.add(next, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        CitiFrame frame = new CitiFrame();
        frame.setVisible(true);
    }
}
