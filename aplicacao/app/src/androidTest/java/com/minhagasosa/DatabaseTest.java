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

import junit.framework.Assert;

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

    private static boolean isFirstTime = true;

    protected void setUp() throws Exception {
        super.setUp();
        Log.d(LOT_TAG_TEST, "entrou no setUp");
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getContext(), "casosa-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);

        session = daoMaster.newSession();
        modeloDao = session.getModeloDao();
        carroDao = session.getCarroDao();

        if (isFirstTime) {
            Log.d(LOT_TAG_TEST, "entrou no if de apagar");
            modeloDao.deleteAll();
            carroDao.deleteAll();
            isFirstTime = false;
        }


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
            Assert.fail("Deveria ter lançado exceção.");
        } catch (SQLiteConstraintException e) {
            //success
        }

        try {
            carroDao.insert(hilux);
            Assert.fail("Deveria ter lançado exceção.");
        } catch (SQLiteConstraintException e) {
            //success
        }

        try {
            carroDao.insert(fusca);
            Assert.fail("Deveria ter lançado exceção.");
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
        populateDaos();

        fusca.setMarca("Toyota");
        carroDao.update(fusca);
        assertEquals("Toyota", fusca.getMarca());

        Carro newFusca = carroDao.load(fusca.getId());
        assertEquals("Toyota", newFusca.getMarca());

        clearDB();
    }

    private void populateDaos() {
        modeloDao.insert(modeloFusca);
        modeloDao.insert(modeloFiesta);
        modeloDao.insert(modeloGolf);
        modeloDao.insert(modeloHb20);
        modeloDao.insert(modeloHilux);

        carroDao.insert(fusca);
        carroDao.insert(fiesta);
        carroDao.insert(golf);
        carroDao.insert(hb20);
        carroDao.insert(hilux);
    }

    private void clearDB() {
        modeloDao.deleteAll();
        carroDao.deleteAll();
    }
}
