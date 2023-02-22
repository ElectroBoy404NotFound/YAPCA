package me.electronicsboy.yapca.ui.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Message;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import me.electronicsboy.yapca.R;
import me.electronicsboy.yapca.TempStorage;
import me.electronicsboy.yapca.data.MessageAdapter;
import me.electronicsboy.yapca.data.MessageItem;

public class ChatScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);
        ListView listview = findViewById(R.id.chat);
        List<MessageItem> ele = new ArrayList<>();
        MessageItem msg = new MessageItem();
        msg.setText("Test");
        msg.setName("test");
        ele.add(msg);
        MessageAdapter adapter = new MessageAdapter(this, R.layout.item_chat, ele);
        listview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}