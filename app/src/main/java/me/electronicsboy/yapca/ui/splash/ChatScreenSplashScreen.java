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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.electronicsboy.yapca.R;
import me.electronicsboy.yapca.TempStorage;
import me.electronicsboy.yapca.ui.chat.ChatScreen;

public class ChatScreenSplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen_splash_screen);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Chat/" + TempStorage.get("OPEN_CHAT"));
//        Instant instant = Instant.ofEpochMilli(System.currentTimeMillis());
//        System.out.println(TimeZone.getDefault().getID());
//        ZoneId zoneId = ZoneId.of(TimeZone.getDefault().getID()); // Or "Asia/Kolkata", "Europe/Paris", and so on.
//        ZonedDateTime zdt = ZonedDateTime.ofInstant( instant , zoneId );
//        HashMap<String, String> data = new HashMap<>();
//        data.put("user", Crypto.encrypt((String) TempStorage.get("USERNAME"), (String) TempStorage.get("CT_CP")));
//        data.put("msg", Crypto.encrypt("Hello, World!", (String) TempStorage.get("CT_CP")));
//        data.put("")
//        myRef.push().setValue(data);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<HashMap> chatData = new ArrayList<>();
                for(DataSnapshot snap : dataSnapshot.getChildren())
                    chatData.add((HashMap) snap.getValue());
                TempStorage.addOrSet("CHAT_DATA", chatData);
                myRef.removeEventListener(this);
                startActivity(new Intent(ChatScreenSplashScreen.this, ChatScreen.class));
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
//                Toast.makeText(LoginActivity.this, "ERROR!\n" + error.toException().getMessage(), Toast.LENGTH_LONG).show();
                Log.w(null,"Failed to read value.", error.toException());
//                ((TextView) findViewById(R.id.status)).setText("Oh no! Connect to the internet and restart the app to continue!");
            }
        });
    }
}