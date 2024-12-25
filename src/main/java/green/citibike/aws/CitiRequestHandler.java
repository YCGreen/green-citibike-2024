package green.citibike.aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.google.gson.Gson;
import green.citibike.CitiController;
import green.citibike.CitiService;
import green.citibike.CitiServiceFactory;
import green.citibike.json.StationInfo;
import green.citibike.json.StationStatus;
import green.citibike.json.Stations;
import green.citibike.json.StatusInfo;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import javax.swing.*;

public class CitiRequestHandler implements RequestHandler<APIGatewayProxyRequestEvent, Response> {
    private CitiController citiController;
    private Stations<StationInfo> stationsInfo;
    private Stations<StatusInfo> statusInfo;

    public CitiRequestHandler() {
        createDisposables();
        citiController = new CitiController(stationsInfo, statusInfo);
    }

    private void createDisposables() {
        CitiService citiService = new CitiServiceFactory().getService();
        Disposable disposableStations = citiService.getStations()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.from(SwingUtilities::invokeLater))
                .subscribe(
                        this::handleResponseStations,
                        Throwable::printStackTrace);
        Disposable disposableStatus = citiService.getStatus()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.from(SwingUtilities::invokeLater))
                .subscribe(
                        this::handleResponseStatus,
                        Throwable::printStackTrace);
    }

    private void handleResponseStations(Stations<StationInfo> response) {
        stationsInfo = response;
    }

    private void handleResponseStatus(Stations<StatusInfo> response) {
        statusInfo = response;
    }

    @Override
    public Response handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        String body = event.getBody();
        Gson gson = new Gson();
        Request request = gson.fromJson(body, Request.class);

        Coordinate from = request.getFrom();
        Coordinate to = request.getTo();

        StationStatus directFrom = citiController.findClosestStationWithBike(from.getLat(), from.getLon());
        StationStatus directTo = citiController.findClosestStationWithDock(to.getLat(), to.getLon());

        return new Response(directFrom, directTo);
    }


}
