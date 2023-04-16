package me.electronicsboy.yapca.data;

import androidx.annotation.NonNull;

import com.google.firebase.database.FirebaseDatabase;

import me.electronicsboy.yapca.util.TempStorage;
import me.electronicsboy.yapca.data.model.LoggedInUser;
import me.electronicsboy.yapca.util.Crypto;
import me.electronicsboy.yapca.util.StringUtil;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import javax.security.auth.login.LoginException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    public Result<LoggedInUser> login(String username, @NonNull String password) {
        try {
            HashMap<String, String> loginData = (HashMap<String, String>) TempStorage.get("LOGIN_DATA");
            String actualPassword = loginData.get(username);
            if (actualPassword == null)
                return new Result.Error(new IOException("User \"" + username + "\" does not exist!"));
            if (!Crypto.getSHA256(password).equals(actualPassword))
                return new Result.Error(new IOException("Incorrect password for user \"" + username + "\"!"));
            return new Result.Success<>(new LoggedInUser(username));
        } catch (Exception e) {
            e.printStackTrace();
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public Result<LoggedInUser> register(String username, @NonNull String password) {
        if(password.length() < 16) password = StringUtil.convertTo16chars(password);
        HashMap<String, String> loginData = (HashMap<String, String>) TempStorage.get("LOGIN_DATA");
        if(loginData.get(username) != null)
            return new Result.Error(new LoginException("Username with username \"" + username + "\" already exists!"));
        try {
            FirebaseDatabase.getInstance().getReference("Users/" + username).setValue(Crypto.getSHA256(password));
            loginData.put(username, Crypto.getSHA256(password));
            TempStorage.addOrSet("LOGIN_DATA", loginData);
        } catch (NoSuchAlgorithmException e) {
            return new Result.Error(new Exception("SHA256 Algorithm error!"));
        }
        return login(username, password);
    }
}