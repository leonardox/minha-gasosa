package com.minhagasosa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.minhagasosa.dao.Rota;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

public class RotaAdapter extends ArrayAdapter<Rota> {

    public RotaAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public RotaAdapter(Context context, int resource, List<Rota> items) {
        super(context, resource, items);
    }

    @Override
    public final View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.route_adapter, null);
        }

        Rota r = getItem(position);

        TextView tvTituloRota = (TextView) v.findViewById(R.id.tvTituloRota);
        TextView tvDistTotal = (TextView) v.findViewById(R.id.tvDistTotal);
        ImageView imvExtra = (ImageView) v.findViewById(R.id.imvExtra);
        tvTituloRota.setText(r.getNome());
        DecimalFormat df = new DecimalFormat("##.##");
        df.setRoundingMode(RoundingMode.DOWN);
        if(r.getIdaEVolta()){
            tvDistTotal.setText(df.format((r.getDistanciaIda() + r.getDistanciaVolta())) + " KM");
        }else{
            tvDistTotal.setText(df.format((r.getDistanciaIda())) + " KM");
        }

        if(r.getDeRotina()) {
            imvExtra.setVisibility(View.GONE);
        }
        return v;
    }

}