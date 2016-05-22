package com.minhagasosa;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.minhagasosa.dao.DaoMaster;
import com.minhagasosa.dao.DaoSession;
import com.minhagasosa.dao.Rota;
import com.minhagasosa.dao.RotaDao;

import java.util.List;

public class ListRoutesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_routes);
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "casosa-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession session = daoMaster.newSession();
        RotaDao rDao = session.getRotaDao();
        List<Rota> rotas  =rDao.loadAll();

        ListView lv = (ListView) findViewById(R.id.lvRotas);
        ListAdapter la = new RotaAdapter(this, R.layout.route_adapter, rotas);
        lv.setAdapter(la);
    }
}
