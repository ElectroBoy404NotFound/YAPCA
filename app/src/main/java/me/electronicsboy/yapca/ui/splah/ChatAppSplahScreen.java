package me.electronicsboy.yapca.ui.splah;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import me.electronicsboy.yapca.R;
import me.electronicsboy.yapca.TempStorage;
import me.electronicsboy.yapca.ui.chat.ChatSelectScreen;
import me.electronicsboy.yapca.ui.login.LoginActivity;
import me.electronicsboy.yapca.util.Crypto;
import me.electronicsboy.yapca.util.StringUtil;

public class ChatAppSplahScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_app_splah_screen);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Chats/" + TempStorage.get("USERNAME"));
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    List<String> chats = new ArrayList<String>();
                    for(String s : ((String)dataSnapshot.getValue()).split(","))
                        try {
                            chats.add(Crypto.decrypt(s, (String) TempStorage.get("PASSWORD_CLEARTXT")));
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    TempStorage.addOrSet("CHATS_DATA", chats);
                }
                myRef.removeEventListener(this);
                startActivity(new Intent(ChatAppSplahScreen.this, ChatSelectScreen.class));
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(null,"Failed to read value.", error.toException());
                ((TextView) findViewById(R.id.status)).setText("Oh no! Connect to the internet and restart the app to continue!");
            }
        });
    }
}