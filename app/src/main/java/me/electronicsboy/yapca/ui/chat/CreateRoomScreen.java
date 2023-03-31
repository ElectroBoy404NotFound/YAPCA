package me.electronicsboy.yapca.ui.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

public class CreateRoomScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room_screen);
        ((EditText)findViewById(R.id.editTextTextPassword3)).addTextChangedListener(new TextWatcher() {
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
                EditText password = (EditText)findViewById(R.id.editTextTextPassword3);
                if(password.getText().toString().length() < 5 || password.getText().toString().length() > 16) {
                    password.setError(getString(R.string.invalid_password));
                    ((Button)findViewById(R.id.buttoncreate2)).setEnabled(false);
                }else
                    ((Button)findViewById(R.id.buttoncreate2)).setEnabled(true);
            }
        });

        ((EditText)findViewById(R.id.editTextTextPersonName)).addTextChangedListener(new TextWatcher() {
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
                EditText username = (EditText)findViewById(R.id.editTextTextPersonName);
                if(!isUserNameValid(username.getText().toString())) {
                    username.setError(getString(R.string.invalid_username));
                    ((Button)findViewById(R.id.buttoncreate2)).setEnabled(false);
                }else
                    ((Button)findViewById(R.id.buttoncreate2)).setEnabled(true);
            }
            private boolean isUserNameValid(String username) {
                if (username == null) {
                    return false;
                }
                if (username.contains("@") || username.contains("/") || username.contains(".")) {
                    return false;
                } else {
                    return !username.trim().isEmpty();
                }
            }
        });
        ((Button) findViewById(R.id.buttoncreate2)).setOnClickListener((v) -> {
            if(((HashMap<String, String>) TempStorage.get("CHAT_KEYS")).get(((EditText)findViewById(R.id.editTextTextPersonName)).getText().toString()) != null)
                Toast.makeText(CreateRoomScreen.this, "Room already exists!", Toast.LENGTH_SHORT).show();
            else {
                try {
                    StringBuilder password = new StringBuilder(((EditText) findViewById(R.id.editTextTextPassword3)).getText().toString());
                    if(password.length() < 16)
                        while(password.length() < 16)
                            password.append('0');
                    ((HashMap<String, String>) TempStorage.get("CHAT_KEYS")).put(((EditText)findViewById(R.id.editTextTextPersonName)).getText().toString(), Crypto.getSHA256(password.toString()));
                    FirebaseDatabase.getInstance().getReference("ChatKeys/" + ((EditText)findViewById(R.id.editTextTextPersonName)).getText().toString()).setValue(Crypto.getSHA256(password.toString()));
                    List<String> chats = (List<String>) TempStorage.get("CHATS_DATA");
                    chats.add(((EditText)findViewById(R.id.editTextTextPersonName)).getText().toString());
                    StringBuilder finalData = new StringBuilder();
                    for(int i = 0; i < chats.size(); i++) {
                        finalData.append(Crypto.encrypt(chats.get(i), (String) TempStorage.get("PASSWORD_CLEARTXT")).replace("\n", ""));
                        if(i < chats.size()-1) finalData.append(',');
                    }
                    System.out.println(finalData);
                    FirebaseDatabase.getInstance().getReference("Chats/" + TempStorage.get("USERNAME")).setValue(finalData.toString());
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