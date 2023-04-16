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

import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;

import me.electronicsboy.yapca.R;
import me.electronicsboy.yapca.util.Client;
import me.electronicsboy.yapca.util.DataListenerInterface;
import me.electronicsboy.yapca.util.TempStorage;
import me.electronicsboy.yapca.ui.login.LoginActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users");
        try {
            DataListenerInterface DLI = new DataListenerInterface();
            TempStorage.addOrSet("NCI", new Client(DLI));
            TempStorage.addOrSet("DLI", DLI);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
//                ((TextView) findViewById(R.ids.status)).setText("Oh no! Connect to the internet and restart the app to continue!");
            }
        });
    }

}