package com.minhagasosa;

import android.app.job.JobParameters;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.minhagasosa.dao.Carro;
import com.minhagasosa.dao.DaoMaster;
import com.minhagasosa.dao.DaoSession;
import com.minhagasosa.dao.Modelo;
import com.minhagasosa.dao.Rota;
import com.minhagasosa.dao.RotaDao;
import com.minhagasosa.preferences.MinhaGasosaPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoutesActivity extends AppCompatActivity {
    private CheckBox checkRepeat;
    private CheckBox checkBoxRoute;
    private TextInputLayout distanceGoingWrapper;
    private TextInputLayout distanceBackWrapper;
    private TextInputLayout timesRouteWrapper;
    private TextInputLayout routeTitleWrapper;
    private static final String TAG_ROUTES_ACTIVITY = "RoutesActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initializeLayout();
    }

    private void initializeLayout() {
        distanceGoingWrapper = (TextInputLayout) findViewById(R.id.distance_going_wrapper);
        distanceBackWrapper = (TextInputLayout) findViewById(R.id.distance_comeback_wrapper);
        timesRouteWrapper = (TextInputLayout) findViewById(R.id.times_routes_wraper);
        routeTitleWrapper = (TextInputLayout) findViewById(R.id.route_title_wrapper);

        distanceGoingWrapper.setHint(getResources().getString(R.string.dist_going));
        distanceBackWrapper.setHint(getResources().getString(R.string.dist_come_back));
        timesRouteWrapper.setHint(getResources().getString(R.string.times_routes_week));
        routeTitleWrapper.setHint(getResources().getString(R.string.route_title));

        checkBoxRoute = (CheckBox) findViewById(R.id.check_route);
        checkBoxRoute.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    distanceBackWrapper.setVisibility(View.VISIBLE);
                } else {
                    distanceBackWrapper.setVisibility(View.GONE);
                }
            }
        });

        checkRepeat = (CheckBox) findViewById(R.id.check_repeats);
        checkRepeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    timesRouteWrapper.getEditText().setEnabled(true);
                } else {
                    timesRouteWrapper.getEditText().setEnabled(false);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.distance_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.done_route) {
            if (validateFields()) {
                saveRoute(getRouteTitle(), getDistanceGoing(), getDistanceBack(),
                        checkBoxRoute.isChecked(), checkRepeat.isChecked(), getRepetitions());
            } else {
                Log.d(TAG_ROUTES_ACTIVITY, "no menu, algo foi inválido");
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean validateFields() {
        String distanceGoing = distanceGoingWrapper.getEditText().getText().toString();
        String distanceBack = distanceBackWrapper.getEditText().getText().toString();
        String repeatTimes = timesRouteWrapper.getEditText().getText().toString();
        String titleRoute = routeTitleWrapper.getEditText().getText().toString();

        if (titleRoute != null && distanceGoing != null && distanceBack != null && repeatTimes != null) {
            if (titleRoute.trim().isEmpty()) {
                routeTitleWrapper.setError(getString(R.string.invalid_name));
            } else if (distanceGoing.trim().isEmpty()) {
                distanceGoingWrapper.setError(getString(R.string.invalid_distance));
            } else if (checkBoxRoute.isChecked() && distanceBack.trim().isEmpty()) {
                distanceBackWrapper.setError(getString(R.string.invalid_distance));
            } else if (checkRepeat.isChecked() && repeatTimes.trim().isEmpty()) {
                timesRouteWrapper.setError(getString(R.string.invalid_value));
            } else {
                return true;
            }
        } else {
            Log.e(TAG_ROUTES_ACTIVITY, "algo deu errado na recuperação dos campos de texto");
        }
        return false;
    }

    private void saveRoute(String title, float distanceGoing, float distanceBack, boolean goingBack,
                           boolean repeats, int repetitions) {

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "casosa-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession session = daoMaster.newSession();
        RotaDao rotaDao = session.getRotaDao();
        Rota novaRota = new Rota();
        rotaDao.insert(novaRota);

        novaRota.setId(rotaDao.getKey(novaRota));
        Log.d("RoutesActivity", "id da rota inserida: " + novaRota.getId());
        Log.d("RoutesActivity", "id da rota inserida no bd: " + rotaDao.getKey(novaRota));
        novaRota.setNome(title);
        novaRota.setDistanciaIda(distanceGoing);
        novaRota.setIdaEVolta(goingBack);
        novaRota.setDistanciaVolta(distanceBack);
        novaRota.setRepeteSemana(repeats);
        novaRota.setRepetoicoes(repetitions);
        rotaDao.update(novaRota);
        Log.d(TAG_ROUTES_ACTIVITY, "atualizou a rota no banco");
        calculaDistanciaTotal(session);
        onBackPressed();
    }

    public float getDistanceGoing() {
        return Integer.parseInt(distanceGoingWrapper.getEditText().getText().toString());
    }

    public float getDistanceBack() {
        if (checkBoxRoute.isChecked()) {
            return Integer.parseInt(distanceBackWrapper.getEditText().getText().toString());
        }
        return Integer.parseInt(distanceGoingWrapper.getEditText().getText().toString());
    }

    public int getRepetitions() {
        if (checkRepeat.isChecked()) {
            return Integer.parseInt(timesRouteWrapper.getEditText().getText().toString());
        } else {
            return 0;
        }
    }

    public String getRouteTitle() {
        return routeTitleWrapper.getEditText().getText().toString();
    }
    private List<Pair<String,Float>> calculaDistanciaPorRota(DaoSession session){
        String select = "SELECT * FROM ROTA";

        ArrayList<Rota> listaRotas = (ArrayList<Rota>) listRotas(session, select);
        List<Pair<String,Float>> listaRotaDistancia = new ArrayList<>();

        for (int i = 0; i < listaRotas.size(); i++) {
            float atual;
            if (listaRotas.get(i).getIdaEVolta()) {
                atual = listaRotas.get(i).getDistanciaIda() + listaRotas.get(i).getDistanciaVolta();
                if (listaRotas.get(i).getRepeteSemana()) {
                    atual = atual * listaRotas.get(i).getRepetoicoes();
                }
            } else {
                atual = listaRotas.get(i).getDistanciaIda();
                if (listaRotas.get(i).getRepeteSemana()) {
                    atual = atual * listaRotas.get(i).getRepetoicoes();
                }
            }
            Log.e("RoutesDistancia","Indice: " + i + " " + listaRotas.get(i).getNome() + ": " + atual);
            listaRotaDistancia.add(new Pair<>(listaRotas.get(i).getNome(), atual));
        }
        return listaRotaDistancia;
    }
    //CALCULO SEMANAL
    private void calculaDistanciaTotal(DaoSession session) {
        List<Pair<String,Float>> listaRotaDistancia = calculaDistanciaPorRota(session);
        float soma = 0.0f;

        for (int i = 0; i < listaRotaDistancia.size(); i++) {
            soma += listaRotaDistancia.get(i).second;
        }
        MinhaGasosaPreference.setDistanciaTotal(soma, getApplicationContext());
    }

    /**
     * Esse metodo detorna um par contendo o nome e a distancia das 3 principais rotas da semana
     * @param session
     * @return
     */
    private List<Pair<String,Float>> calculaPrincipaisRotas(DaoSession session) {
        List<Pair<String,Float>> listaRotaDistancia = calculaDistanciaPorRota(session);
        List<Pair<String,Float>> listaOrdenada = new ArrayList<>();

        while (listaOrdenada.size() < 3 && listaRotaDistancia.size() != 0) {
            int index = 0;
            for (int i = 0; i < listaRotaDistancia.size(); i++) {
                if (listaRotaDistancia.get(i).second > listaRotaDistancia.get(index).second) {
                    index = i;
                }
            }
            Log.e("RotasPrincipais", "RotaPrincipal: " + listaRotaDistancia.get(index).first);
            listaOrdenada.add(listaRotaDistancia.remove(index));
        }
        return listaOrdenada;
    }

    private static List<Rota> listRotas(DaoSession session, String select) {
        ArrayList<Rota> result = new ArrayList<Rota>();
        Cursor c = session.getDatabase().rawQuery(select, null);
        RotaDao rDao = session.getRotaDao();
        try {
            if (c.moveToFirst()) {
                do {
                    Rota r = new Rota();
                    r.setId(c.getLong(0));
                    r.setNome(c.getString(1));
                    r.setIdaEVolta(c.getInt(2) != 0);
                    r.setDistanciaIda(c.getFloat(3));
                    r.setDistanciaVolta(c.getFloat(4));
                    r.setRepeteSemana(c.getInt(5) != 0);
                    r.setRepetoicoes(c.getInt(6));
                    result.add(r);
                } while (c.moveToNext());
            }
        } finally {
            c.close();
        }
        return result;
    }
}