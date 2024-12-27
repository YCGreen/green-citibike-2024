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

public class CitiRequestHandler implements RequestHandler<APIGatewayProxyRequestEvent, Response> {
    private CitiService citiService;
    private CitiController citiController;

    public CitiRequestHandler() {
        CitiService citiService = new CitiServiceFactory().getService();
    }

    @Override
    public Response handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        Stations<StationInfo> stationsInfo = citiService.getStations().blockingGet();
        Stations<StatusInfo> statusInfo = citiService.getStatus().blockingGet();
        citiController.replaceStationsInfo(stationsInfo, statusInfo);
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
