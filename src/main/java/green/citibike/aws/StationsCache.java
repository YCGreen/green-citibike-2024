package green.citibike.aws;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import green.citibike.CitiService;
import green.citibike.CitiServiceFactory;
import green.citibike.json.StationInfo;
import green.citibike.json.Stations;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
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
    }

    public Stations<StationInfo> getStations() {
       boolean updated = updatedWithinHour();
      /*   if (response == null && updated) {
            return readFromS3();
        } else if (updatedWithinHour()) {
            return response;
        }

        updateLastModified();
        return response;


        if(response != null && updated) {
            return response;
        } */
        if(response != null && !updated) {
            updateLastModified();
        } else if(response == null && updated) {
            response = readFromS3();
        } else if(response == null && !updated) {
            updateLastModified();
        }

        return response;
    }

    private void updateLastModified() {
        Stations<StationInfo> stations = service.getStations().blockingGet();
        lastModified = Instant.now();
        writeToS3(stations);
    }

    private boolean updatedWithinHour() {
        HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                .bucket(BUCKET)
                .key(KEY)
                .build();

        try {
            HeadObjectResponse headObjectResponse = s3Client.headObject(headObjectRequest);
            lastModified = headObjectResponse.lastModified();
            return Duration.between(lastModified, Instant.now()).toHours() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    private void writeToS3(Stations<StationInfo> stations) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(BUCKET)
                .key(KEY)
                .build();

        String content = gson.toJson(stations);
        s3Client.putObject(putObjectRequest, RequestBody.fromString(content));
    }

    private Stations<StationInfo> readFromS3() {
        GetObjectRequest getObjectRequest = GetObjectRequest
                .builder()
                .bucket(BUCKET)
                .key(KEY)
                .build();

        InputStream in = s3Client.getObject(getObjectRequest);
        Type type = new TypeToken<Stations<StationInfo>>() {}.getType();
        return gson.fromJson(new InputStreamReader(in), type);
    }

}