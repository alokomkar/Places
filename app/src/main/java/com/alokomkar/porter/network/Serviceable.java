package com.alokomkar.porter.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Alok on 09/04/18.
 */

public class Serviceable {

    @SerializedName("serviceable")
    @Expose
    private Boolean serviceable;

    public Boolean getServiceable() {
        return serviceable;
    }

    public void setServiceable(Boolean serviceable) {
        this.serviceable = serviceable;
    }

}
