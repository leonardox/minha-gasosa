package com.minhagasosa;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.util.Pair;

import com.minhagasosa.dao.DaoSession;
import com.minhagasosa.dao.Rota;
import com.minhagasosa.dao.RotaDao;
import com.minhagasosa.preferences.MinhaGasosaPreference;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Leonardo on 25/04/2016.
 */
public class Utils {

    public static List<Rota> listRotas(DaoSession session, String select) {
        ArrayList<Rota> result = new ArrayList<Rota>();
        Cursor c = session.getDatabase().rawQuery(select, null);

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
                    r.setDeRotina(c.getInt(7) != 0);
                    System.out.println("-----------" + (c.getLong(8)) + "---" + (new Date(c.getLong(8))));
                    r.setData(c.getLong(8));
                    result.add(r);
                } while (c.moveToNext());
            }
        } finally {
            c.close();
        }
        return result;
    }

    public static List<Pair<String, Float>> calculaDistanciaPorRota(DaoSession session, String mes, String ano) {
        String select = "SELECT * FROM ROTA";

        ArrayList<Rota> listaRotas = (ArrayList<Rota>) listRotas(session, select);
        List<Pair<String, Float>> listaRotaDistancia = new ArrayList<>();

        for (int i = 0; i < listaRotas.size(); i++) {
            String data = listaRotas.get(i).getData().toString();

            if (ano == null && mes == null ||
                    ano.equals(data.substring(24)) && mes.equals(data.substring(4, 7))) {
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
                Log.e("RoutesDistancia", "Indice: " + i + " " + listaRotas.get(i).getNome() + ": " + atual + " " + listaRotas.get(i).getData());
                listaRotaDistancia.add(new Pair<>(listaRotas.get(i).getNome(), atual));
            }
        }
        return listaRotaDistancia;
    }

    //CALCULO SEMANAL
    public static void calculaDistanciaTotal(DaoSession session, String mes, String ano, Context context) {
        List<Pair<String, Float>> listaRotaDistancia = calculaDistanciaPorRota(session, mes, ano);
        float soma = 0.0f;

        for (int i = 0; i < listaRotaDistancia.size(); i++) {
            soma += listaRotaDistancia.get(i).second;
        }
        MinhaGasosaPreference.setDistanciaTotal(soma, context);
    }

    /**
     * Esse metodo detorna um par contendo o nome e a distancia das 3 principais rotas da semana
     *
     * @param session
     * @return
     */
    public static List<Pair<String, Float>> calculaPrincipaisRotas(DaoSession session, String mes,String ano) {
        List<Pair<String, Float>> listaRotaDistancia = calculaDistanciaPorRota(session, mes, ano);
        List<Pair<String, Float>> listaOrdenada = new ArrayList<>();

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
}
