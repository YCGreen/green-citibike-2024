package green.citibike.aws;

import green.citibike.CitiService;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class LambdaServiceFactory {
    public LambdaService getService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://s47env2ki9.execute-api.us-east-2.amazonaws.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();

        return retrofit.create(LambdaService.class);
    }
}
