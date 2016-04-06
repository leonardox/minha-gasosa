package com.minhagasosa;


import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import com.minhagasosa.dao.Carro;
import com.minhagasosa.dao.CarroDao;
import com.minhagasosa.dao.DaoMaster;
import com.minhagasosa.dao.DaoSession;
import com.minhagasosa.dao.Modelo;
import com.minhagasosa.dao.ModeloDao;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.Params;

import junit.framework.Assert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.BeforeClass;

import java.io.IOException;
import java.io.InputStream;

public class DatabaseTest extends AndroidTestCase {
    private final String LOT_TAG_TEST = "TAG_DATABASE_TEST";

    private Modelo modeloFusca;
    private Modelo modeloHb20;
    private Modelo modeloHilux;
    private Modelo modeloFiesta;
    private Modelo modeloGolf;

    private Carro fusca;
    private Carro fiesta;
    private Carro hilux;
    private Carro hb20;
    private Carro golf;

    private DaoSession session;
    private ModeloDao modeloDao;
    private CarroDao carroDao;

    @BeforeClass
    protected void setUp() throws Exception {
        super.setUp();
        Log.d(LOT_TAG_TEST, "entrou no setUp");
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getContext(), "casosa-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);

        session = daoMaster.newSession();
        modeloDao = session.getModeloDao();
        carroDao = session.getCarroDao();

        inicializaModelos();
        inicializaCarros();
    }

    private void inicializaCarros() {
        golf = new Carro((long) 1108, "Volkswagen", "2014", (float) 11.5, (float) 14.9, (float) -1, (float) -1, false, "Highline 1.4 TSi", modeloGolf.getId());
        fiesta = new Carro((long) 220, "Ford", "2000", (float) 13.7, (float) 14.1, (float) -1, (float) -1, false, "GL 1.0", modeloFiesta.getId());
        hilux = new Carro((long) 1525, "Toyota", "2012", (float) 6.5, (float) 7.93, (float) 6.1, (float) 5, true, "SRV 2.7 Flex 4x4 AT", modeloHilux.getId());
        hb20 = new Carro((long) 1596, "Hyunday", "2013", (float) 11.6, (float) 12.7, (float) 8.7, (float) 7.6, true, "Comfort 1.6 16v", modeloHb20.getId());
        fusca = new Carro((long) 887, "Volkswagen", "2013", (float) 9.7, (float) 13.1, (float) 0.0, (float) 0.0, false, "2.0 TSi DSG", modeloFusca.getId());
    }

    private void inicializaModelos() {
        modeloGolf = new Modelo();
        modeloGolf.setMODELO("Golf Highline 1.4 TSi");
        modeloFiesta = new Modelo();
        modeloFiesta.setMODELO("Fiesta GL 1.0");
        modeloHilux = new Modelo();
        modeloHilux.setMODELO("Hilux SRV 2.7");
        modeloHb20 = new Modelo();
        modeloHb20.setMODELO("HB20 Comfort 1.6 16V");
        modeloFusca = new Modelo();
        modeloFusca.setMODELO("Fusca 2.0 TSi DSG");
    }

    public void test01() {
        assertEquals(0, modeloDao.count());
        assertEquals(0, carroDao.count());
    }

    public void testAddModel() {
        assertEquals(0, modeloDao.count());
        modeloDao.insert(modeloGolf);
        assertEquals(1, modeloDao.count());

        modeloDao.insert(modeloFiesta);
        assertEquals(2, modeloDao.count());

        modeloDao.insert(modeloHilux);
        assertEquals(3, modeloDao.count());

        modeloDao.insert(modeloHb20);
        assertEquals(4, modeloDao.count());

        modeloDao.insert(modeloFusca);
        assertEquals(5, modeloDao.count());
    }

    public void testAddCarro() {
        assertEquals(0, carroDao.count());

        carroDao.insert(golf);
        assertEquals(1, carroDao.count());
        carroDao.insert(fiesta);
        assertEquals(2, carroDao.count());
        carroDao.insert(hilux);
        assertEquals(3, carroDao.count());
        carroDao.insert(hb20);
        assertEquals(4, carroDao.count());
        carroDao.insert(fusca);
        assertEquals(5, carroDao.count());
    }

    public void testAddDenovo() {
        try {
            carroDao.insert(fusca);
            Assert.fail("Deveria ter quebrado.");
        } catch (SQLiteConstraintException e) {
            //success
        }

        try {
            carroDao.insert(hilux);
            Assert.fail("Deveria ter quebrado.");
        } catch (SQLiteConstraintException e) {
            //success
        }

        try {
            carroDao.insert(fusca);
            Assert.fail("Deveria ter quebrado.");
        } catch (SQLiteConstraintException e) {
            //success
        }
    }

    public void testDelete() {
        assertEquals(5, modeloDao.count());
        assertEquals(5, carroDao.count());

        carroDao.delete(fusca);
        assertEquals(4, carroDao.count());

        carroDao.delete(fusca);
        assertEquals(4, carroDao.count());

        // O que acontece se eu deletar todos os modelos?
        modeloDao.deleteAll();
        assertEquals(0, modeloDao.count());

        // verifica se os objetos DAO retornados pela session estão atualizados.
        modeloDao = session.getModeloDao();
        carroDao = session.getCarroDao();

        assertEquals(0, modeloDao.count());
        assertEquals(4, carroDao.count());

        carroDao.deleteAll();
        assertEquals(0, carroDao.count());
    }

    public void testEdit() {
        clearDB();
    }

    private void clearDB() {
        modeloDao.deleteAll();
        carroDao.deleteAll();
    }

    private void populateBD(final DaoSession session) {
        Log.d(LOT_TAG_TEST, "entrou no polulateBD");
        JobManager jobManager = new JobManager(getContext());
        if (modeloDao.count() == 0) {
            jobManager.addJobInBackground(new Job(new Params(1)) {
                @Override
                public void onAdded() {
                    Log.d(LOT_TAG_TEST, "Terminou de preencher o banco");
                    Log.d(LOT_TAG_TEST, "num de modelos no modeloDao: " + modeloDao.count());
                    Log.d(LOT_TAG_TEST, "num de modelos no carroDao: " + carroDao.count());
                }

                @Override
                public void onRun() throws Throwable {
                    CarroDao cDao = session.getCarroDao();
                    populateCars(modeloDao, cDao);
                }

                @Override
                protected void onCancel() {

                }
            });
            System.out.println("Populating...");

        }
        carroDao = session.getCarroDao();
    }

    private String loadJSONFromAsset(String fileName) {
        String json = null;
        try {

            InputStream is = getContext().getAssets().open(fileName);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    private void populateCars(ModeloDao mDao, CarroDao cDao) {
        Log.d(LOT_TAG_TEST, "chamou o populateCars");
        String jsonModelos = loadJSONFromAsset("Modelos.json");
        String jsonCarros = loadJSONFromAsset("carrosminhagasosa.json");
        try {
            JSONArray modelos = new JSONArray(jsonModelos);
            Log.d(LOT_TAG_TEST, "num de modelos no jSon: " + modelos.length());
            for (int i = 0; i < modelos.length(); i++) {
                JSONObject modeloJson = modelos.getJSONObject(i);
                Modelo modelo = new Modelo(modeloJson.getLong("ID"), modeloJson.getString("MODELO"));
                System.out.println("Adding model: " + modeloJson.getString("MODELO"));
                mDao.insert(modelo);
            }
            JSONArray carros = new JSONArray(jsonCarros);
            Log.d(LOT_TAG_TEST, "num de carros no jSon: " + modelos.length());
            for (int i = 0; i < carros.length(); i++) {
                JSONObject cj = carros.getJSONObject(i);
                if (cj.getInt("FLEX") == 1) {
                    Carro c = new Carro();
                    c.setId(cj.getLong("id"));
                    c.setMarca(cj.getString("marca"));
                    c.setAno(cj.getString("ano"));
                    c.setConsumoUrbanoGasolina((float) cj.getDouble("urbano"));
                    c.setConsumoRodoviarioGasolina((float) cj.getDouble("rodoviario"));
                    c.setVersion(cj.getString("VERSION"));
                    c.setModeloId(cj.getLong("MODEL"));
                    if (cj.getInt("FLEX") == 1) {
                        c.setConsumoUrbanoAlcool((float) cj.getDouble("urbano_alcol"));
                        c.setConsumoRodoviarioAlcool((float) cj.getDouble("rodoviario_alcool"));
                    }
                    System.out.println("Inserting car: " + c.getVersion() + " | " + i);
                    cDao.insert(c);
                }

            }
        } catch (JSONException e) {
            System.out.print("TRETAOO");
            e.printStackTrace();
        }
    }
}
