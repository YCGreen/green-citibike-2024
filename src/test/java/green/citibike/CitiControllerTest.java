package green.citibike;

import green.citibike.json.StationStatus;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CitiControllerTest {

    @Test
    public void test() {
        CitiService service = new CitiServiceFactory().getService();
        CitiController controller = new CitiController(service);

        List<StationStatus> stationStatuses = controller.getStationStatus();
        String stationStatusStr = stationStatuses.get(0).toString();

        String expected = "Longitude: -74.009441\n" +
                "Latitude: 40.642703\n" +
                "Name: 52 St & 6 Ave\n" +
                "StationID: 816e50eb-dc4b-47dc-b773-154e2020cb0d\n" +
                "IsRenting: 0\n" +
                "NumBikes: 0\n" +
                "NumDocks: 0\n";

        assertEquals(expected, stationStatusStr);
    }
}
