package com.minhagasosa.dao;

import com.minhagasosa.dao.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "CARRO".
 */
public class Carro {

    private Long id;
    private String marca;
    private String ano;
    private Float consumoUrbanoGasolina;
    private Float consumoRodoviarioGasolina;
    private Float consumoUrbanoAlcool;
    private Float consumoRodoviarioAlcool;
    private Boolean isFlex;
    private String version;
    private Long modeloId;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient CarroDao myDao;

    private Modelo modelo;
    private Long modelo__resolvedKey;


    public Carro() {
    }

    public Carro(Long id) {
        this.id = id;
    }

    public Carro(Long id, String marca, String ano, Float consumoUrbanoGasolina, Float consumoRodoviarioGasolina, Float consumoUrbanoAlcool, Float consumoRodoviarioAlcool, Boolean isFlex, String version, Long modeloId) {
        this.id = id;
        this.marca = marca;
        this.ano = ano;
        this.consumoUrbanoGasolina = consumoUrbanoGasolina;
        this.consumoRodoviarioGasolina = consumoRodoviarioGasolina;
        this.consumoUrbanoAlcool = consumoUrbanoAlcool;
        this.consumoRodoviarioAlcool = consumoRodoviarioAlcool;
        this.isFlex = isFlex;
        this.version = version;
        this.modeloId = modeloId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCarroDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public Float getConsumoUrbanoGasolina() {
        return consumoUrbanoGasolina;
    }

    public void setConsumoUrbanoGasolina(Float consumoUrbanoGasolina) {
        this.consumoUrbanoGasolina = consumoUrbanoGasolina;
    }

    public Float getConsumoRodoviarioGasolina() {
        return consumoRodoviarioGasolina;
    }

    public void setConsumoRodoviarioGasolina(Float consumoRodoviarioGasolina) {
        this.consumoRodoviarioGasolina = consumoRodoviarioGasolina;
    }

    public Float getConsumoUrbanoAlcool() {
        return consumoUrbanoAlcool;
    }

    public void setConsumoUrbanoAlcool(Float consumoUrbanoAlcool) {
        this.consumoUrbanoAlcool = consumoUrbanoAlcool;
    }

    public Float getConsumoRodoviarioAlcool() {
        return consumoRodoviarioAlcool;
    }

    public void setConsumoRodoviarioAlcool(Float consumoRodoviarioAlcool) {
        this.consumoRodoviarioAlcool = consumoRodoviarioAlcool;
    }

    public Boolean getIsFlex() {
        return isFlex;
    }

    public void setIsFlex(Boolean isFlex) {
        this.isFlex = isFlex;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Long getModeloId() {
        return modeloId;
    }

    public void setModeloId(Long modeloId) {
        this.modeloId = modeloId;
    }

    /** To-one relationship, resolved on first access. */
    public Modelo getModelo() {
        Long __key = this.modeloId;
        if (modelo__resolvedKey == null || !modelo__resolvedKey.equals(__key)) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ModeloDao targetDao = daoSession.getModeloDao();
            Modelo modeloNew = targetDao.load(__key);
            synchronized (this) {
                modelo = modeloNew;
            	modelo__resolvedKey = __key;
            }
        }
        return modelo;
    }

    public void setModelo(Modelo modelo) {
        synchronized (this) {
            this.modelo = modelo;
            modeloId = modelo == null ? null : modelo.getId();
            modelo__resolvedKey = modeloId;
        }
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

    @Override
    public String toString(){
        return getVersion();
    }

}
