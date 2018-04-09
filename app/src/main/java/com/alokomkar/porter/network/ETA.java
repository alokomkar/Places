package com.alokomkar.porter.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Alok on 09/04/18.
 */

public class ETA {

    @SerializedName("eta")
    @Expose
    private Integer eta;

    public Integer getEta() {
        return eta;
    }

    public void setEta(Integer eta) {
        this.eta = eta;
    }

}
