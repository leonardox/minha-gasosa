package com.minhagasosa;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by elyervesson on 22/05/17.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    public final String FIREBASE_TOKEN = "FIREBASE_TOKEN";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("Firebase", "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        // sendRegistrationToServer(refreshedToken);
        storeToken(refreshedToken);
    }

    private void storeToken(String token) {
        SharedPreferences sharedPreferences = getSharedPreferences(LoginActivity.PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FIREBASE_TOKEN, token);

        editor.apply();
    }
}
