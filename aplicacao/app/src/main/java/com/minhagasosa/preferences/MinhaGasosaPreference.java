package com.minhagasosa.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.minhagasosa.R;

/**
 * Created by Vinicius Silva on 06/04/2016.
 */
public final class MinhaGasosaPreference {

    final private static String PREFERENCE = "com.minhagasosa.preference";
    private MinhaGasosaPreference() {

    }

    private static SharedPreferences.Editor getSharedPreference(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE,
                Context.MODE_PRIVATE);
        return preferences.edit();
    }

    public static void putWithPotency(boolean withPotency, Context context) {
        SharedPreferences.Editor editor = getSharedPreference(context);
        editor.putBoolean(context.getString(R.string.com_potencia), withPotency);
        editor.commit();
    }

    public static void putPotency(float potency, Context context) {
        SharedPreferences.Editor editor = getSharedPreference(context);
        editor.putFloat(context.getString(R.string.sharedPotencia), potency);
        editor.commit();
    }

    public static boolean getWithPotency(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE,
                Context.MODE_PRIVATE);
        return preferences.getBoolean(context.getString(R.string.com_potencia), false);

    }

    public static float getPotency(Context context) {
        final SharedPreferences preferences = context.getSharedPreferences(PREFERENCE,
                Context.MODE_PRIVATE);
        return preferences.getFloat(context.getString(R.string.sharedPotencia), -1);
    }


    public static void putPrice(float price, Context context) {
        SharedPreferences.Editor editor = getSharedPreference(context);
        editor.putFloat(context.getString(R.string.shared_price), price);
        editor.commit();
    }

    public static float getPrice(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE,
                Context.MODE_PRIVATE);
        return preferences.getFloat(context.getString(R.string.shared_price), 0);

    }

    public static void setCarroIsFlex(boolean is_flex, Context context) {
        SharedPreferences.Editor editor = getSharedPreference(context);
        editor.putBoolean(context.getString(R.string.is_flex), is_flex);
        editor.commit();
    }

    public static boolean getCarroIsFlex(Context context) {
        final SharedPreferences preferences = context.getSharedPreferences(PREFERENCE,
                Context.MODE_PRIVATE);
        return preferences.getBoolean(context.getString(R.string.is_flex), false);
    }

    public static void setConsumoUrbanoPrimario(float consumo, Context context) {
        SharedPreferences.Editor editor = getSharedPreference(context);
        editor.putFloat(context.getString(R.string.consumoUrbanoPrimario), consumo);
        editor.commit();
    }

    public static float getConsumoUrbanoPrimario(Context context) {
        final SharedPreferences preferences = context.getSharedPreferences(PREFERENCE,
                Context.MODE_PRIVATE);
        return preferences.getFloat(context.getString(R.string.consumoUrbanoPrimario), -1);
    }

    public static void setConsumoUrbanoSecundario(float consumo, Context context) {
        SharedPreferences.Editor editor = getSharedPreference(context);
        editor.putFloat(context.getString(R.string.consumoUrbanoSecundario), consumo);
        editor.commit();
    }

    public static float getConsumoUrbanoSecundario(Context context) {
        final SharedPreferences preferences = context.getSharedPreferences(PREFERENCE,
                Context.MODE_PRIVATE);
        return preferences.getFloat(context.getString(R.string.consumoUrbanoSecundario), -1);
    }

    public static void setConsumoRodoviarioPrimario(float consumo, Context context) {
        SharedPreferences.Editor editor = getSharedPreference(context);
        editor.putFloat(context.getString(R.string.consumoRodoviarioPrimario), consumo);
        editor.commit();
    }

    public static float getConsumoRodoviarioPrimario(Context context) {
        final SharedPreferences preferences = context.getSharedPreferences(PREFERENCE,
                Context.MODE_PRIVATE);
        return preferences.getFloat(context.getString(R.string.consumoRodoviarioPrimario), -1);
    }

    public static void setConsumoRodoviarioSecundario(float consumo, Context context) {
        SharedPreferences.Editor editor = getSharedPreference(context);
        editor.putFloat(context.getString(R.string.consumoRodoviarioSecundario), consumo);
        editor.commit();
    }



    public static float getConsumoRodoviarioSecundario(Context context) {
        final SharedPreferences preferences = context.getSharedPreferences(PREFERENCE,
                Context.MODE_PRIVATE);
        return preferences.getFloat(context.getString(R.string.consumoRodoviarioSecundario), -1);
    }

    public static void setDone(boolean done, Context context) {
        SharedPreferences.Editor editor = getSharedPreference(context);
        editor.putBoolean("done", done);
        editor.commit();
    }

    public static boolean getDone(Context context) {
        final SharedPreferences preferences = context.getSharedPreferences(PREFERENCE,
                Context.MODE_PRIVATE);
        return preferences.getBoolean("done", false);
    }

    public static void setDistanciaTotal(float distancia, Context context) {
        SharedPreferences.Editor editor = getSharedPreference(context);
        editor.putFloat(context.getString(R.string.distancia_total), distancia);
        editor.commit();
    }

    public static float getDistanciaTotal(Context context) {
        final SharedPreferences preferences = context.getSharedPreferences(PREFERENCE,
                Context.MODE_PRIVATE);
        return preferences.getFloat(context.getString(R.string.distancia_total), 0.0f);
    }

    public static void putValorMaximoParaGastar(float valor, Context context) {
        SharedPreferences.Editor editor = getSharedPreference(context);
        editor.putFloat(context.getString(R.string.shared_valor_maximo_gastar), valor);
        editor.commit();
    }

    public static float getValorMaximoParaGastar(Context context) {
        final SharedPreferences preferences = context.getSharedPreferences(PREFERENCE,
                Context.MODE_PRIVATE);
        return preferences.getFloat(context.getString(R.string.shared_valor_maximo_gastar), Float.MAX_VALUE);
    }

    public static void putCapacidadeDoTanque(float valor, Context context) {
        SharedPreferences.Editor editor = getSharedPreference(context);
        editor.putFloat(context.getString(R.string.shared_capacidade_tanque), valor);
        editor.commit();
    }

    public static float getCapacidadeDoTanque(Context context) {
        final SharedPreferences preferences = context.getSharedPreferences(PREFERENCE,
                Context.MODE_PRIVATE);
        return preferences.getFloat(context.getString(R.string.shared_capacidade_tanque), (float)-1.0);
    }
}
