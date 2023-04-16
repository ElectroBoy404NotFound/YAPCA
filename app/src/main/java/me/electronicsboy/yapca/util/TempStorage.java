package me.electronicsboy.yapca.util;

import java.util.HashMap;

public class TempStorage {
    private static final HashMap<String, Object> vars = new HashMap<>();
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
