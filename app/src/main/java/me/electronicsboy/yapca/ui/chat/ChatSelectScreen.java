package me.electronicsboy.yapca.ui.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import me.electronicsboy.yapca.R;
import me.electronicsboy.yapca.util.TempStorage;
import me.electronicsboy.yapca.ui.splash.SplashScreen;

public class ChatSelectScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_select_screen);
        ListView listview = findViewById(R.id.chatsList);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, (List) TempStorage.get("CHATS_DATA"));
        listview.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        listview.setOnItemClickListener((parentView, childView, position, id) -> onItemClick(childView));
        ((Button) findViewById(R.id.menu)).setOnClickListener((v) -> {
            TempStorage.clear();
            startActivity(new Intent(ChatSelectScreen.this, SplashScreen.class));
        });
        ((Button) findViewById(R.id.createroom)).setOnClickListener((v) -> startActivity(new Intent(ChatSelectScreen.this, CreateRoomScreen.class)));
        ((Button) findViewById(R.id.openroom)).setOnClickListener((v) -> startActivity(new Intent(ChatSelectScreen.this, OpenNewChatScreen.class)));
    }

    private void onItemClick(View childView) {
        TempStorage.addOrSet("OPEN_CHAT", ((TextView) childView).getText());
        startActivity(new Intent(ChatSelectScreen.this, ChatLoginScreen.class));
        finish();
    }
}