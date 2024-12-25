package green.citibike;

import green.citibike.json.StationInfo;
import green.citibike.json.Stations;
import green.citibike.json.StatusInfo;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CitiServiceTest {

    @Test
    void getStation() {
        //given
        CitiService service = new CitiServiceFactory().getService();

        //when
        Stations<StationInfo> stations = service.getStations().blockingGet();

        //then
        assertNotNull(stations.data.stations.get(0).station_id);
    }

    @Test
    void getStatus() {
        CitiService service = new CitiServiceFactory().getService();

        Stations<StatusInfo> stations = service.getStatus().blockingGet();

        assertNotNull(stations.data.stations.get(0).station_id);
    }
}
