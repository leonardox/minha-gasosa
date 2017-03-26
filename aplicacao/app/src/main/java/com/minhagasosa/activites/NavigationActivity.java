package com.minhagasosa.activites;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.minhagasosa.LoginActivity;
import com.minhagasosa.R;
import com.minhagasosa.activites.maps.GasMapsActivity;
import com.minhagasosa.fragments.Home.HomeFragment;
import com.minhagasosa.fragments.comparison.ComparisonFragment;
import com.minhagasosa.fragments.expenditureplanning.ExpenditurePlanningFragment;
import com.minhagasosa.fragments.listroutes.ListRoutesFragment;
import com.minhagasosa.fragments.myCar.myCarFragment;
import com.minhagasosa.fragments.weeklydetailing.WeeklyDetailingFragment;
import com.minhagasosa.preferences.MinhaGasosaPreference;

public class NavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    private SharedPreferences sharedPreferences;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Minha gasosa");
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);

//        LinearLayout view = (LinearLayout)findViewById(R.id.teste);
        TextView mUse = (TextView) header.findViewById(R.id.user);
        //mUse.setText("sdsdsd")
        sharedPreferences = getSharedPreferences(LoginActivity.PREFERENCE_NAME, MODE_PRIVATE);
        mUse.setText(sharedPreferences.getString("USER_NOME", "usuário"));

        sharedPreferences = getSharedPreferences(LoginActivity.PREFERENCE_NAME, MODE_PRIVATE);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        Fragment fragment = new HomeFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        FragmentTransaction ft;
        Fragment fragment;
        switch (id) {
            case R.id.nav_home:
                fragment = new HomeFragment();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
                break;
            case R.id.nav_list_routes:
                fragment = new ListRoutesFragment();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
                break;
            case R.id.nav_set_planning:
                fragment = new ExpenditurePlanningFragment();
                ft = getSupportFragmentManager().beginTransaction();
                Bundle args = new Bundle();
                float dist = MinhaGasosaPreference.getDistanciaTotal(getApplicationContext());
                float consumo = MinhaGasosaPreference.getConsumoUrbanoPrimario(getApplicationContext());
                args.putFloat("distance", dist);
                args.putFloat("consumo", consumo);
                fragment.setArguments(args);
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
                break;
            case R.id.nav_set_car:
                fragment = new myCarFragment();
                ft = getSupportFragmentManager().beginTransaction();
                Bundle argsCar = new Bundle();
                argsCar.putBoolean("fromHome", true);
                fragment.setArguments(argsCar);
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
                break;
            case R.id.nav_menu_item_comparar:
                fragment = new ComparisonFragment();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
                break;
            case R.id.nav_menu_item_vantagem:
                showDialogVantagemCombustivel();
                break;
            case R.id.nav_gasStations:
                Intent intent = new Intent(this, GasMapsActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_menu_item_detalhamento_semanal:
                fragment = new WeeklyDetailingFragment();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
                break;
            case R.id.nav_logout:
                signOut();
                Intent i = new Intent(this, LoginActivity.class);
                i.putExtra("fromHome", true);
                startActivity(i);
                break;

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void signOut() {
        sharedPreferences = getSharedPreferences(LoginActivity.PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String login = sharedPreferences.getString(LoginActivity.USER_LOGIN, "");

        if (login.equals(LoginActivity.FACEBOOK_LOGIN)) {
            LoginManager.getInstance().logOut();
        } else if (login.equals(LoginActivity.GOOGLE_LOGIN)) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                        }
                    });
        }

        editor.putString(LoginActivity.USER_NOME, "");
        editor.putString(LoginActivity.USER_URL_PHOTO, "");
        editor.putString(LoginActivity.USER_EMAIL, "");
        editor.putString(LoginActivity.USER_ID, "0");
        editor.putBoolean(LoginActivity.USER_STATUS, false);
        editor.putString(LoginActivity.USER_LOGIN, LoginActivity.NO_SOCIAL_LOGIN);

        editor.apply();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void showDialogVantagemCombustivel() {
        AlertDialog.Builder builder = new  AlertDialog.Builder(this);
        builder.setTitle("Vantagem combustível");
        String combustivel = "";
        final float preco = MinhaGasosaPreference.getPrice(this);
        int resultado = 0;
        boolean isFlex = MinhaGasosaPreference.getCarroIsFlex(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.vantagem_dialog_layout, null);
        final EditText precoAlcoolEditText = (EditText) dialoglayout.findViewById(R.id.editTextAlcool);
        final TextView message = (TextView) dialoglayout.findViewById(R.id.mensagem);
        TextView messageAlcool = (TextView) dialoglayout.findViewById(R.id.mensagemAlcool);
        final Button buttonCalcular = (Button) dialoglayout.findViewById(R.id.buttonCalcular);
        if(isFlex){
            precoAlcoolEditText.setVisibility(View.GONE);
            messageAlcool.setVisibility(View.GONE);
            buttonCalcular.setVisibility(View.GONE);
            double precoGasolina = preco;
            double precoAlcool = MinhaGasosaPreference.getPrecoSecundario(getBaseContext());
            if (precoAlcool == 0) {
                calculaSemCombustivelSecundario(preco, precoAlcoolEditText, message, messageAlcool, buttonCalcular);
            } else {
                double calculoCombustivel = (precoAlcool/precoGasolina)*100;
                resultado = (int) calculoCombustivel;
                if(resultado <= 70){
                    combustivel += "Álcool";
                } else {
                    combustivel += "Gasolina";
                }
                message.setText("Baseado no preço do combustível inserido, é mais vantajoso utilizar " + combustivel);
            }

        } else {
            calculaSemCombustivelSecundario(preco, precoAlcoolEditText, message, messageAlcool,
                    buttonCalcular);
        }
        builder.setView(dialoglayout);
        builder.setPositiveButton(getString(android.R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        View view = NavigationActivity.this.getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager)getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                        dialog.dismiss();
                    }
                });
        final AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void calculaSemCombustivelSecundario(final float preco, final EditText precoAlcoolEditText, final TextView message, TextView messageAlcool, Button buttonCalcular) {
        precoAlcoolEditText.setVisibility(View.VISIBLE);
        messageAlcool.setVisibility(View.VISIBLE);
        buttonCalcular.setVisibility(View.VISIBLE);
        message.setText("Informe o valor do combustível secundário para calcular");
        buttonCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String combustivel = "";
                if (!precoAlcoolEditText.getText().toString().isEmpty()) {
                    double precoGasolina = preco;
                    if (preco == 0) {
                        Toast.makeText(getApplicationContext(), "Você precisa informar o valor da gasolina", Toast.LENGTH_SHORT).show();
                    } else {
                        double precoAlcool = Float.valueOf(precoAlcoolEditText.getText().toString());
                        double calculoCombustivel = (precoAlcool / precoGasolina) * 100;
                        int resultado = (int) calculoCombustivel;
                        if (resultado <= 70) {
                            combustivel += "Álcool";
                        } else {
                            combustivel += "Gasolina";
                        }
                        message.setText("Baseado no preço do combustível inserido, é mais vantajoso utilizar " + combustivel);
                    }
                }
            }
        });
        precoAlcoolEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    message.setText("Informe o valor do combustível secundário para calcular");
                }
            }
        });
    }
}
