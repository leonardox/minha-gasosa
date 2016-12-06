package com.minhagasosa;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Broadcast responsavél por lançar a notificação toda vez que chega a hora definida.
 */
public class MyReceiver extends BroadcastReceiver {
    /**
     * Contador para permitir que cada notificação seja única, que uma não sobreescreva a outra.
     */
    int MID = 0;
    /**
     * Lista de dicas.
     */
    private List<String> dicas = new ArrayList<>();

    @Override
    public final void onReceive(Context context, Intent intent) {
        preencheDicas(context);
        Log.d("MyReceiver", "entrou no onReceive");
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context).
                setSmallIcon(R.mipmap.ic_gas_station_black_24dp).setContentTitle("Minha gasosa dicas")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(getDicaAleatoria()));
        // link do tutorial usado https://androiddeveloperhelp.wordpress.com/2014/12/26/daily-local-notification-android/
        notificationManager.notify(MID, builder.build());
        MID++;
    }

    /**
     * Recupera uma dica aleatória da lista de dicas.
     *
     * @return uma dica.
     */
    private String getDicaAleatoria() {
        Log.d("MyReceiver", "tamanho da lista de dicas=" + dicas.size());
        Random random = new Random();
        return dicas.get(random.nextInt(29));
    }

    /**
     * Adiciona na lista de dicas um total de 30 dicas recuperadas das strings.
     *
     * @param context uma instância do contexto da aplicação.
     */
    private void preencheDicas(Context context) {
        dicas.add(context.getResources().getString(R.string.dica01));
        dicas.add(context.getResources().getString(R.string.dica02));
        dicas.add(context.getResources().getString(R.string.dica03));
        dicas.add(context.getResources().getString(R.string.dica04));
        dicas.add(context.getResources().getString(R.string.dica05));
        dicas.add(context.getResources().getString(R.string.dica06));
        dicas.add(context.getResources().getString(R.string.dica07));
        dicas.add(context.getResources().getString(R.string.dica08));
        dicas.add(context.getResources().getString(R.string.dica09));
        dicas.add(context.getResources().getString(R.string.dica10));
        dicas.add(context.getResources().getString(R.string.dica11));
        dicas.add(context.getResources().getString(R.string.dica12));
        dicas.add(context.getResources().getString(R.string.dica13));
        dicas.add(context.getResources().getString(R.string.dica14));
        dicas.add(context.getResources().getString(R.string.dica15));
        dicas.add(context.getResources().getString(R.string.dica16));
        dicas.add(context.getResources().getString(R.string.dica17));
        dicas.add(context.getResources().getString(R.string.dica18));
        dicas.add(context.getResources().getString(R.string.dica19));
        dicas.add(context.getResources().getString(R.string.dica20));
        dicas.add(context.getResources().getString(R.string.dica21));
        dicas.add(context.getResources().getString(R.string.dica22));
        dicas.add(context.getResources().getString(R.string.dica23));
        dicas.add(context.getResources().getString(R.string.dica24));
        dicas.add(context.getResources().getString(R.string.dica25));
        dicas.add(context.getResources().getString(R.string.dica26));
        dicas.add(context.getResources().getString(R.string.dica27));
        dicas.add(context.getResources().getString(R.string.dica28));
        dicas.add(context.getResources().getString(R.string.dica29));
        dicas.add(context.getResources().getString(R.string.dica30));
    }
}
