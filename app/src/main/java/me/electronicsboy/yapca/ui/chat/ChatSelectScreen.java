package me.electronicsboy.yapca.ui.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import me.electronicsboy.yapca.R;
import me.electronicsboy.yapca.TempStorage;

public class ChatSelectScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_select_screen);
        ListView listview = findViewById(R.id.chatsList);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, (List) TempStorage.get("CHATS_DATA"));
        listview.setAdapter(adapter);
        adapter.notifyDataSetChanged();

//        listview.setOnItemClickListener((AdapterView<?> parentView, View childView, int position, long id) -> {});
    }
}