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
import java.util.Objects;

import me.electronicsboy.yapca.R;
import me.electronicsboy.yapca.TempStorage;
import me.electronicsboy.yapca.ui.splash.ChatAppSplashScreen;
import me.electronicsboy.yapca.util.Crypto;

public class OpenNewChatScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_new_chat_screen);
        ((EditText) findViewById(R.id.password)).addTextChangedListener(new TextWatcher() {
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
                EditText password = (EditText)findViewById(R.id.password);
                if(password.getText().toString().length() < 5 || password.getText().toString().length() > 16) {
                    password.setError(getString(R.string.invalid_password));
                    ((Button)findViewById(R.id.buttonopen)).setEnabled(false);
                }else
                    ((Button)findViewById(R.id.buttonopen)).setEnabled(true);
            }
        });
        ((EditText)findViewById(R.id.username)).addTextChangedListener(new TextWatcher() {
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
                EditText username = (EditText)findViewById(R.id.username);
                if(!isUserNameValid(username.getText().toString())) {
                    username.setError(getString(R.string.invalid_username));
                    ((Button)findViewById(R.id.buttonopen)).setEnabled(false);
                }else
                    ((Button)findViewById(R.id.buttonopen)).setEnabled(true);
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
        ((Button) findViewById(R.id.buttonopen)).setOnClickListener((v) -> {
            if(((HashMap<String, String>) TempStorage.get("CHAT_KEYS")).get(((EditText)findViewById(R.id.username)).getText().toString()) == null)
                Toast.makeText(OpenNewChatScreen.this, "Room does not exists!", Toast.LENGTH_SHORT).show();
            else {
                StringBuilder password = new StringBuilder(((EditText) findViewById(R.id.password)).getText().toString());
                if(password.length() < 16)
                    while(password.length() < 16)
                        password.append('0');
                HashMap<String, String> chatKeys = ((HashMap<String, String>) TempStorage.get("CHAT_KEYS"));
                try {
                    if(Objects.requireNonNull(chatKeys.get(((EditText) findViewById(R.id.username)).getText().toString())).equals(Crypto.getSHA256(password.toString()))) {
                        List<String> chats = (List<String>) TempStorage.get("CHATS_DATA");
                        chats.add(((EditText)findViewById(R.id.username)).getText().toString());
                        StringBuilder finalData = new StringBuilder();
                        for(int i = 0; i < chats.size(); i++) {
                            finalData.append(Crypto.encrypt(chats.get(i), (String) TempStorage.get("PASSWORD_CLEARTXT")).replace("\n", ""));
                            if(i < chats.size()-1) finalData.append(',');
                        }
                        System.out.println(finalData);
                        FirebaseDatabase.getInstance().getReference("Chats/" + TempStorage.get("USERNAME")).setValue(finalData.toString());
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