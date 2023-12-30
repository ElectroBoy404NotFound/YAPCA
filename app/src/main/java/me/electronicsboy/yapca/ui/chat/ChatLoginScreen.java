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

import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Objects;

import javax.security.auth.login.LoginException;

import me.electronicsboy.yapca.R;
import me.electronicsboy.yapca.data.LoginDataSource;
import me.electronicsboy.yapca.data.Result;
import me.electronicsboy.yapca.data.model.LoggedInUser;
import me.electronicsboy.yapca.ui.splash.ChatAppSplashScreen;
import me.electronicsboy.yapca.ui.splash.ChatScreenSplashScreen;
import me.electronicsboy.yapca.util.Client;
import me.electronicsboy.yapca.util.Crypto;
import me.electronicsboy.yapca.util.DataListenerInterface;
import me.electronicsboy.yapca.util.StringUtil;
import me.electronicsboy.yapca.util.TempStorage;

public class ChatLoginScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_login_screen);
//        HashMap<String, String> bannedUsers = (HashMap<String, String>) get("BANNED_USERS_CHATS");
//        if(bannedUsers.get(get("OPEN_CHAT")) != null) {
//            for (String u : Objects.requireNonNull(bannedUsers.get(get("OPEN_CHAT"))).split(",")) {
//                System.out.println(u);
//                if (u.equals(get("USERNAME"))) {
//                    Toast.makeText(this, "You are banned from " + get("OPEN_CHAT") + "!", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(ChatLoginScreen.this, ChatSelectScreen.class));
//                    finish();
//                }
//            }
//        }
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
//            HashMap<String, String> keys = (HashMap<String, String>) get("CHAT_KEYS");
            try {
//                String password = StringUtil.convertTo16chars(((EditText) findViewById(R.id.editTextTextPassword)).getText().toString());
//                if(Crypto.getSHA256(password).equals(keys.get(get("OPEN_CHAT")))){
//                    addOrSet("CT_CP", password);
//                    startActivity(new Intent(ChatLoginScreen.this, ChatScreenSplashScreen.class));
//                }else Toast.makeText(ChatLoginScreen.this, "Incorrect Password!", Toast.LENGTH_SHORT).show();
                checkLogin();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        ((Button)findViewById(R.id.buttonback)).setOnClickListener((v) ->
            startActivity(new Intent(ChatLoginScreen.this, ChatAppSplashScreen.class))
        );
    }

    private boolean dataRecievedLogin = false;
    private String dataLogin = "";
    private void checkLogin() throws Exception {
        String password = StringUtil.convertTo16chars(((EditText) findViewById(R.id.editTextTextPassword)).getText().toString());
        String passwordHash = Crypto.getSHA256(password);

        try {
            ((DataListenerInterface) TempStorage.get("DLI")).setL(new Client.DataListener() {
                @Override
                public void gotData(JSONObject data) {
                    ChatLoginScreen.this.dataLogin = data.toString();
                    dataRecievedLogin = true;
                }
            });

            HashMap<String, String> sendData = new HashMap<>();
            sendData.put("room", (String) get("OPEN_CHAT"));
            sendData.put("password", passwordHash);
            addOrSet("CHAT_PASSWORD", passwordHash);
            HashMap<String, String> userData = new HashMap<>();
            ((Client) TempStorage.get("NCI")).updateState("LOGIN_ROOM", sendData, userData);

            while (!dataRecievedLogin);
            dataRecievedLogin = false;

            JSONObject obj = new JSONObject(dataLogin);

            TempStorage.addOrSet("SHA256PWD_ROOM", passwordHash);

            if (obj.getString("state").equals("ROOMLOGIN_DNTEXT")) {
                Toast.makeText(ChatLoginScreen.this, "Room doesn't exit!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ChatLoginScreen.this, ChatSelectScreen.class));
                finish();
            } if(obj.getString("state").equals("ROOMLOGIN_FAIL"))
                Toast.makeText(ChatLoginScreen.this, "Incorrect Password!", Toast.LENGTH_SHORT).show();
            if (obj.getString("state").equals("ROOMLOGIN_SUCCESS")) {
                startActivity(new Intent(ChatLoginScreen.this, ChatScreenSplashScreen.class));
                finish();
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}