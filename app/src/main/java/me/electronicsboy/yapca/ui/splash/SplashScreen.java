package me.electronicsboy.yapca.ui.splash;

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

import java.util.HashMap;

import me.electronicsboy.yapca.R;
import me.electronicsboy.yapca.TempStorage;
import me.electronicsboy.yapca.ui.login.LoginActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, String> logins = new HashMap<>();
                for(DataSnapshot snap : dataSnapshot.getChildren())
                    logins.put(snap.getKey(), (String) snap.getValue());
                TempStorage.addOrSet("LOGIN_DATA", logins);
                myRef.removeEventListener(this);
                startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
//                Toast.makeText(LoginActivity.this, "ERROR!\n" + error.toException().getMessage(), Toast.LENGTH_LONG).show();
                Log.w(null,"Failed to read value.", error.toException());
                ((TextView) findViewById(R.id.status)).setText("Oh no! Connect to the internet and restart the app to continue!");
            }
        });
    }

}