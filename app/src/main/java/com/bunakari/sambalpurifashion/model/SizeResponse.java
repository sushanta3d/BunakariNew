package com.bunakari.sambalpurifashion.model;

import java.io.Serializable;

public class SizeResponse implements Serializable {

    private String id;
    private String size;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
