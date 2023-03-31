package me.electronicsboy.yapca.ui.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import me.electronicsboy.yapca.R;
import me.electronicsboy.yapca.TempStorage;
import me.electronicsboy.yapca.data.MessageAdapter;
import me.electronicsboy.yapca.data.MessageItem;
import me.electronicsboy.yapca.ui.splash.SplashScreen;
import me.electronicsboy.yapca.util.Crypto;

public class ChatScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);
        ListView listview = findViewById(R.id.chat);
        List<MessageItem> ele = new ArrayList<>();
        List<HashMap> data = (List<HashMap>) TempStorage.get("CHAT_DATA");
        Collections.reverse(data);
        data.forEach((e) -> {
            MessageItem msg = new MessageItem();
            msg.setText(Crypto.decrypt((String) e.get("msg"), (String) TempStorage.get("CT_CP")));
            msg.setName(Crypto.decrypt((String) e.get("user"), (String) TempStorage.get("CT_CP")));
            ele.add(msg);
        });
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Chat/" + TempStorage.get("OPEN_CHAT"));

        MessageAdapter adapter = new MessageAdapter(this, R.layout.item_chat, ele);
        listview.setAdapter(adapter);
        ValueEventListener vel = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<HashMap> chatData = new ArrayList<>();
                for(DataSnapshot snap : dataSnapshot.getChildren())
                    chatData.add((HashMap) snap.getValue());
                ele.clear();
                Collections.reverse(chatData);
                chatData.forEach((e) -> {
                    MessageItem msg = new MessageItem();
                    msg.setText(Crypto.decrypt((String) e.get("msg"), (String) TempStorage.get("CT_CP")));
                    msg.setName(Crypto.decrypt((String) e.get("user"), (String) TempStorage.get("CT_CP")));
                    ele.add(msg);
                });
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
//                Toast.makeText(LoginActivity.this, "ERROR!\n" + error.toException().getMessage(), Toast.LENGTH_LONG).show();
                Log.w(null,"Failed to read value.", error.toException());
            }
        };
        myRef.addValueEventListener(vel);
        adapter.notifyDataSetChanged();
        ((Button) findViewById(R.id.sendtxt)).setOnClickListener((v) -> {
            HashMap<String, String> data2 = new HashMap<>();
            data2.put("user", Crypto.encrypt((String) TempStorage.get("USERNAME"), (String) TempStorage.get("CT_CP")));
            data2.put("msg", Crypto.encrypt(((EditText)findViewById(R.id.messagesend)).getText().toString(), (String) TempStorage.get("CT_CP")));
            myRef.push().setValue(data2);
        });
        ((Button) findViewById(R.id.logout)).setOnClickListener((v) -> {
            myRef.removeEventListener(vel);
            TempStorage.clear();
            startActivity(new Intent(ChatScreen.this, SplashScreen.class));
        });
        ((Button) findViewById(R.id.sendtxt)).setEnabled(false);
        ((EditText)findViewById(R.id.messagesend)).setEnabled(true);
        ((EditText)findViewById(R.id.messagesend)).setVisibility(View.VISIBLE);
        ((EditText)findViewById(R.id.messagesend)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Do nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                ((Button) findViewById(R.id.sendtxt)).setEnabled(!s.toString().isEmpty());
            }
        });
    }
}