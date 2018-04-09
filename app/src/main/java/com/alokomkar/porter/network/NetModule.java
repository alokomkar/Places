package com.alokomkar.porter.network;

import android.app.Application;
import android.support.annotation.NonNull;

import com.alokomkar.porter.PorterApplication;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Alok on 09/04/18.
 */

public class NetModule {

    private PorterApplication application;

    private Cache cache;
    private OkHttpClient okHttpClient;
    private Retrofit retrofit;
    private String SERVER_URL = "https://assignment-mobileapi.porter.in/";
    private String BASE_URL = "http://maps.googleapis.com/maps/api/geocode/";

    // Constructor needs one parameter to instantiate.
    public NetModule(PorterApplication application) {
        this.application = application;
    }

    private Cache provideOkHttpCache(Application application) {
        int cacheSize = 10 * 1024 * 1024; // 10 MB
        return new Cache(application.getCacheDir(), cacheSize);
    }

    private OkHttpClient provideOkHttpClient(Cache cache) {
        // create an instance of OkLogInterceptor using a builder()
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(getInterceptor());
        client.cache(cache);
        return client.build();
    }

    private Interceptor getInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                if (application.isNetworkAvailable()) {
                    int maxAge = 60; // read from cache for 1 minute
                    return originalResponse.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .build();
                } else {
                    int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                    return originalResponse.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .build();
                }
            }
        };
    }

    private Retrofit provideRetrofit(OkHttpClient okHttpClient, String url) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(url)
                .client(okHttpClient)
                .build();
    }

    private Cache getCache() {
        if( cache == null ) {
            cache = provideOkHttpCache(application);
        }
        return cache;
    }

    private OkHttpClient getOkHttpClient() {
        if( okHttpClient == null ) {
            okHttpClient = provideOkHttpClient(getCache());
        }
        return okHttpClient;
    }

    private Retrofit getRetrofit( String serverBaseUrl ) {
        if( retrofit == null ) {
            retrofit = provideRetrofit(getOkHttpClient(), serverBaseUrl);
        }
        return retrofit;
    }

    private Gson getGson() {
        return new GsonBuilder()
                .setLenient()
                .create();
    }

    public ReverseGeoCodeApi getReverseGeoCodeAPI() {
        return getRetrofit( BASE_URL ).create(ReverseGeoCodeApi.class);
    }

    public ServerAPI getServerAPI() {
        return getRetrofit( SERVER_URL ).create(ServerAPI.class);
    }
}
