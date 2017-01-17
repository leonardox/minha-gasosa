package com.minhagasosa.Transfer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by GAEL on 16/01/2017.
 */

public class City {

    @SerializedName("nome")
    @Expose
    private String nome;

    @SerializedName("state")
    @Expose
    private String state;

    @SerializedName("_id")
    @Expose
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return this.nome;
    }
}
