package green.citibike.aws;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import green.citibike.CitiService;
import green.citibike.CitiServiceFactory;
import green.citibike.json.StationInfo;
import green.citibike.json.Stations;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.time.Duration;
import java.time.Instant;

public class StationsCache {
    private static final String KEY = "station_information.json";
    private static final String BUCKET = "green.citibike";
    private Stations<StationInfo> response;
    private Instant lastModified;
    private Gson gson = new Gson();
    private S3Client s3Client;
    private CitiService service = new CitiServiceFactory().getService();

    public StationsCache() {
        s3Client = S3Client.builder()
                .region(Region.US_EAST_2)
                .build();

        response = readFromS3();
        resetLastModified();
    }

    public Stations<StationInfo> getStations() {
       boolean updatedWithinHour = updatedWithinHour();

       if (!updatedWithinHour) {
           downloadStations();
       }

       return response;
    }

    private void resetLastModified() {
        HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                .bucket(BUCKET)
                .key(KEY)
                .build();
        try {
            HeadObjectResponse headObjectResponse = s3Client.headObject(headObjectRequest);
            lastModified = headObjectResponse.lastModified();
        } catch (Exception ignored) {

        }
    }

    private void downloadStations() {
        response = service.getStations().blockingGet();
        lastModified = Instant.now();
        writeToS3(response);
    }

    private boolean updatedWithinHour() {
        if (lastModified == null) {
            return false;
        }

        return Duration.between(lastModified, Instant.now()).toHours() > 0;
    }

    private void writeToS3(Stations<StationInfo> stations) {
        try {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(BUCKET)
                .key(KEY)
                .build();

        String content = gson.toJson(stations);
        s3Client.putObject(putObjectRequest, RequestBody.fromString(content));
        } catch (Exception ignored) {
        }
    }

    private Stations<StationInfo> readFromS3() {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest
                    .builder()
                    .bucket(BUCKET)
                    .key(KEY)
                    .build();

            InputStream in = s3Client.getObject(getObjectRequest);
            Type type = new TypeToken<Stations<StationInfo>>() {}.getType();
            return gson.fromJson(new InputStreamReader(in), type);
        } catch (Exception ignored) {
        }

        return null;
    }

}