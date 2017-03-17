package com.minhagasosa.fragments.expenditureplanning;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.minhagasosa.R;
import com.minhagasosa.fragments.Home.HomeFragment;
import com.minhagasosa.preferences.MinhaGasosaPreference;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class ExpenditurePlanningFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Planejamento de gastos");
        View view = inflater.inflate(R.layout.activity_planning, container, false);

        final EditText editValor = (EditText) view.findViewById(R.id.ed_valor_maximo);
        float valor = MinhaGasosaPreference.getValorMaximoParaGastar(getContext());

        editValor.setText(String.valueOf(valor));
        Bundle bundle = getArguments();
        Button button = (Button) view.findViewById(R.id.buttonOK);
        float distancia = bundle.getFloat("distance", -1);
        float consumo = bundle.getFloat("consumo", -1);
        float capacidade = MinhaGasosaPreference.getCapacidadeDoTanque(getContext());
        Log.e("Distania: ", distancia + "");
        Log.e("Consumo: ", consumo+"");
        Log.e("Capacidade: ", capacidade+"");

        if(distancia > 0 && capacidade != -1 && consumo != -1){
            TextView edVezes = (TextView) view.findViewById(R.id.ed_tanque_vezes);
            float vezes = (distancia/consumo)/capacidade;
            DecimalFormat df = new DecimalFormat("##.##");
            df.setRoundingMode(RoundingMode.DOWN);
            edVezes.setText(getString(R.string.neeeded_times) + df.format(vezes) + " " + getString(R.string.times) + ".");
        }
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!editValor.getText().toString().equals("")) {
                    float valor = Float.parseFloat(editValor.getText().toString());
                    MinhaGasosaPreference.putValorMaximoParaGastar(valor, getActivity());
                    Fragment fragment;
                    FragmentTransaction ft;
                    fragment = new HomeFragment();
                    ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                    ft.commit();
                }else{
                    Toast.makeText(getContext(),"Valor inválido",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }
}
