package me.electronicsboy.yapca.ui.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;

import me.electronicsboy.yapca.R;
import me.electronicsboy.yapca.TempStorage;
import me.electronicsboy.yapca.ui.splash.ChatScreenSplashScreen;
import me.electronicsboy.yapca.util.Crypto;

public class ChatSelectScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_select_screen);
        ListView listview = findViewById(R.id.chatsList);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, (List) TempStorage.get("CHATS_DATA"));
        listview.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        listview.setOnItemClickListener(this::onItemClick);
    }

    private void onItemClick(AdapterView<?> parentView, View childView, int position, long id) {
        TempStorage.addOrSet("OPEN_CHAT", ((TextView) childView).getText());
        System.out.println(TempStorage.get("OPEN_CHAT"));
        PopupWindow popUp = new PopupWindow(this);
        LinearLayout layout = new LinearLayout(this);
//            layout.addView(tv, params);
        Button b = new Button(this);
        b.setText("Login");
        EditText e = new EditText(this);
        TextView t = new TextView(this);
        t.setText("Login for " + ((TextView) childView).getText());
        e.setText("TEST");
        b.setOnClickListener((view) -> {
            HashMap<String, String> keys = (HashMap<String, String>) TempStorage.get("CHAT_KEYS");
            try {
                if(Crypto.getSHA256(e.getText().toString()).equals(keys.get(TempStorage.get("OPEN_CHAT")))){
                    System.out.println("LOGIN");
                    TempStorage.addOrSet("CT_CP", e.getText().toString());
                    startActivity(new Intent(ChatSelectScreen.this, ChatScreenSplashScreen.class));
                }
            } catch (NoSuchAlgorithmException ex) {
                throw new RuntimeException(ex);
            }
        });
        layout.addView(t);
        layout.addView(e);
        layout.addView(b);
        popUp.setContentView(layout);
        popUp.showAtLocation(layout, Gravity.CENTER, 10, 10);
    }
}