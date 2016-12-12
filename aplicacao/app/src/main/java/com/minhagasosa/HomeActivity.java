package com.minhagasosa;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.github.mikephil.charting.charts.PieChart;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.minhagasosa.preferences.MinhaGasosaPreference;

import java.text.DecimalFormat;
import java.util.Calendar;

/**
 * Classe Inicial do app.
 */
public class HomeActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private String Home = "HomeActivity";
    private String R$ = "R$ ";
    /**
     * preco do combustivel primario
     */
    private EditText priceFuelEditText;
    /**
     * preco do combustivel secundario
     */
    private EditText priceFuelEditText2;
    /**
     * texto
     */
    private TextView secundarioText;
    /**
     * texto
     */
    private TextView secundarioPriceText;
    /**
     * marcador de flex no carro
     */
    private CheckBox checkFlex;
    /**
     * atribute
     */
    private TextView porcentagem1;
    /**
     * atribute
     */
    private TextView porcentagem2;
    /**
     * atribute
     */
    private TextView consumoS;
    /**
     * atribute
     */
    private TextView consumoM;
    /**
     * atribute
     */
    private TextView porcento1;
    /**
     * atribute
     */
    private TextView porcento2;
    /**
     * atribute
     */
    private Spinner spinnerPorcentagem1;
    /**
     * atribute
     */
    private Spinner spinnerPorcentagem2;
    /**
     * atribute
     */
    private ScrollView layoutMain;
    /**
     * string de porcentagens da capacidade do tanque do carro
     */
    private String[] porcento = {"0", "5", "10", "15", "20", "25", "30", "35", "40", "45", "50",
            "55", "60", "65", "70", "75", "80", "85", "90", "95", "100"};

    /**
     *
     */
    private static final int VALOR_MAXIMO_REQUEST = 101;
    /**
     * valor maximo que pode gastar
     */
    private float valorMaximoGastar;
    /**
     * atribute
     */
    private ChartView chartView;

    private PendingIntent pendingIntent;

    private GoogleApiClient mGoogleApiClient;
    private SharedPreferences sharedPreferences;

    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        secundarioText = (TextView) findViewById(R.id.textView8);
        secundarioText.setVisibility(View.GONE);
        secundarioPriceText = (TextView) findViewById(R.id.textView9);
        secundarioPriceText.setVisibility(View.GONE);
        priceFuelEditText2 = (EditText) findViewById(R.id.editText);
        priceFuelEditText2.setVisibility(View.GONE);
        porcentagem1 = (TextView) findViewById(R.id.textView10);
        porcentagem1.setVisibility(View.GONE);
        porcentagem2 = (TextView) findViewById(R.id.textView11);
        porcentagem2.setVisibility(View.GONE);
        consumoS = (TextView) findViewById(R.id.tv_consumo_semanal_valor);
        consumoM = (TextView) findViewById(R.id.tv_consumo_mensal_valor);
        porcento1 = (TextView) findViewById(R.id.textView2);
        porcento1.setVisibility(View.GONE);
        porcento2 = (TextView) findViewById(R.id.textView12);
        porcento2.setVisibility(View.GONE);
        porcentagem2 = (TextView) findViewById(R.id.textView11);
        spinnerPorcentagem1 = (Spinner) findViewById(R.id.spinner);
        spinnerPorcentagem1.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, porcento));
        spinnerPorcentagem1.setVisibility(View.GONE);
        spinnerPorcentagem2 = (Spinner) findViewById(R.id.spinner2);
        spinnerPorcentagem2.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, porcento));
        spinnerPorcentagem2.setVisibility(View.GONE);
        layoutMain = (ScrollView) findViewById(R.id.layout_main);

        sharedPreferences = getSharedPreferences(LoginActivity.PREFERENCE_NAME, MODE_PRIVATE);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {Intent intent = new Intent(HomeActivity.this, RoutesActivity.class);
                startActivity(intent);
            }
        });
        priceFuelEditText = (EditText) findViewById(R.id.editTextPrice);
        priceFuelEditText.setText(String.valueOf(MinhaGasosaPreference.getPrice(getApplicationContext())));
        priceFuelEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {
                //empty
            }
            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
                gerarPrevisao();
            }
            @Override
            public void afterTextChanged(final Editable s) {
                if (s.toString().isEmpty()) {
                    MinhaGasosaPreference.putPrice(0, getApplicationContext());
                } else {
                    MinhaGasosaPreference.putPrice(Float.valueOf(s.toString()),
                            getApplicationContext());
                }
                gerarPrevisao();
            }
        });
        priceFuelEditText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {
                //empty
            }
            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
                gerarPrevisao();
            }
            @Override
            public void afterTextChanged(final Editable s) {
                gerarPrevisao();
            }
        });
        spinnerPorcentagem1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
                gerarPrevisao();
            }
            @Override
            public void onNothingSelected(final AdapterView<?> parent) {
            }
        });
        spinnerPorcentagem2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
                gerarPrevisao();
            }
            @Override
            public void onNothingSelected(final AdapterView<?> parent) {
            }
        });
        checkFlex = (CheckBox) findViewById(R.id.checkBox);
        boolean isChecked = MinhaGasosaPreference.getCarroIsFlex(getApplicationContext());
        checkFlex.setChecked(isChecked);
        updateIsCheck(isChecked);
        checkFlex.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MinhaGasosaPreference.setCarroIsFlex(isChecked, getApplicationContext());
                updateIsCheck(isChecked);
            }
        });
        recuperaValorMaximo();
        PieChart pieChart = (PieChart) findViewById(R.id.chart);
        chartView = new ChartView(this, pieChart);
        startTheNotificationLauncher();
    }

    private void updateIsCheck(boolean isChecked) {
        if (isChecked) {
            secundarioText.setVisibility(View.VISIBLE);
            secundarioPriceText.setVisibility(View.VISIBLE);
            priceFuelEditText2.setVisibility(View.VISIBLE);
            porcentagem1.setVisibility(View.VISIBLE);
            porcentagem2.setVisibility(View.VISIBLE);
            spinnerPorcentagem1.setVisibility(View.VISIBLE);
            spinnerPorcentagem2.setVisibility(View.VISIBLE);
            porcento1.setVisibility(View.VISIBLE);
            porcento2.setVisibility(View.VISIBLE);
        } else {
            secundarioText.setVisibility(View.GONE);
            priceFuelEditText2.setText("");
            secundarioPriceText.setVisibility(View.GONE);
            priceFuelEditText2.setVisibility(View.GONE);
            porcentagem1.setVisibility(View.GONE);
            porcentagem2.setVisibility(View.GONE);
            spinnerPorcentagem1.setVisibility(View.GONE);
            spinnerPorcentagem2.setVisibility(View.GONE);
            porcento1.setVisibility(View.GONE);
            porcento2.setVisibility(View.GONE);
        }
    }

    /**
     * Método para criaçao de mecanismo para lançamento da notificação diária.
     */
    private void startTheNotificationLauncher() {
        Log.d(Home, "entrou no startTheNotificationLauncher");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(HomeActivity.this, MyReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(HomeActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) HomeActivity.this.getSystemService(HomeActivity.this.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    /**
     * Metodo que recupera o maximo maximo gasto
     */
    private void recuperaValorMaximo() {
        valorMaximoGastar = MinhaGasosaPreference.getValorMaximoParaGastar(HomeActivity.this);
    }

    @Override
    protected final void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (requestCode == VALOR_MAXIMO_REQUEST && resultCode == RESULT_OK) {
                valorMaximoGastar = MinhaGasosaPreference.getValorMaximoParaGastar(HomeActivity.this);
                addAvisoConsumo();

        }
    }

    /**
     * metodo que gera a previsao de gasto semanal e mensal gasto
     */
    private void gerarPrevisao() {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        float previsaoConsumoMensal = 0;
        if (!checkFlex.isChecked()) {
            float precoPrincipal = getPrecoPrincipal();
            float distancias = getDistanciaTotal();
            Log.d(Home, "Preço principal: " + precoPrincipal);
            Log.d(Home, "Distancias: " + distancias);

            float consumoUrbano = getConsumoUrbano();
            Log.d(Home, "Consumo: " + consumoUrbano);

            float valorPrevistoSemana = calculaPrevisaoSemanalNormal(precoPrincipal, distancias, consumoUrbano);
            previsaoConsumoMensal = calculaPrevisaoMesNorlmal(precoPrincipal, distancias, consumoUrbano);
            consumoS.setText(R$ + df.format(valorPrevistoSemana));
            consumoM.setText(R$ + df.format(previsaoConsumoMensal));
        } else {
            float precoPrincipal = getPrecoPrincipal();
            float precoSecundario = getPrecoSecundario();
            float distancias = getDistanciaTotal();
            Log.d(Home, "Distancias: " + distancias);
            float consumoUrbano = getConsumoUrbano();
            float consumoUrbanoSecundario = getConsumoUrbanoSecundario();
            int porcentagemPrincipal = getPorcentagemPrincipal();
            int porcentagemSecundaria = getPorcentagemSecundaria();
            float valorPrevistoSemanal = calculaPrevisaoSemanalNormal(porcentagemPrincipal,
                    porcentagemSecundaria, distancias, consumoUrbano, consumoUrbanoSecundario,
                    precoPrincipal, precoSecundario);
            previsaoConsumoMensal = calculaPrevisaoMensalFlex(porcentagemPrincipal,
                    porcentagemSecundaria, distancias, consumoUrbano, consumoUrbanoSecundario,
                    precoPrincipal, precoSecundario);

            consumoS.setText(R$ + df.format(valorPrevistoSemanal));
            consumoM.setText(R$ + df.format(previsaoConsumoMensal));
        }

        addAvisoConsumo(previsaoConsumoMensal);
    }

    /**
     * metodo que adiciona o  alerta caso o valor gasto seja maior do que o previsto mensal
     */
    private void addAvisoConsumo() {
        float previsaoConsumoMensal = 0;
        if (checkFlex.isChecked()) {
            // se for flex.
            previsaoConsumoMensal = calculaPrevisaoMensalFlex(getPorcentagemPrincipal(), getPorcentagemSecundaria(),
                    getDistanciaTotal(), getConsumoUrbano(), getConsumoUrbanoSecundario(),
                    getPrecoPrincipal(), getPrecoSecundario());
        } else {
            // se não for.
            previsaoConsumoMensal = calculaPrevisaoMesNorlmal(getPrecoPrincipal(), getDistanciaTotal(), getConsumoUrbano());
        }

        addAvisoConsumo(previsaoConsumoMensal);
    }

    /**
     * metodo que adiciona o aviso de consumo baseado na previsao mensal
     *
     * @param previsaoConsumoMensal
     */
    private void addAvisoConsumo(final float previsaoConsumoMensal) {
        if (previsaoConsumoMensal >= valorMaximoGastar) {
            mostraAviso(layoutMain, "Atenção! Você pode estar gastando mais do que " + valorMaximoGastar);
        }
    }

    /**
     * Exibe um aviso em uma snackbar.
     *
     * @param view     uma instancia da view principal.
     * @param mensagem a mensagem que se quer exibir.
     */
    private void mostraAviso(final View view, final String mensagem) {
        Snackbar snackbar = Snackbar
                .make(view, mensagem, Snackbar.LENGTH_INDEFINITE)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                    }
                });

        // Changing message text color
        snackbar.setActionTextColor(Color.YELLOW);

        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.RED);
        snackbar.show();
    }

    /**
     * metodo que pega o preco principla
     *
     * @return
     */
    private float getPrecoPrincipal() {
        if (!priceFuelEditText.getText().toString().isEmpty()) {
            return Float.parseFloat(priceFuelEditText.getText().toString());
        }
        return 0.0f;
    }

    /**
     * @return
     */
    private float getPrecoSecundario() {
        if (!priceFuelEditText2.getText().toString().isEmpty()) {
            return Float.parseFloat(priceFuelEditText2.getText().toString());
        }
        return 0.0f;
    }

    /**
     * @return
     */

    private float getDistanciaTotal() {
        return MinhaGasosaPreference.getDistanciaTotal(getApplicationContext());
    }

    /**
     * @return
     */
    private float getConsumoUrbano() {
        return MinhaGasosaPreference.
                getConsumoUrbanoPrimario(getApplicationContext());
    }

    /**
     * @return
     */
    private int getPorcentagemPrincipal() {
        return Integer.parseInt(
                spinnerPorcentagem1.getSelectedItem().toString());
    }

    /**
     * @return
     */
    private int getPorcentagemSecundaria() {
        return Integer.parseInt(
                spinnerPorcentagem2.getSelectedItem().toString());
    }

    /**
     * @return
     */

    private float calculaPrevisaoSemanalNormal(final float precoPrincipal, final float distancias, final float consumoUrbano) {
        return (distancias / consumoUrbano) * precoPrincipal;
    }

    /**
     * @return
     */
    private float calculaPrevisaoMesNorlmal(final float precoPrincipal, final float distancias, final float consumoUrbano) {
        return calculaPrevisaoSemanalNormal(precoPrincipal, distancias, consumoUrbano) * 4;
    }

    /**
     * @return
     */
    private float calculaPrevisaoSemanalNormal(final float porcentagemPrincipal, final float porcentagemSecundaria,
                                               final float distancias, final float consumoUrbano,
                                               final float consumoUrbanoSecundario, final float precoPrincipal,
                                               final float precoSecundario) {
        float result = 0.0f;
        if (porcentagemPrincipal != 0 || porcentagemSecundaria != 0) {
            float gastoPrincipal = (distancias / consumoUrbano) * precoPrincipal;
            float gastoSecundario = (distancias / consumoUrbanoSecundario) * precoSecundario;
            result = (((gastoPrincipal * porcentagemPrincipal) / 100) + ((gastoSecundario * porcentagemSecundaria) / 100));
            Log.d(Home, "consumoUrbano" + consumoUrbano);
            Log.d(Home, "consumoS" + consumoUrbanoSecundario);
            Log.d(Home, "result" + result);
        }
        return result;
    }

    /**
     * @return
     */
    public final float getConsumoUrbanoSecundario() {
        return MinhaGasosaPreference.
                getConsumoUrbanoSecundario(getApplicationContext());
    }

    /**
     * @param porcentagemPrincipal
     * @param porcentagemSecundaria
     * @param distancias
     * @param consumoUrbano
     * @param consumoUrbanoSecundario
     * @param precoPrincipal
     * @param precoSecundario
     * @return
     */
    private float calculaPrevisaoMensalFlex(final float porcentagemPrincipal, final float porcentagemSecundaria,
                                            final float distancias, final float consumoUrbano,
                                            final float consumoUrbanoSecundario, final float precoPrincipal,
                                            final float precoSecundario) {

        return calculaPrevisaoSemanalNormal(porcentagemPrincipal,
                porcentagemSecundaria, distancias, consumoUrbano, consumoUrbanoSecundario,
                precoPrincipal, precoSecundario) * 4;
    }

    @Override
    public final boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public final boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == R.id.set_car) {
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra("fromHome", true);
            startActivity(i);
        } else if (item.getItemId() == R.id.set_planning) {
            float dist = getDistanciaTotal();
            float consumo = getConsumoUrbano();
            Intent intent = new Intent(this, PlanningActivity.class);
            intent.putExtra("distance", dist);
            intent.putExtra("consumo", consumo);
            startActivityForResult(intent, VALOR_MAXIMO_REQUEST);
        } else if (item.getItemId() == R.id.menu_item_comparar) {
            Intent intent = new Intent(this, ComparaGastosActivity.class);
            startActivity(intent);
        } else if(item.getItemId() == R.id.list_routes){
            Intent intent = new Intent(this, ListRoutesActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.menu_item_vantagem) {
            showDialogVantagemCombustivel();
        } else if(item.getItemId() == R.id.menu_logout){
            signOut();

            Intent mainIntent = new Intent(HomeActivity.this,LoginActivity.class);
            startActivity(mainIntent);
            finish();
        }
        return super.onOptionsItemSelected(item);
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
            double precoAlcool = getPrecoSecundario();
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
                        View view = HomeActivity.this.getCurrentFocus();
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

    @Override
    protected final void onResume() {
        super.onResume();
        chartView.iniciaDistancias();
    }

    private void signOut() {
        sharedPreferences = getSharedPreferences(LoginActivity.PREFERENCE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String login = sharedPreferences.getString(LoginActivity.USER_LOGIN, "");

        if (login.equals(LoginActivity.FACEBOOK_LOGIN)) {
            LoginManager.getInstance().logOut();
        } else if (login.equals(LoginActivity.GOOGLE_LOGIN)){
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {}
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
}
