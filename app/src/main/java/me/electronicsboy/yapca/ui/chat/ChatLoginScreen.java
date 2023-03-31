package me.electronicsboy.yapca.ui.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import me.electronicsboy.yapca.R;
import me.electronicsboy.yapca.TempStorage;
import me.electronicsboy.yapca.ui.splash.ChatAppSplashScreen;
import me.electronicsboy.yapca.ui.splash.ChatScreenSplashScreen;
import me.electronicsboy.yapca.util.Crypto;

public class ChatLoginScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_login_screen);
        ((EditText)findViewById(R.id.editTextTextPassword)).addTextChangedListener(new TextWatcher() {
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
                EditText password = (EditText)findViewById(R.id.editTextTextPassword);
                if(password.getText().toString().length() < 5 || password.getText().toString().length() > 16) {
                    password.setError(getString(R.string.invalid_password));
                    ((Button)findViewById(R.id.buttoncreate)).setEnabled(false);
                }else
                    ((Button)findViewById(R.id.buttoncreate)).setEnabled(true);
            }
        });
        ((TextView)findViewById(R.id.text123)).setText("Login for " + TempStorage.get("OPEN_CHAT"));
        ((Button)findViewById(R.id.buttoncreate)).setOnClickListener((v) -> {
            HashMap<String, String> keys = (HashMap<String, String>) TempStorage.get("CHAT_KEYS");
            try {
                StringBuilder password = new StringBuilder(((EditText) findViewById(R.id.editTextTextPassword)).getText().toString());
                if(password.length() < 16)
                    while(password.length() < 16)
                        password.append('0');
                if(Crypto.getSHA256(password.toString()).equals(keys.get(TempStorage.get("OPEN_CHAT")))){
                    TempStorage.addOrSet("CT_CP", ((EditText)findViewById(R.id.editTextTextPassword)).getText().toString());
                    startActivity(new Intent(ChatLoginScreen.this, ChatScreenSplashScreen.class));
                }else Toast.makeText(ChatLoginScreen.this, "Incorrect Password!", Toast.LENGTH_SHORT).show();
            } catch (NoSuchAlgorithmException ex) {
                throw new RuntimeException(ex);
            }
        });
        ((Button)findViewById(R.id.buttonback)).setOnClickListener((v) ->
            startActivity(new Intent(ChatLoginScreen.this, ChatAppSplashScreen.class))
        );
    }
}