package com.minhagasosa.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "MODELO".
 */
public class Modelo {

    private Long id;
    private String MODELO;

    public Modelo() {
    }

    public Modelo(Long id) {
        this.id = id;
    }

    public Modelo(Long id, String MODELO) {
        this.id = id;
        this.MODELO = MODELO;
    }

    public final Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public final String getMODELO() {
        return MODELO;
    }

    public final void setMODELO(String MODELO) {
        this.MODELO = MODELO;
    }

    @Override
    public final String toString(){
        return  getMODELO();
    }

}
