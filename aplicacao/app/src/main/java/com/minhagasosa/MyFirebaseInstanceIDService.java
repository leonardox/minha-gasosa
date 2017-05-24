package com.minhagasosa;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.minhagasosa.API.GasStationService;
import com.minhagasosa.API.UsersService;
import com.minhagasosa.activites.EndpointFactory;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by elyervesson on 22/05/17.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    public static final String FIREBASE_TOKEN = "FIREBASE_TOKEN";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("Firebase", "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        // sendRegistrationToServer(refreshedToken);
        Retrofit retrofit  = EndpointFactory.buildEndpoint(getBaseContext());
        UsersService usersService = retrofit.create(UsersService.class);
        usersService.updateUserToken(refreshedToken).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("Firebase", "Token updated on server");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Firebase", "Error updating token updated on server");
            }
        });
        storeToken(refreshedToken);
    }

    private void storeToken(String token) {
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FIREBASE_TOKEN, token);

        editor.apply();
    }
}
