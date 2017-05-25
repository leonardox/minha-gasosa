package com.minhagasosa;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.util.Pair;

import com.minhagasosa.dao.DaoSession;
import com.minhagasosa.dao.Rota;
import com.minhagasosa.preferences.MinhaGasosaPreference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Leonardo on 25/04/2016.
 */
public final class Utils {
    private static final int THREE = 3;
    private static final int FOUR = 4;
    private static final int FIVE = 5;
    private static final int SIX = 6;
    private static final int SEVEN = 7;
    private static final int EIGHT = 8;
    private static final int ZERO = 0;
    private static final int TWENTYFOUR = 24;

    private Utils(){

    }
    /**
     * @param session
     * @param select
     * @return
     */

    public static List<Rota> listRotas(final DaoSession session, final String select) {
        ArrayList<Rota> result = new ArrayList<Rota>();
        Cursor c = session.getDatabase().rawQuery(select, null);

        try {
            if (c.moveToFirst()) {
                do {
                    Rota r = new Rota();
                    r.setId(c.getLong(ZERO));
                    r.setNome(c.getString(1));
                    r.setIdaEVolta(c.getInt(2) != ZERO);
                    r.setDistanciaIda(c.getFloat(THREE));
                    r.setDistanciaVolta(c.getFloat(FOUR));
                    r.setRepeteSemana(c.getInt(FIVE) != ZERO);
                    r.setRepetoicoes(c.getInt(SIX));
                    r.setDeRotina(c.getInt(SEVEN) != ZERO);
                    r.setData(c.getLong(EIGHT));
                    result.add(r);
                } while (c.moveToNext());
            }
        } finally {
            c.close();
        }
        return result;
    }

    /**
     * @param session
     * @param mes
     * @param ano
     * @return
     */
    public static List<Pair<String, Float>> calculaDistanciaPorRota(final DaoSession session, final String mes, final String ano) {
        String select = "SELECT * FROM ROTA";

        ArrayList<Rota> listaRotas = (ArrayList<Rota>) listRotas(session, select);
        List<Pair<String, Float>> listaRotaDistancia = new ArrayList<>();

        for (int i = ZERO; i < listaRotas.size(); i++) {
            String data = listaRotas.get(i).getData().toString();

            if (ano == null && mes == null ||
                    ano.equals(data.substring(TWENTYFOUR)) && mes.equals(data.substring(FOUR, SEVEN))) {
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

    public static List<Pair<String, Float>> calculaDistanciaPorRotaSemanal(final DaoSession session, final String mes, final String ano) {
        Calendar cal = Calendar.getInstance();
        Calendar first = (Calendar) cal.clone();
        first.add(Calendar.DAY_OF_WEEK,
                first.getFirstDayOfWeek() - first.get(Calendar.DAY_OF_WEEK));
        Calendar last = (Calendar) first.clone();
        last.add(Calendar.DAY_OF_YEAR, 6);

        String select = "SELECT * FROM ROTA WHERE DE_ROTINA = 0 AND DATA >= " + first.getTimeInMillis()
                + " AND DATA <= " + last.getTimeInMillis();

        ArrayList<Rota> listaRotas = (ArrayList<Rota>) listRotas(session, select);
        List<Pair<String, Float>> listaRotaDistancia = new ArrayList<>();

        for (int i = ZERO; i < listaRotas.size(); i++) {
            String data = listaRotas.get(i).getData().toString();
            if (ano == null && mes == null ||
                    ano.equals(data.substring(TWENTYFOUR)) && mes.equals(data.substring(FOUR, SEVEN))) {
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

    /**
     * Metodo que calcula a distancia total
     *
     * @param session
     * @param mes
     * @param ano
     * @param context
     */
    public static void calculaDistanciaTotal(final DaoSession session, final String mes, final String ano, final Context context) {
        List<Pair<String, Float>> listaRotaDistancia = calculaDistanciaPorRota(session, mes, ano);
        float soma = 0.0f;

        for (int i = ZERO; i < listaRotaDistancia.size(); i++) {
            soma += listaRotaDistancia.get(i).second;
        }
        MinhaGasosaPreference.setDistanciaTotal(soma, context);
    }

    public static void calculaDistanciaTotalSemanalmente(final DaoSession session, final String mes, final String ano, final Context context) {
        List<Pair<String, Float>> listaRotaDistancia = calculaDistanciaPorRotaSemanal(session, mes, ano);
        float soma = 0.0f;

        for (int i = ZERO; i < listaRotaDistancia.size(); i++) {
            soma += listaRotaDistancia.get(i).second;
        }
        MinhaGasosaPreference.setDistanciaTotal(soma, context);
    }

    public static float calculaDistanciaTotal(DaoSession session, String mes, String ano) {
        List<Pair<String, Float>> listaRotaDistancia = calculaDistanciaPorRota(session, mes, ano);
        float total = 0.0f;
        Log.d("Utils", "num de rotas = " + listaRotaDistancia.size());

        for (int i = ZERO; i < listaRotaDistancia.size(); i++) {
            total += listaRotaDistancia.get(i).second;
        }
        return total;
    }

    /**
     * Esse metodo detorna um par contendo o nome e a distancia das THREE principais rotas da semana
     *
     * @param session
     * @return
     */
    public static List<Pair<String, Float>> calculaPrincipaisRotas(final DaoSession session, final String mes, String ano) {
        List<Pair<String, Float>> listaRotaDistancia = calculaDistanciaPorRota(session, mes, ano);
        List<Pair<String, Float>> listaOrdenada = new ArrayList<>();

        while (listaOrdenada.size() < THREE && listaRotaDistancia.size() != ZERO) {
            int index = ZERO;
            for (int i = ZERO; i < listaRotaDistancia.size(); i++) {
                if (listaRotaDistancia.get(i).second > listaRotaDistancia.get(index).second) {
                    index = i;
                }
            }
            Log.e("RotasPrincipais", "RotaPrincipal: " + listaRotaDistancia.get(index).first);
            listaOrdenada.add(listaRotaDistancia.remove(index));
        }

        return listaOrdenada;
    }

    public static List<Pair<String, Float>> calculaPrincipaisRotasSemanalmente(final DaoSession session, final String mes, String ano) {
        List<Pair<String, Float>> listaRotaDistancia = calculaDistanciaPorRotaSemanal(session, mes, ano);
        List<Pair<String, Float>> listaOrdenada = new ArrayList<>();

        while (listaOrdenada.size() < THREE && listaRotaDistancia.size() != ZERO) {
            int index = ZERO;
            for (int i = ZERO; i < listaRotaDistancia.size(); i++) {
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
