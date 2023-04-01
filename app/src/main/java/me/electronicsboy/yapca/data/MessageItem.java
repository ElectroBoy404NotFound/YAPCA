package me.electronicsboy.yapca.data;

import java.util.HashMap;

public class MessageItem {
    private String text;
    private String name;
    private String id;

    public MessageItem() {
    }

    public MessageItem(String text, String name, String photoUrl) {
        this.text = text;
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getID() { return id; }

    public void setID(String s) { this.id = s; }

    public HashMap<String, String> asHashMap() {
        HashMap<String, String> result = new HashMap<>();
        result.put("msg", getText());
        result.put("user", getName());
        return result;
    }
}
