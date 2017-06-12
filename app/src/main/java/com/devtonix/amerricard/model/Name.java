
package com.devtonix.amerricard.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Name implements Serializable{

    @SerializedName("en")
    @Expose
    private String en;
    @SerializedName("es")
    @Expose
    private String es;
    @SerializedName("ru")
    @Expose
    private String ru;
    @SerializedName("ua")
    @Expose
    private String ua;
    @SerializedName("fr")
    @Expose
    private String fr;

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getEs() {
        return es;
    }

    public void setEs(String es) {
        this.es = es;
    }

    public String getRu() {
        return ru;
    }

    public void setRu(String ru) {
        this.ru = ru;
    }

    public String getUa() {
        return ua;
    }

    public void setUa(String ua) {
        this.ua = ua;
    }

    public String getFr() {
        return fr;
    }

    public void setFr(String fr) {
        this.fr = fr;
    }

}
