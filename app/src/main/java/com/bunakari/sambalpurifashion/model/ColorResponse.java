package com.bunakari.sambalpurifashion.model;

import java.io.Serializable;

public class ColorResponse implements Serializable {

    private String id;
    private String colorname;
    private String colorcode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getColorname() {
        return colorname;
    }

    public void setColorname(String colorname) {
        this.colorname = colorname;
    }

    public String getColorcode() {
        return colorcode;
    }

    public void setColorcode(String colorcode) {
        this.colorcode = colorcode;
    }
}
