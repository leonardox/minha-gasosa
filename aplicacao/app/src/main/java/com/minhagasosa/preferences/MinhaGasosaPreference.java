package com.minhagasosa.preferences;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.minhagasosa.R;

/**
 * Created by Vinicius Silva on 06/04/2016.
 */
public class MinhaGasosaPreference {

    final private static String PREFERENCE = "com.minhagasosa.preference";


    private static SharedPreferences.Editor getSharedPreference(Context context){
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE,
                Context.MODE_PRIVATE);
        return preferences.edit();
    }

    public static void putWithPotency(boolean withPotency, Context context){
        SharedPreferences.Editor editor = getSharedPreference(context);
        editor.putBoolean(context.getString(R.string.com_potencia),withPotency);
        editor.commit();
    }

    public static void putPotency(int potency, Context context){
        SharedPreferences.Editor editor = getSharedPreference(context);
        editor.putInt(context.getString(R.string.sharedPotencia), potency);
        editor.commit();
    }

    public static boolean getWithPotency(Context context){
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE,
                Context.MODE_PRIVATE);
        return preferences.getBoolean(context.getString(R.string.com_potencia),false);

    }
    public static int getPotency(Context context){
        final SharedPreferences preferences =  context.getSharedPreferences(PREFERENCE,
                Context.MODE_PRIVATE);
        return preferences.getInt(context.getString(R.string.sharedPotencia), -1);
    }


    public static void putPrice(float price, Context context){
        SharedPreferences.Editor editor = getSharedPreference(context);
        editor.putFloat(context.getString(R.string.shared_price), price);
        editor.commit();
    }

    public static float getPrice(Context context){
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE,
                Context.MODE_PRIVATE);
        return preferences.getFloat(context.getString(R.string.shared_price), -1);

    }
}
