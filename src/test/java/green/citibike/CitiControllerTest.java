package green.citibike;

import green.citibike.json.StationInfo;
import green.citibike.json.StationStatus;
import green.citibike.json.Stations;
import green.citibike.json.StatusInfo;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CitiControllerTest {

    CitiService service = new CitiServiceFactory().getService();
    Stations<StationInfo> stations = service.getStations().blockingGet();
    Stations<StatusInfo> statuses = service.getStatus().blockingGet();
    CitiController controller = new CitiController(stations, statuses);

    @Test
    public void test() {
        List<StationStatus> stationStatuses = controller.getStationStatus();
        String stationStatusStr = stationStatuses.get(0).toString();

        assertNotNull(stationStatusStr);
    }

    @Test
    public void testGetNearestBike() {
        StationStatus stationStatus = controller.findClosestStationWithBike(-74, 40);
        assertNotNull(stationStatus);
    }


}
