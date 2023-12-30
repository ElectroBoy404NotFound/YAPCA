package me.electronicsboy.yapca.ui.splash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.security.auth.login.LoginException;

import me.electronicsboy.yapca.R;
import me.electronicsboy.yapca.data.LoginDataSource;
import me.electronicsboy.yapca.util.Client;
import me.electronicsboy.yapca.util.DataListenerInterface;
import me.electronicsboy.yapca.util.TempStorage;
import me.electronicsboy.yapca.ui.chat.ChatSelectScreen;
import me.electronicsboy.yapca.util.Crypto;

public class ChatAppSplashScreen extends AppCompatActivity {
    private boolean dataRecievedChats = false;
    private String dataChats = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_app_splah_screen);
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("Chats/" + TempStorage.get("USERNAME"));
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    List<String> chats = new ArrayList<>();
//                    for (String s : ((String) Objects.requireNonNull(dataSnapshot.getValue())).split(","))
//                        try {
//                            System.out.println(s);
//                            chats.add(Crypto.decrypt(s, (String) TempStorage.get("PASSWORD_CLEARTXT")));
//                        } catch (Exception e) {
//                            throw new RuntimeException(e);
//                        }
//                    TempStorage.addOrSet("CHATS_DATA", chats);
//                }
//                DatabaseReference myRef2 = database.getReference("ChatKeys/");
//                myRef2.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        if (dataSnapshot.exists()) {
//                            HashMap<String, String> keys = new HashMap<>();
//                            for(DataSnapshot s : dataSnapshot.getChildren())
//                                keys.put(s.getKey(), (String) s.getValue());
//                            TempStorage.addOrSet("CHAT_KEYS", keys);
//                        }
//                        myRef2.removeEventListener(this);
//                        DatabaseReference myRef3 = database.getReference("BannedUsers/");
//                        HashMap<String, String> bannedUsers = new HashMap<>();
//                        TempStorage.addOrSet("BANNED_USERS_CHATS", bannedUsers);
//                        myRef3.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot1) {
//                                if(dataSnapshot.exists())
//                                    for(DataSnapshot s : snapshot1.getChildren()) {
//                                        System.out.println(s);
//                                        System.out.println(bannedUsers);
//                                        bannedUsers.put(s.getKey(), (String) s.getValue());
//                                        System.out.println(bannedUsers);
//                                    }
//                                TempStorage.addOrSet("BANNED_USERS_CHATS", bannedUsers);
//                                myRef3.removeEventListener(this);
//                                startActivity(new Intent(ChatAppSplashScreen.this, ChatSelectScreen.class));
//                                finish();
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        Log.w(null, "Failed to read value.", error.toException());
////                        ((TextView) findViewById(R.id.status)).setText("Oh no! Connect to the internet and restart the app to continue!");
//                    }
//                });
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.w(null, "Failed to read value.", error.toException());
////                ((TextView) findViewById(R.id.status)).setText("Oh no! Connect to the internet and restart the app to continue!");
//            }
//        });

        ((DataListenerInterface) TempStorage.get("DLI")).setL(new Client.DataListener() {
            @Override
            public void gotData(JSONObject data) {
                ChatAppSplashScreen.this.dataChats = data.toString();
                dataRecievedChats = true;
            }
        });
        try {
            HashMap<String, String> sendData = new HashMap<>();
            sendData.put("username", (String) TempStorage.get("USERNAME"));
            sendData.put("password", (String) TempStorage.get("USERNAME"));
            HashMap<String, String> userData = new HashMap<>();
            userData.put("username", (String) TempStorage.get("USERNAME"));
            userData.put("password", (String) TempStorage.get("USERNAME"));
            ((Client) TempStorage.get("NCI")).updateState("GETROOMS", sendData, userData);

            while(!dataRecievedChats);
            dataRecievedChats = false;

            JSONObject obj = new JSONObject(dataChats);
            if(!obj.getString("state").equals("GETROOMS_SUCCESS")) throw new Exception("Get rooms failed!");

            JSONArray arr = obj.getJSONArray("data");
            List<String> chats = new ArrayList<>();
            for(int i = 0; i < arr.length(); i++)
                chats.add(Crypto.decrypt(arr.getString(i), (String) TempStorage.get("SHA256PWD")));
            TempStorage.addOrSet("CHATS_DATA", chats);

            startActivity(new Intent(ChatAppSplashScreen.this, ChatSelectScreen.class));
            finish();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}