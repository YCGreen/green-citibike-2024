package green.citibike;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import green.citibike.aws.CitiRequestHandler;
import green.citibike.aws.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CitiRequestHandlerTest {

    @Test
    void requestHandler() {
        String json = """
        {
          "from": {
            "lat": 40.8211,
            "lon": -73.9359
          },
          "to": {
            "lat": 40.7190,
            "lon": -73.9585
          }
        }
        """;

        APIGatewayProxyRequestEvent event = new APIGatewayProxyRequestEvent();
        event.setBody(json);

        CitiRequestHandler handler = new CitiRequestHandler();

        Response response = handler.handleRequest(event, null);

        assertNotNull(response.getEnd());
    }
}
