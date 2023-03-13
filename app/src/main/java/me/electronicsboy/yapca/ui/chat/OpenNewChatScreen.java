package me.electronicsboy.yapca.ui.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;

import me.electronicsboy.yapca.R;
import me.electronicsboy.yapca.TempStorage;
import me.electronicsboy.yapca.ui.splash.ChatAppSplashScreen;
import me.electronicsboy.yapca.util.Crypto;

public class OpenNewChatScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_new_chat_screen);
        ((Button) findViewById(R.id.buttonopen)).setOnClickListener((v) -> {
            if(((HashMap<String, String>) TempStorage.get("CHAT_KEYS")).get(((EditText)findViewById(R.id.username)).getText().toString()) == null)
                Toast.makeText(OpenNewChatScreen.this, "Room does not exists!", Toast.LENGTH_SHORT).show();
            else {
                HashMap<String, String> chatKeys = ((HashMap<String, String>) TempStorage.get("CHAT_KEYS"));
                try {
                    if(chatKeys.get(((EditText) findViewById(R.id.username)).getText().toString()).equals(Crypto.getSHA256(((EditText) findViewById(R.id.password)).getText().toString()))) {
                        List<String> chats = (List<String>) TempStorage.get("CHATS_DATA");
                        if(chats==null) chats = new ArrayList<String>();
                        chats.add(((EditText)findViewById(R.id.username)).getText().toString());
                        String finalData = "";
                        for(int i = 0; i < chats.size(); i++) {
                            finalData += Crypto.encrypt(chats.get(i), (String) TempStorage.get("PASSWORD_CLEARTXT")).replace("\n", "");
                            if(i < chats.size()-1) finalData += ',';
                        }
                        System.out.println(finalData);
                        FirebaseDatabase.getInstance().getReference("Chats/" + TempStorage.get("USERNAME")).setValue(finalData);
                        startActivity(new Intent(OpenNewChatScreen.this, ChatAppSplashScreen.class));
                    }else
                        Toast.makeText(OpenNewChatScreen.this, "Incorrect password for room!", Toast.LENGTH_SHORT).show();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        });
        ((Button)findViewById(R.id.buttonback)).setOnClickListener((v) ->
                startActivity(new Intent(OpenNewChatScreen.this, ChatAppSplashScreen.class))
        );
    }
}