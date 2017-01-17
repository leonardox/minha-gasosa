package com.minhagasosa.Transfer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by GAEL on 16/01/2017.
 */

public class State {

    @SerializedName("nome")
    @Expose
    private String nome;

    @SerializedName("sigla")
    @Expose
    private String sigla;

    @SerializedName("_id")
    @Expose
    private String id;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return this.nome;
    }
}
