package com.minhagasosa.activites;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.minhagasosa.LoginActivity;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by GAEL on 11/01/2017.
 */

public class EndpointFactory {

    public static Retrofit buildEndpoint(Context context){

        SharedPreferences sharedPref = context.getSharedPreferences(BaseActivity.PREFERENCE_NAME, context.MODE_PRIVATE);
        final String token = sharedPref.getString("AUTH_TOKEN", "lol");

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();

                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .header("x-access-token", token); // <-- this is the important line

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        OkHttpClient client = httpClient.build();
        return new Retrofit.Builder()
                .baseUrl("https://minha-gasosa-p1.herokuapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }
}
