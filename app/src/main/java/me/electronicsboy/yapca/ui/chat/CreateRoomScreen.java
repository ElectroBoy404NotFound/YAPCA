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

import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

import javax.security.auth.login.LoginException;

import me.electronicsboy.yapca.R;
import me.electronicsboy.yapca.data.LoginDataSource;
import me.electronicsboy.yapca.data.Result;
import me.electronicsboy.yapca.util.Client;
import me.electronicsboy.yapca.util.DataListenerInterface;
import me.electronicsboy.yapca.util.TempStorage;
import me.electronicsboy.yapca.ui.splash.ChatAppSplashScreen;
import me.electronicsboy.yapca.util.Crypto;
import me.electronicsboy.yapca.util.StringUtil;

public class CreateRoomScreen extends AppCompatActivity {
    private boolean dataRecievedRegister = false;
    private String dataRegister = "";

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
//            if(((HashMap<String, String>) TempStorage.get("CHAT_KEYS")).get(((EditText)findViewById(R.id.editTextTextPersonName)).getText().toString()) != null)
//                Toast.makeText(CreateRoomScreen.this, "Room already exists!", Toast.LENGTH_SHORT).show();
//            else {
//                try {
//                    String password = StringUtil.convertTo16chars(((EditText) findViewById(R.id.editTextTextPassword3)).getText().toString());
//                    ((HashMap<String, String>) TempStorage.get("CHAT_KEYS")).put(((EditText)findViewById(R.id.editTextTextPersonName)).getText().toString(), Crypto.getSHA256(password));
//                    FirebaseDatabase.getInstance().getReference("ChatKeys/" + ((EditText)findViewById(R.id.editTextTextPersonName)).getText().toString()).setValue(Crypto.getSHA256(password));
//                    List<String> chats = (List<String>) TempStorage.get("CHATS_DATA");
//                    if(chats == null) chats = new ArrayList<>();
//                    chats.add(((EditText)findViewById(R.id.editTextTextPersonName)).getText().toString());
//                    StringBuilder finalData = new StringBuilder();
//                    for(int i = 0; i < chats.size(); i++) {
//                        finalData.append(Objects.requireNonNull(Crypto.encrypt(chats.get(i), (String) TempStorage.get("PASSWORD_CLEARTXT"))).replace("\n", ""));
//                        if(i < chats.size()-1) finalData.append(',');
//                    }
//                    System.out.println(finalData);
//                    FirebaseDatabase.getInstance().getReference("Chats/" + TempStorage.get("USERNAME")).setValue(finalData.toString());
//                } catch (NoSuchAlgorithmException e) {
//                    e.printStackTrace();
//                }
//            }

            ((DataListenerInterface) TempStorage.get("DLI")).setL(new Client.DataListener() {
                @Override
                public void gotData(JSONObject data) {
                    CreateRoomScreen.this.dataRegister = data.toString();
                    dataRecievedRegister = true;
                }
            });

//            try {
//                String password = StringUtil.convertTo16chars(((EditText) findViewById(R.id.editTextTextPassword3)).getText().toString());
//                HashMap<String, String> sendData = new HashMap<>();
//                sendData.put("username", username);
//                sendData.put("chatpassword", Crypto.getSHA256(password));
//                HashMap<String, String> userData = new HashMap<>();
//                userData.put("username", username);
//                userData.put("password", Crypto.getSHA256(password));
//                ((Client) TempStorage.get("NCI")).updateState("REGISTER", sendData, userData);
//
//                while (!dataRecievedRegister) ;
//                dataRecievedRegister = false;
//
//                JSONObject obj = new JSONObject(dataRegister);
//
//                if (obj.getString("state").equals("USR_EXISTS"))
//                    return new Result.Error(new LoginException("Username with username \"" + username + "\" already exists!"));
//                if (obj.getString("state").equals("LOGIN_SUCCESS"));
//            } catch (Exception myHead) {
//                throw new RuntimeException(myHead);
//            }
        });
        ((Button)findViewById(R.id.buttonback2)).setOnClickListener((v) ->
                startActivity(new Intent(CreateRoomScreen.this, ChatAppSplashScreen.class))
        );
    }
}