package com.minhagasosa.fragments.listroutes;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.minhagasosa.R;
import com.minhagasosa.RotaAdapter;
import com.minhagasosa.dao.DaoMaster;
import com.minhagasosa.dao.DaoSession;
import com.minhagasosa.dao.Rota;
import com.minhagasosa.dao.RotaDao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListRoutesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Listagem de Rotas");
        View view = inflater.inflate(R.layout.activity_list_routes, container, false);

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(getContext(), "casosa-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession session = daoMaster.newSession();
        RotaDao rDao = session.getRotaDao();
        List<Rota> rotas  =rDao.loadAll();
        Date today = new Date();
        List<Rota> rotasMes = new ArrayList<Rota>();
        for (Rota r:
                rotas) {
            if(today.getMonth() == r.getData().getMonth() && today.getYear() == r.getData().getYear()){
                rotasMes.add(r);
            }
        }
        ListView lv = (ListView) view.findViewById(R.id.lvRotas);
        ListAdapter la = new RotaAdapter(getContext(), R.layout.route_adapter, rotasMes);
        lv.setAdapter(la);
        return view;
    }

}
