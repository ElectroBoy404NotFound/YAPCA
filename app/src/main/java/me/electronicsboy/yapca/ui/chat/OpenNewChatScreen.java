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
import me.electronicsboy.yapca.util.TempStorage;
import me.electronicsboy.yapca.ui.splash.ChatAppSplashScreen;
import me.electronicsboy.yapca.util.Crypto;
import me.electronicsboy.yapca.util.StringUtil;

public class OpenNewChatScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_new_chat_screen);
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
            TempStorage.addOrSet("OPEN_CHAT", ((EditText) findViewById(R.id.username)).getText().toString());
            startActivity(new Intent(OpenNewChatScreen.this, ChatLoginScreen.class));
            finish();
        });
        ((Button)findViewById(R.id.buttonback)).setOnClickListener((v) -> {
            startActivity(new Intent(OpenNewChatScreen.this, ChatAppSplashScreen.class));
            finish();
        });
    }
}