package com.minhagasosa;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import com.minhagasosa.dao.DaoMaster;
import com.minhagasosa.dao.DaoSession;
import com.minhagasosa.dao.Rota;
import com.minhagasosa.dao.RotaDao;
import static com.minhagasosa.Utils.calculaDistanciaTotal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Date;


/**
 *  classe de rotas.
 */
public class RoutesActivity extends AppCompatActivity {

    /**
     * Verificar repetir
     */
    private CheckBox checkRepeat;
    /**
     * Verificar rota
     */
    private CheckBox checkBoxRoute;
    /**
     * Verificar tipo de rota
     */
    private CheckBox checkBoxTypeRoute;
    /**
     * distancia ida
     */
    private TextInputLayout distanceGoingWrapper;
    /**
     * distancia volta
     */
    private TextInputLayout distanceBackWrapper;
    /**
     * tempo de rota
     */
    private TextInputLayout timesRouteWrapper;
    /**
     * titulo da rota
     */
    private TextInputLayout routeTitleWrapper;
    /**
     * Actividade das rotas
     */
    private static final String TAG_ROUTES_ACTIVITY = "RoutesActivity";
    /**
     * requisicao da rota do mapa.
     */
    private final int MAPA_ROTA_REQUEST = 102;

    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initializeLayout();
    }

    /**
     * Metodo que inicializa os campos do layout com seus respectivos atributos
     */
    private void initializeLayout() {
        distanceGoingWrapper = (TextInputLayout) findViewById(R.id.distance_going_wrapper);
        distanceBackWrapper = (TextInputLayout) findViewById(R.id.distance_comeback_wrapper);
        timesRouteWrapper = (TextInputLayout) findViewById(R.id.times_routes_wraper);
        routeTitleWrapper = (TextInputLayout) findViewById(R.id.route_title_wrapper);

        distanceGoingWrapper.setHint(getResources().getString(R.string.dist_going));
        distanceBackWrapper.setHint(getResources().getString(R.string.dist_come_back));
        timesRouteWrapper.setHint(getResources().getString(R.string.times_routes_week));
        routeTitleWrapper.setHint(getResources().getString(R.string.route_title));
        Button botaoMapa = (Button) findViewById(R.id.button_mapa);
        botaoMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Intent i = new Intent(RoutesActivity.this, MapsActivity.class);
                startActivityForResult(i, MAPA_ROTA_REQUEST);
            }
        });
        checkBoxRoute = (CheckBox) findViewById(R.id.check_route);
        checkBoxRoute.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
                if (isChecked) {
                    distanceBackWrapper.setVisibility(View.VISIBLE);
                } else {
                    distanceBackWrapper.setVisibility(View.GONE);
                }
            }
        });
        checkBoxTypeRoute = (CheckBox) findViewById(R.id.check_type_route);
        checkBoxTypeRoute.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
                if (isChecked) {
                    checkRepeat.setVisibility(View.VISIBLE);
                    timesRouteWrapper.setVisibility(View.VISIBLE);
                } else {
                    checkRepeat.setVisibility(View.GONE);
                    timesRouteWrapper.setVisibility(View.GONE);
                }
            }
        });

        checkRepeat = (CheckBox) findViewById(R.id.check_repeats);
        checkRepeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
                if (isChecked) {
                    timesRouteWrapper.getEditText().setEnabled(true);
                } else {
                    timesRouteWrapper.getEditText().setEnabled(false);
                }
            }
        });
    }

    @Override
    public final boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.distance_menu, menu);
        return true;
    }

    /**
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected final void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        switch(requestCode){
            case MAPA_ROTA_REQUEST:
                if (resultCode == RESULT_OK) {
                    distanceGoingWrapper.getEditText().setText("");
                    distanceBackWrapper.getEditText().setText("");
                    Bundle res = data.getExtras();
                    float ida = res.getFloat("ida", -1);
                    float volta = res.getFloat("volta" , -1);
                    DecimalFormat df = new DecimalFormat("##.##");
                    df.setRoundingMode(RoundingMode.DOWN);
                    if(ida != -1) distanceGoingWrapper.getEditText().setText(df.format(ida/1000.0));
                    if(volta != -1) distanceBackWrapper.getEditText().setText(df.format(volta/1000.0));
                }
                break;
        }
    }

    @Override
    public final boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == R.id.done_route) {
            if (validateFields()) {
                saveRoute(getRouteTitle(), getDistanceGoing(), getDistanceBack(),
                        checkBoxRoute.isChecked(), checkRepeat.isChecked(), getRepetitions(),
                        checkBoxTypeRoute.isChecked());
            } else {
                Log.d(TAG_ROUTES_ACTIVITY, "no menu, algo foi inválido");
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * metodo que valida os campos de entrada
     * @return true caso for validade ou false caso seja invalido
     */
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

    /**
     *
     * @param title
     * @param distanceGoing
     * @param distanceBack
     * @param goingBack
     * @param repeats
     * @param repetitions
     * @param deRotina
     */
    private void saveRoute(final String title, final float distanceGoing, final float distanceBack, final boolean goingBack,
                           final boolean repeats, final int repetitions, final boolean deRotina) {

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
        novaRota.setDeRotina(deRotina);
        novaRota.setData(new Date().getTime());

        rotaDao.update(novaRota);
        Log.d(TAG_ROUTES_ACTIVITY, "atualizou a rota no banco");
        calculaDistanciaTotal(session, null, null, getApplicationContext());
        onBackPressed();
    }

    /**
     * metodo que retorna a distancia de ida
     * @return
     */
    public final float getDistanceGoing() {
        return Integer.parseInt(distanceGoingWrapper.getEditText().getText().toString());
    }

    /**
     * metodo que verifica se a rota foi checada, se sim retorna a distancia de volta
     * caso contrário retorna a distancia de ida
     * @return
     */
    public final float getDistanceBack() {
        if (checkBoxRoute.isChecked()) {
            return Integer.parseInt(distanceBackWrapper.getEditText().getText().toString());
        }
        return Integer.parseInt(distanceGoingWrapper.getEditText().getText().toString());
    }

    /**
     * metodo que retorna o tempo da rota
     * @return
     */
    public final int getRepetitions() {
        if (checkRepeat.isChecked()) {
            return Integer.parseInt(timesRouteWrapper.getEditText().getText().toString());
        } else {
            return 0;
        }
    }

    /**
     * metodo que retorna o titulo da rota
     * @return
     */
    public final String getRouteTitle() {
        return routeTitleWrapper.getEditText().getText().toString();
    }
}