package com.minhagasosa.dao;

import java.io.IOException;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Property.PropertyBuilder;
import de.greenrobot.daogenerator.Schema;
public class Generators {

	
	/**
	 * @param args
	 * @throws Exception 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException, Exception {
			Schema schema = new Schema(1, "com.minhagasosa.bd");
			schema.setDefaultJavaPackageTest("com.minhagasosa");
			Entity modelo = schema.addEntity("Modelo");
			Entity carro = schema.addEntity("Carro");
			Entity rota = schema.addEntity("Rota");
			Entity abastecimento = schema.addEntity("Abastecimento");
			
			modelo.addIdProperty().autoincrement();
			modelo.addStringProperty("MODELO");
			
			carro.addIdProperty().autoincrement();
			carro.addStringProperty("marca");
			carro.addStringProperty("ano");
			carro.addFloatProperty("consumoUrbanoGasolina");
			carro.addFloatProperty("consumoRodoviarioGasolina");
			carro.addFloatProperty("consumoUrbanoAlcool");
			carro.addFloatProperty("consumoRodoviarioAlcool");
			carro.addBooleanProperty("isFlex");
			carro.addStringProperty("version");
			Property modeloId = carro.addLongProperty("modeloId").getProperty();
			carro.addToOne(modelo, modeloId);
			
			rota.addIdProperty().autoincrement();
			rota.addStringProperty("Nome");
			rota.addBooleanProperty("idaEVolta");
			rota.addFloatProperty("distanciaIda");
			rota.addFloatProperty("distanciaVolta");
			rota.addBooleanProperty("repeteSemana");
			rota.addIntProperty("repetoicoes");

            abastecimento.addIdProperty().autoincrement();
            abastecimento.addDateProperty("dataAbastecimento");
            abastecimento.addFloatProperty("precoTotal");
            abastecimento.addFloatProperty("precoCombustivel");
            abastecimento.addFloatProperty("litros");
            abastecimento.addFloatProperty("odometro");
            abastecimento.addBooleanProperty("tanqueCheio");

			new DaoGenerator().generateAll(schema, "./");
			System.out.println("Done!");
	}

}
