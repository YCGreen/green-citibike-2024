package green.citibike;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.CloudFrontEvent;
import com.google.gson.Gson;

public class CitiRequestHandler implements RequestHandler<APIGatewayProxyRequestEvent, CloudFrontEvent.Response> {

    @Override
    public CloudFrontEvent.Response handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        String body = event.getBody();
        Gson gson = new Gson();
        CloudFrontEvent.Request request = gson.fromJson(body, CloudFrontEvent.Request.class);
        // do something with the request
        return new CloudFrontEvent.Response();
    }
}
