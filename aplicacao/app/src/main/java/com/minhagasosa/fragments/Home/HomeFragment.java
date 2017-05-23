package com.minhagasosa.fragments.Home;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.minhagasosa.ChartView;
import com.minhagasosa.MyReceiver;
import com.minhagasosa.NewRefuelActivity;
import com.minhagasosa.R;
import com.minhagasosa.RoutesActivity;
import com.minhagasosa.fragments.carsettings.CarSettingsFragment;
import com.minhagasosa.preferences.MinhaGasosaPreference;

import java.text.DecimalFormat;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {


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



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home, container, false);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        secundarioText = (TextView) view.findViewById(R.id.textView8);
        secundarioText.setVisibility(View.GONE);
        secundarioPriceText = (TextView) view.findViewById(R.id.textView9);
        secundarioPriceText.setVisibility(View.GONE);
        priceFuelEditText2 = (EditText) view.findViewById(R.id.editText);
        priceFuelEditText2.setVisibility(View.GONE);
        porcentagem1 = (TextView) view.findViewById(R.id.textView10);
        porcentagem1.setVisibility(View.GONE);
        porcentagem2 = (TextView) view.findViewById(R.id.textView11);
        porcentagem2.setVisibility(View.GONE);
        consumoS = (TextView) view.findViewById(R.id.tv_consumo_semanal_valor);
        consumoM = (TextView) view.findViewById(R.id.tv_consumo_mensal_valor);
        porcento1 = (TextView) view.findViewById(R.id.textView2);
        porcento1.setVisibility(View.GONE);
        porcento2 = (TextView) view.findViewById(R.id.textView12);
        porcento2.setVisibility(View.GONE);
        porcentagem2 = (TextView) view.findViewById(R.id.textView11);
        spinnerPorcentagem1 = (Spinner) view.findViewById(R.id.spinner);
        spinnerPorcentagem1.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, porcento));
        spinnerPorcentagem1.setVisibility(View.GONE);
        spinnerPorcentagem2 = (Spinner) view.findViewById(R.id.spinner2);
        spinnerPorcentagem2.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, porcento));
        spinnerPorcentagem2.setVisibility(View.GONE);
        layoutMain = (ScrollView) view.findViewById(R.id.layout_main);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RoutesActivity.class);
                startActivity(intent);
            }
        });

        boolean firstTime = MinhaGasosaPreference.getFirstTime(getContext());
        if(firstTime) {
            MinhaGasosaPreference.setFirstTime(false , getContext());
            Fragment fragment;
            FragmentTransaction ft;
            fragment = new CarSettingsFragment();
            ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }




        priceFuelEditText = (EditText) view.findViewById(R.id.editTextPrice);
        priceFuelEditText.setText(String.valueOf(MinhaGasosaPreference.getPrice(getContext())));
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
                if ((s.toString().isEmpty()) || (s.toString().equals("."))) {
                    MinhaGasosaPreference.putPrice(0, getContext());
                } else {
                    MinhaGasosaPreference.putPrice(Float.valueOf(s.toString()),
                            getContext());
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
        checkFlex = (CheckBox) view.findViewById(R.id.checkBox);
        boolean isChecked = MinhaGasosaPreference.getCarroIsFlex(getContext());
        checkFlex.setChecked(isChecked);
        updateIsCheck(isChecked);
        checkFlex.setVisibility(View.GONE);
        checkFlex.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MinhaGasosaPreference.setCarroIsFlex(isChecked, getContext());
                updateIsCheck(isChecked);
            }
        });
        recuperaValorMaximo();
        PieChart pieChart = (PieChart) view.findViewById(R.id.chart);
        chartView = new ChartView(getContext(), pieChart);
        startTheNotificationLauncher();

        View viewKeyboard = getActivity().getCurrentFocus();
        if (viewKeyboard != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        return view;
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

        Intent intent = new Intent(getActivity(), MyReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) getActivity().getSystemService(getActivity().ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    /**
     * Metodo que recupera o maximo maximo gasto
     */
    private void recuperaValorMaximo() {
        valorMaximoGastar = MinhaGasosaPreference.getValorMaximoParaGastar(getActivity());
        Log.e("valorMAXIMOOO", String.valueOf(valorMaximoGastar));
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
            Log.e("valorPrevisaoSemanal ", String.valueOf(valorPrevistoSemana));
            previsaoConsumoMensal = calculaPrevisaoMesNorlmal(precoPrincipal, distancias, consumoUrbano);
            Log.e("valorPrevisaoMensa  ", String.valueOf(previsaoConsumoMensal));

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
        recuperaValorMaximo();
        if (previsaoConsumoMensal >= valorMaximoGastar) {
            Log.e("Valor Planejamento ", String.valueOf(valorMaximoGastar));
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
        if (!(priceFuelEditText.getText().toString().isEmpty()) && !(priceFuelEditText.getText().toString().equals(".")) ) {
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
        float f = MinhaGasosaPreference.getDistanciaTotal(getContext());
                Log.e("Distancia totaaaall ", String.valueOf(f));

        return MinhaGasosaPreference.getDistanciaTotal(getContext());
    }

    /**
     * @return
     */
    private float getConsumoUrbano() {
        float f = MinhaGasosaPreference.
                getConsumoUrbanoPrimario(getContext());
        Log.e("Consumo Urbanooooo ", String.valueOf(f));
        return MinhaGasosaPreference.
                getConsumoUrbanoPrimario(getContext());
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
                getConsumoUrbanoSecundario(getContext());
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
    public final void onResume() {
        super.onResume();
        chartView.iniciaDistancias();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

}
