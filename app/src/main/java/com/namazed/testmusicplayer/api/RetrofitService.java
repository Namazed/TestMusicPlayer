package com.namazed.testmusicplayer.api;

import com.google.gson.GsonBuilder;
import com.namazed.testmusicplayer.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import timber.log.Timber;

public class RetrofitService {

    private static final String BASE_URL = "https://itunes.apple.com/";


    private final OkHttpClient httpClient;
    private final GsonConverterFactory gsonConverterFactory;
    private final Retrofit client;

    public RetrofitService() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(message ->
                Timber.tag("OkHttp").d(message));
        if (BuildConfig.DEBUG) {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        }
        httpClient = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request.Builder ongoing = chain.request().newBuilder();
                    return chain.proceed(ongoing.build());
                })
                .addInterceptor(loggingInterceptor)
                .build();

        gsonConverterFactory = GsonConverterFactory.create(
                new GsonBuilder()
                        .registerTypeAdapterFactory(AutoValueGsonTypeAdapterFactory.create())
                        .create());

        client = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient)
                .build();
    }

    public <T> T createRetrofitClient(final Class<T> service) {
        return client.create(service);
    }
}
