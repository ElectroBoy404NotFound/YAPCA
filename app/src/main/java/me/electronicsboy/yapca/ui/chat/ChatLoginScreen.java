package me.electronicsboy.yapca.ui.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
//        ((EditText)findViewById(R.id.editTextTextPassword)).addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                // Do nothing
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                // Do nothing
//            }
//        });
        ((TextView)findViewById(R.id.text123)).setText("Login for " + TempStorage.get("OPEN_CHAT"));
        ((Button)findViewById(R.id.button)).setOnClickListener((v) -> {
            HashMap<String, String> keys = (HashMap<String, String>) TempStorage.get("CHAT_KEYS");
            try {
                if(Crypto.getSHA256(((EditText)findViewById(R.id.editTextTextPassword)).getText().toString()).equals(keys.get(TempStorage.get("OPEN_CHAT")))){
                    System.out.println("LOGIN");
                    TempStorage.addOrSet("CT_CP", ((EditText)findViewById(R.id.editTextTextPassword)).getText().toString());
                    startActivity(new Intent(ChatLoginScreen.this, ChatScreenSplashScreen.class));
                }else Toast.makeText(ChatLoginScreen.this, "Incorrect Password!", Toast.LENGTH_SHORT).show();
            } catch (NoSuchAlgorithmException ex) {
                throw new RuntimeException(ex);
            }
        });
        ((Button)findViewById(R.id.button2)).setOnClickListener((v) ->
            startActivity(new Intent(ChatLoginScreen.this, ChatAppSplashScreen.class))
        );
    }
}