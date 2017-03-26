package com.minhagasosa.fragments.myCar;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.minhagasosa.R;
import com.minhagasosa.fragments.Home.HomeFragment;
import com.minhagasosa.fragments.carsettings.CarSettingsFragment;
import com.minhagasosa.preferences.MinhaGasosaPreference;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class myCarFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Meu Carro");
        View view = inflater.inflate(R.layout.activity_my_car, container, false);


        final TextView tvMarca = (TextView) view.findViewById(R.id.marca_do_carro);
        final TextView tvModelo = (TextView) view.findViewById(R.id.modelo_do_carro);
        final TextView tvVersao = (TextView) view.findViewById(R.id.versao_do_carro);

        Bundle bundle = getArguments();
        Button button = (Button) view.findViewById(R.id.buttonAlterarCarro);

        String marca = MinhaGasosaPreference.getMarca(getContext());
        String modelo = MinhaGasosaPreference.getModelo(getContext());
        String versao = MinhaGasosaPreference.getVersao(getContext());

        if (marca != null){
            tvMarca.setText(marca);
        }else{
            tvMarca.setText("");
        }
        if (modelo != null) {
            tvModelo.setText(modelo);
        }else{
            tvModelo.setText("");
        }
        if(versao != null){
            tvVersao.setText(versao);
        }else{
            tvVersao.setText("");
        }

        if (marca == null || modelo == null|| versao == null){
            //// TODO: 25/03/17
        }



        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                    Fragment fragment;
                    FragmentTransaction ft;
                    fragment = new CarSettingsFragment();
                    ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                    ft.commit();
            }
        });

        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }
}
