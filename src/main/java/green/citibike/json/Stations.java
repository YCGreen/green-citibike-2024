package green.citibike.json;

import java.util.List;

public class Stations<T> {
    public Data<T> data;

    public class Data<T> {
        public List<T> stations;
    }
}
