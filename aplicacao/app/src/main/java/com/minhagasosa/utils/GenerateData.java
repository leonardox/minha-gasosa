package com.minhagasosa.utils;

import com.minhagasosa.dao.Abastecimento;

import java.util.Collections;
import java.util.List;

public class GenerateData{
    private List<Abastecimento> abastecimentos;
		public GenerateData(List<Abastecimento> abastecimentos){
			this.abastecimentos = abastecimentos;
			Collections.sort(this.abastecimentos);
		}
		
		public double getKmTotal(){
			double result = abastecimentos.get(abastecimentos.size()).getOdometro() - abastecimentos.get(0).getOdometro();
			return result;
		}
	public double getKmTotal(int inicio, int fim){
			double result = abastecimentos.get(fim).getOdometro() - abastecimentos.get(inicio).getOdometro();
			System.out.println("inicio"+inicio+" " +  abastecimentos.get(inicio).getOdometro() + ", fim :"+fim+":" + abastecimentos.get(fim).getOdometro() + " km total: " +result);
			return result;
		}

	public double getGastoTotal(){
			double total = 0;
			for (Abastecimento abs : abastecimentos) {
				total += abs.getPrecoTotal();
			}
			return total;
		}


	public double getKmPerLitre(){
			int inicio = -1 ,fim = -1;
			double result;
			for (int i = 0; i < abastecimentos.size(); i++) {
				if( abastecimentos.get(i).getTanqueCheio()){
					if(inicio < 0){
						inicio = i;
						System.out.println("inicio " + i);
					}else {
						System.out.println("fim " + i);
						fim = i;
					}
				}
			}
			if (inicio >= 0 && fim > inicio){
				double total = getTotalLitre(inicio,fim);
				result = getKmTotal(inicio, fim) / total;
				return result;
			}else {
				return -1;
			}
		}

	public double getTotalLitre(int inicio, int fim) {
			double total = 0;
			for (int i = inicio +1; i <= fim; i++) {
				total += abastecimentos.get(i).getLitros();
				System.out.println("litros: " + total);
			}
			System.out.println("total litros: " + total);
			return total;
		}
		
		
	}
