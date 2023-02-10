package me.electronicsboy.yapca;

import java.util.HashMap;

public class TempStorage {
    private static HashMap<String, Object> vars = new HashMap<String, Object>();
    public static void addOrSet(String var, Object obj) {
        vars.put(var, obj);
    }
    public static Object get(String var) {
        return vars.get(var);
    }

    public static void clear() {
        vars.clear();
    }
}
