package com.minhagasosa;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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
import com.minhagasosa.API.LocationService;
import com.minhagasosa.API.UsersService;
import com.minhagasosa.Transfer.City;
import com.minhagasosa.Transfer.State;
import com.minhagasosa.Transfer.TUser;
import com.minhagasosa.activites.BaseActivity;
import com.minhagasosa.activites.NavigationActivity;
import com.minhagasosa.fragments.Home.HomeFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

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
    UsersService m_usersService;
    LocationService m_locationService;


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
                        signInFacebookButton.setVisibility(View.VISIBLE);
                    }
                    signInGoogleButton.setVisibility(SignInButton.VISIBLE);
                }

                if (haveFacebook()) {
                    configFacebookTrackers();
                }
            }
        }
        m_usersService = retrofit.create(UsersService.class);
        m_locationService = retrofit.create(LocationService.class);
    }

    private void skipSplash() {
        signInGoogleButton.setVisibility(SignInButton.INVISIBLE);
        signInFacebookButton.setVisibility(View.VISIBLE);
        updateUI();
    }

    private void updateUI() {
        if (!isActivityRunning(NavigationActivity.class) && !closed) {
            startActivity(new Intent(this, NavigationActivity.class));
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

            Toast.makeText(LoginActivity.this, "Usuario " + email + ": logado", Toast.LENGTH_SHORT).show();
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

    private void configFacebookTrackers() {
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                //setFacebookProfile(currentProfile);
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

            Toast.makeText(LoginActivity.this, "Usuario " + name + ": logado", Toast.LENGTH_SHORT).show();

            updateUI();
        }
    }

    private void register(String cityId) {
        Profile fbProfile = Profile.getCurrentProfile();
        TUser us = new TUser();
        us.setFirstName(fbProfile.getFirstName());
        us.setLastName(fbProfile.getLastName());
        us.setFbId(fbProfile.getId());
        us.setCity(cityId);
        m_usersService.registerUser(us).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                switch (response.code()) {
                    case 201:
                        Log.d("Reg", "Usuario registrando, logando...");
                        authInServer(AccessToken.getCurrentAccessToken().getToken());
                        break;
                    default:
                        Log.e("Err", "Não foi possível criar o novo usuário");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {

            }
        });
    }

    private void authInServer(String fbAuthToken) {
        m_usersService.Auth(fbAuthToken).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                switch (response.code()) {
                    case 200:
                        try {
                            Log.d("Login", "Usuario autênticado com token: ");
                            SharedPreferences sharedPref = getSharedPreferences(BaseActivity.PREFERENCE_NAME, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("AUTH_TOKEN", response.body().string());
                            editor.commit();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        logado = true;
                        setFacebookProfile(Profile.getCurrentProfile());
                        break;
                    case 403:
                        Log.d("Reg", "Usuario ainda não registrado, registrando...");
                        showSelectLocationDialog();
                        //TODO Move to location selection
                        //register("");
                        break;
                    default:
                        Log.e("Err", "Erro de autenticação");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                System.out.print(throwable.toString());
            }
        });

    }

    private void showSelectLocationDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_select_location);
        dialog.setTitle("Seleciona sua localização");

        final Spinner stateSpinner = (Spinner) dialog.findViewById(R.id.spinner_select_state);
        final Spinner citySpinner = (Spinner) dialog.findViewById(R.id.spinner_select_city);
        final Button confirmbutton = (Button) dialog.findViewById(R.id.button_confirm_location);
        confirmbutton.setEnabled(false);
        final ArrayAdapter citiesAdapter = new ArrayAdapter<City>(dialog.getContext(),
                android.R.layout.simple_spinner_item, new ArrayList<City>());
        citySpinner.setAdapter(citiesAdapter);
        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                State selectedState = (State) stateSpinner.getSelectedItem();
                Log.d("state", "StateId: " + selectedState.getId());
                m_locationService.getCities(selectedState.getId()).enqueue(new Callback<List<City>>() {
                    @Override
                    public void onResponse(Call<List<City>> call, Response<List<City>> response) {
                        List<City> cities = response.body();
                        Log.d("Citites", "Cities Size: " + cities.size());

                        citiesAdapter.clear();
                        citiesAdapter.addAll(cities);
                        citiesAdapter.notifyDataSetChanged();

                        confirmbutton.setEnabled(true);
                    }

                    @Override
                    public void onFailure(Call<List<City>> call, Throwable t) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        m_locationService.getStates().enqueue(new Callback<List<State>>() {
            @Override
            public void onResponse(Call<List<State>> call, Response<List<State>> response) {
                List<State> statesList = response.body();

                stateSpinner.setAdapter(new ArrayAdapter<State>(dialog.getContext(),
                        android.R.layout.simple_spinner_item, statesList));
            }

            @Override
            public void onFailure(Call<List<State>> call, Throwable t) {

            }
        });

        confirmbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                City selectedCity = (City) citySpinner.getSelectedItem();
                register(selectedCity.getId());
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private boolean haveFacebook() {
        try {
            ApplicationInfo info = getPackageManager().
                    getApplicationInfo("com.facebook.katana", 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

//    protected Boolean isActivityRunning(Fragment fragmentClass) {
////        FragmentManager fragmentManager = (FragmentManager)getApplicationContext().getSystemService(Context.FA);
//        FragmentManager fragmentManager = (FragmentManager) getBaseContext().getSystemService(ACTIVITY_SERVICE);
//
//        List<FragmentManager.BackStackEntry> tasks = (List<FragmentManager.BackStackEntry>) fragmentManager.getBackStackEntryAt(Integer.MAX_VALUE);
//        //List<FragmentManager.BackStackEntry> tasks = fragmentManager.getBackStackEntryAt(Integer.MAX_VALUE);
//
//        for (FragmentManager.BackStackEntry task : tasks) {
//            if (fragmentClass.getActivity().equals(task.getClass().getName())) {
//                return true;
//            }
//        }


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
        final Activity self = this;
        signInFacebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(self, Arrays.asList("public_profile", "user_friends", "email"));
                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        authInServer(AccessToken.getCurrentAccessToken().getToken());
//                        logado = true;
//                        setFacebookProfile(Profile.getCurrentProfile());
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException error) {

                    }
                });
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
                    if (haveFacebook()) {
                        signInFacebookButton.setVisibility(View.VISIBLE);
                    } else {
                        signInFacebookButton.setVisibility(View.VISIBLE);
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