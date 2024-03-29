package com.minhagasosa.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 

import java.util.Date;

/**
 * Entity mapped to table "ROTA".
 */
public class Rota {

    private Long id;
    private String Nome;
    private Boolean idaEVolta;
    private Float distanciaIda;
    private Float distanciaVolta;
    private Boolean repeteSemana;
    private Integer repetoicoes;
    private Boolean deRotina;
    private long data;

    public Rota() {
    }

    public Rota(Long id) {
        this.id = id;
    }

    public Rota(Long id, String Nome, Boolean idaEVolta, Float distanciaIda, Float distanciaVolta, Boolean repeteSemana, Integer repetoicoes, Boolean deRotina, long date) {
        this.id = id;
        this.Nome = Nome;
        this.idaEVolta = idaEVolta;
        this.distanciaIda = distanciaIda;
        this.distanciaVolta = distanciaVolta;
        this.repeteSemana = repeteSemana;
        this.repetoicoes = repetoicoes;
        this.deRotina = deRotina;
        this.data = date;//new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String Nome) {
        this.Nome = Nome;
    }

    public Boolean getIdaEVolta() {
        return idaEVolta;
    }

    public void setIdaEVolta(Boolean idaEVolta) {
        this.idaEVolta = idaEVolta;
    }

    public Float getDistanciaIda() {
        return distanciaIda;
    }

    public void setDistanciaIda(Float distanciaIda) {
        this.distanciaIda = distanciaIda;
    }

    public Float getDistanciaVolta() {
        return distanciaVolta;
    }

    public void setDistanciaVolta(Float distanciaVolta) {
        this.distanciaVolta = distanciaVolta;
    }

    public Boolean getRepeteSemana() {
        return repeteSemana;
    }

    public void setRepeteSemana(Boolean repeteSemana) {
        this.repeteSemana = repeteSemana;
    }

    public Integer getRepetoicoes() {
        return repetoicoes;
    }

    public void setRepetoicoes(Integer repetoicoes) {
        this.repetoicoes = repetoicoes;
    }

    public Boolean getDeRotina() {
        return deRotina;
    }

    public void setDeRotina(Boolean deRotina) {
        this.deRotina = deRotina;
    }

    public void setData(long data){ this.data = data; }

    public long getDataInLong() { return  data; }
    public Date getData() { return  new Date(data); }
}
