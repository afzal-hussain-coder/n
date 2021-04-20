package com.pb.criconet.retrofit;

import com.pb.criconet.BuildConfig;
import com.pb.criconet.Utills.Global;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiInterfaceService {

    private static String URL = Global.URL2;

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
            .readTimeout(440, TimeUnit.SECONDS)
            .writeTimeout(440, TimeUnit.SECONDS)
            .connectTimeout(440, TimeUnit.SECONDS);

    private static HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();

    private static OkHttpClient.Builder getHttpClient() {
        if (BuildConfig.DEBUG) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
//            httpClient.addInterceptor(new LogInterceptor());
            httpClient.addInterceptor(interceptor);
        } else {
            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
            httpClient.addInterceptor(interceptor);
        }
        return httpClient;
    }

    private static Retrofit retrofit;

    public static VideoInterface getApiService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getHttpClient().build())
                    .build();
        }
        return retrofit.create(VideoInterface.class);
    }

}
