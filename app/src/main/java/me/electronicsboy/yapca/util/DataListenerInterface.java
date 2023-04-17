package me.electronicsboy.yapca.util;

import org.json.JSONObject;

public class DataListenerInterface implements Client.DataListener {
    @Override
    public void gotData(JSONObject data) {
        System.out.println(data.toString());
    }
}
