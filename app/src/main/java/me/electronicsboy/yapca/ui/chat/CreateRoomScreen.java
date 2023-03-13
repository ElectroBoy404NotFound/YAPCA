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
import java.util.ArrayList;

import me.electronicsboy.yapca.R;
import me.electronicsboy.yapca.TempStorage;
import me.electronicsboy.yapca.ui.splash.ChatAppSplashScreen;
import me.electronicsboy.yapca.util.Crypto;

public class CreateRoomScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room_screen);

        ((Button) findViewById(R.id.buttoncreate2)).setOnClickListener((v) -> {
            if(((HashMap<String, String>) TempStorage.get("CHAT_KEYS")).get(((EditText)findViewById(R.id.editTextTextPersonName)).getText().toString()) != null)
                Toast.makeText(CreateRoomScreen.this, "Room already exists!", Toast.LENGTH_SHORT).show();
            else {
                try {
                    ((HashMap<String, String>) TempStorage.get("CHAT_KEYS")).put(((EditText)findViewById(R.id.editTextTextPersonName)).getText().toString(), Crypto.getSHA256(((EditText)findViewById(R.id.editTextTextPassword3)).getText().toString()));
                    FirebaseDatabase.getInstance().getReference("ChatKeys/" + ((EditText)findViewById(R.id.editTextTextPersonName)).getText().toString()).setValue(Crypto.getSHA256(((EditText)findViewById(R.id.editTextTextPassword3)).getText().toString()));
                    List<String> chats = (List<String>) TempStorage.get("CHATS_DATA");
                    if(chats == null) chats = new ArrayList<String>();
                    chats.add(((EditText)findViewById(R.id.editTextTextPersonName)).getText().toString());
                    String finalData = "";
                    for(int i = 0; i < chats.size(); i++) {
                        finalData += Crypto.encrypt(chats.get(i), (String) TempStorage.get("PASSWORD_CLEARTXT")).replace("\n", "");
                        if(i < chats.size()-1) finalData += ',';
                    }
                    System.out.println(finalData);
                    FirebaseDatabase.getInstance().getReference("Chats/" + TempStorage.get("USERNAME")).setValue(finalData);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        });
        ((Button)findViewById(R.id.buttonback2)).setOnClickListener((v) ->
                startActivity(new Intent(CreateRoomScreen.this, ChatAppSplashScreen.class))
        );
    }
}