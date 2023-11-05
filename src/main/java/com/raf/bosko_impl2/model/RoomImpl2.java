package com.raf.bosko_impl2.model;

import model.Room;

import java.util.HashMap;

public class RoomImpl2 extends Room {

    String name;
    HashMap<String, Object> features;
    public RoomImpl2(String name) {
        super(name);
        this.features = new HashMap<>();
    }

    public boolean addFeature(String key, Object val){

        if (!features.keySet().contains(key)) {

            features.put(key, val);
            return true;
        }
        return false;
    }
}
