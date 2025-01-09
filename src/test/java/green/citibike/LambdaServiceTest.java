package green.citibike;

import green.citibike.aws.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LambdaServiceTest {

    Request request = new Request(new Coordinate(70, 40), new Coordinate(71, 40));
    LambdaService service = new LambdaServiceFactory().getService();
    Response response = service.getStations(request).blockingGet();

    @Test
    void getResponse() {
       assertNotNull(response.getFrom());
    }

}
