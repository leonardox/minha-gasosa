package com.minhagasosa;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

import java.util.Arrays;
import java.util.List;


public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    public static final String PREFERENCE_NAME = "USER_PREFERENCE";
    public static final String USER_NOME = "USER_NOME";
    public static final String USER_EMAIL = "USER_EMAIL";
    public static final String USER_ID = "USER_ID";
    public static final String USER_URL_PHOTO = "USER_URL_PHOTO";
    public static final String USER_STATUS = "USER_STATUS";
    public static final String FACEBOOK_LOGIN = "FACEBOOK_LOGIN";
    public static final String GOOGLE_LOGIN = "GOOGLE_LOGIN";
    public static final String NO_SOCIAL_LOGIN = "NO_SOCIAL_LOGIN";
    public static final String USER_LOGIN = "NO_SOCIAL_LOGIN";

    private static final int RC_SIGN_IN = 9001;
    private static final int SIGN_IN_CODE = 56465;
    private static final int SIGN_IN_FACEBOOK = 64206;

    private static final String TAG = "SplashActivity";
    private GoogleApiClient mGoogleApiClient;
    private CallbackManager callbackManager;
    private boolean logado;
    private ProfileTracker profileTracker;
    private AccessTokenTracker accessTokenTracker;
    private SignInButton signInGoogleButton;
    private LoginButton signInFacebookButton;
    private String tipoAcesso;
    private boolean skip = false;
    private boolean closed = false;
    private boolean isGoogleButtonClicked;
    private boolean isConsentScreenOpened;
    private boolean tryLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        configLoginFacebook();
        configLoginGoogle();

        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        signInGoogleButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInGoogleButton.setOnClickListener(this);

        signInFacebookButton = (LoginButton) findViewById(R.id.login_button);


        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);

        logado = sharedPreferences.getBoolean(USER_STATUS, false);
        final String tipoAcesso = sharedPreferences.getString(USER_LOGIN, "");
        this.tipoAcesso = tipoAcesso;

        if (logado) {
            skipSplash();
        } else {
            if (!skip) {
                skip = true;
                if (logado) {
                    skipSplash();
                } else {
                    if (haveFacebook()) {
                        signInFacebookButton.setVisibility(View.VISIBLE);
                    } else {
                        signInFacebookButton.setVisibility(View.GONE);
                    }
                    signInGoogleButton.setVisibility(SignInButton.VISIBLE);
                }

                if (haveFacebook()) {
                    configFacebookTrackers();
                }
            }}
    }

    private void skipSplash(){
        signInGoogleButton.setVisibility(SignInButton.INVISIBLE);
        signInFacebookButton.setVisibility(View.INVISIBLE);
        updateUI();
    }

    private void updateUI() {
        if (!isActivityRunning(MainActivity.class) && !closed) {
            startActivity(new Intent(this, MainActivity.class));
        }
        closed = true;
        finish();
    }

    // GOOGLE METHODS
    private void configLoginGoogle() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    private void signInGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        isConsentScreenOpened = true;
    }

    private void handleSignInGoogleResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInGoogleResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            String nome = acct.getDisplayName();
            String email = acct.getEmail();
            Uri foto_url = acct.getPhotoUrl();

            SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString(USER_NOME, nome);
            editor.putString(USER_EMAIL, email);
            editor.putString(USER_LOGIN, GOOGLE_LOGIN);

            Toast.makeText(LoginActivity.this, email, Toast.LENGTH_SHORT).show();
            if (foto_url != null) {
                editor.putString(USER_URL_PHOTO, foto_url.toString());
            } else {
                editor.putString(USER_URL_PHOTO, null);
            }

            editor.apply();

            updateUI();
        }
    }

    // FACEBOOK METHODS
    private void configLoginFacebook() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
    }

    private void configFacebookTrackers(){
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                setFacebookProfile(currentProfile);
            }
        };

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                // On AccessToken changes fetch the new profile which fires the event on
                // the ProfileTracker if the profile is different
                Profile.fetchProfileForCurrentAccessToken();
            }
        };

        // Ensure that our profile is up to date
        Profile.fetchProfileForCurrentAccessToken();
        setFacebookProfile(Profile.getCurrentProfile());
    }

    private void setFacebookProfile(Profile profile) {
        if (profile != null) {

            SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            String pessoaId = profile.getId();
            String name = profile.getName();

            editor.putString(USER_ID, pessoaId);
            editor.putString(USER_NOME, name);
            //editor.putString(USER_EMAIL, email);
            editor.putBoolean(USER_STATUS, true);
            editor.putString(USER_LOGIN, FACEBOOK_LOGIN);

            editor.apply();

            Toast.makeText(LoginActivity.this, name, Toast.LENGTH_SHORT).show();

            updateUI();
        }
    }


    private void handleSignInFacebookResult(){
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends", "email"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                logado = true;
                setFacebookProfile(Profile.getCurrentProfile());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    private boolean haveFacebook(){
        try{
            ApplicationInfo info = getPackageManager().
                    getApplicationInfo("com.facebook.katana", 0 );
            return true;
        } catch( PackageManager.NameNotFoundException e ){
            return false;
        }
    }

    protected Boolean isActivityRunning(Class activityClass) {
        ActivityManager activityManager = (ActivityManager) getBaseContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

        for (ActivityManager.RunningTaskInfo task : tasks) {
            if (activityClass.getCanonicalName().equalsIgnoreCase(task.baseActivity.getClassName())) {
                return true;
            }
        }

        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();

        signInFacebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSignInFacebookResult();
            }
        });

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInGoogleResult(result);
        } else {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    handleSignInGoogleResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        closed = true;
        if (profileTracker != null && accessTokenTracker != null) {
            profileTracker.stopTracking();
            accessTokenTracker.startTracking();
        }

        //OnStop
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onClick(View v) {
        signInGoogle();
        isGoogleButtonClicked = true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SIGN_IN_CODE) {
            isConsentScreenOpened = false;

            if (resultCode != RESULT_OK) {
                isGoogleButtonClicked = false;
            }

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        } else if (requestCode == SIGN_IN_FACEBOOK) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        } else if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInGoogleResult(result);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (!logado) {
            if (!connectionResult.hasResolution()) {
                GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), LoginActivity.this, 0).show();
                return;
            }

            if (!isConsentScreenOpened) {
                if (tryLogin) {
                    signInGoogleButton.setVisibility(SignInButton.VISIBLE);
                    if(haveFacebook()){
                        signInFacebookButton.setVisibility(View.VISIBLE);
                    } else {
                        signInFacebookButton.setVisibility(View.INVISIBLE);
                    }
                }

                if (isGoogleButtonClicked) {
                    signInGoogle();
                    tryLogin = true;
                }

            }
        }
    }
}