package me.electronicsboy.yapca.ui.chat;

import static me.electronicsboy.yapca.util.TempStorage.*;

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
import java.util.Objects;

import me.electronicsboy.yapca.R;
import me.electronicsboy.yapca.ui.splash.ChatAppSplashScreen;
import me.electronicsboy.yapca.ui.splash.ChatScreenSplashScreen;
import me.electronicsboy.yapca.util.Crypto;
import me.electronicsboy.yapca.util.StringUtil;

public class ChatLoginScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_login_screen);
        HashMap<String, String> bannedUsers = (HashMap<String, String>) get("BANNED_USERS_CHATS");
        if(bannedUsers.get(get("OPEN_CHAT")) != null) {
            for (String u : Objects.requireNonNull(bannedUsers.get(get("OPEN_CHAT"))).split(",")) {
                System.out.println(u);
                if (u.equals(get("USERNAME"))) {
                    Toast.makeText(this, "You are banned from " + get("OPEN_CHAT") + "!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ChatLoginScreen.this, ChatSelectScreen.class));
                    finish();
                }
            }
        }
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
        ((TextView)findViewById(R.id.text123)).setText("Login for " + get("OPEN_CHAT"));
        ((Button)findViewById(R.id.buttoncreate)).setOnClickListener((v) -> {
            HashMap<String, String> keys = (HashMap<String, String>) get("CHAT_KEYS");
            try {
                String password = StringUtil.convertTo16chars(((EditText) findViewById(R.id.editTextTextPassword)).getText().toString());
                if(Crypto.getSHA256(password).equals(keys.get(get("OPEN_CHAT")))){
                    addOrSet("CT_CP", password);
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