package me.electronicsboy.yapca.util;

import org.json.JSONObject;

public class DataListenerInterface implements Client.DataListener {
    private Client.DataListener l;

    public DataListenerInterface() {
        this.l = new Client.DataListener() {public void gotData(JSONObject data) {}};
    }

    @Override
    public void gotData(JSONObject data) {
        System.out.println(data.toString());
        l.gotData(data);
    }

    public void setL(Client.DataListener l) {
        this.l = l;
    }
}
