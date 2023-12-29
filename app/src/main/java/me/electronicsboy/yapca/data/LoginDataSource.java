package me.electronicsboy.yapca.data;

import androidx.annotation.NonNull;

import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import me.electronicsboy.yapca.util.Client;
import me.electronicsboy.yapca.util.DataListenerInterface;
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
    private boolean dataRecievedLogin = false;
    private String dataLogin = "";
    public Result<LoggedInUser> login(String username, @NonNull String password) {
//        try {
//            HashMap<String, String> sendData = new HashMap<>();
//            sendData.put("username", username);
//            sendData.put("password", Crypto.getSHA256(password));
//            HashMap<String, String> userData = new HashMap<>();
//            userData.put("username", username);
//            userData.put("password", Crypto.getSHA256(password));
//
//            ((Client)TempStorage.get("NCI")).updateState("LOGIN", sendData, userData);
//            HashMap<String, String> loginData = (HashMap<String, String>) TempStorage.get("LOGIN_DATA");
//            String actualPassword = loginData.get(username);
//            if (actualPassword == null)
//                return new Result.Error(new IOException("User \"" + username + "\" does not exist!"));
//            if (!Crypto.getSHA256(password).equals(actualPassword))
//                return new Result.Error(new IOException("Incorrect password for user \"" + username + "\"!"));
//            return new Result.Success<>(new LoggedInUser(username));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new Result.Error(new IOException("Error logging in", e));
//        }

        try {
            ((DataListenerInterface) TempStorage.get("DLI")).setL(new Client.DataListener() {
                @Override
                public void gotData(JSONObject data) {
                    LoginDataSource.this.dataLogin = data.toString();
                    dataRecievedLogin = true;
                }
            });

            HashMap<String, String> sendData = new HashMap<>();
            sendData.put("username", username);
            sendData.put("password", Crypto.getSHA256(password));
            HashMap<String, String> userData = new HashMap<>();
            userData.put("username", username);
            userData.put("password", Crypto.getSHA256(password));
            ((Client) TempStorage.get("NCI")).updateState("LOGIN", sendData, userData);

            while (!dataRecievedLogin);
            dataRecievedLogin = false;

            JSONObject obj = new JSONObject(dataLogin);

            TempStorage.addOrSet("SHA256PWD", Crypto.getSHA256(password));

            if (obj.getString("state").equals("LOGIN_USRDNTEXT"))
                return new Result.Error(new LoginException("Username with username \"" + username + "\" does not exists!"));
            if(obj.getString("state").equals("LOGIN_RE_FAIL"))
                return new Result.Error(new LoginException("Server thinks relogin!"));
            if(obj.getString("state").equals("LOGIN_FAIL"))
                return new Result.Error(new LoginException("Incorrect password!"));
            if (obj.getString("state").equals("LOGIN_SUCCESS"))
                return new Result.Success<>(new LoggedInUser(username));
        }catch (Exception e) {
        }
        return new Result.Error(new LoginException("Invalid!"));
    }

    private boolean dataRecievedRegister = false;
    private String dataRegister = "";
    public Result<LoggedInUser> register(String username, @NonNull String password) throws JSONException, IOException, NoSuchAlgorithmException {
        if(password.length() < 16) password = StringUtil.convertTo16chars(password);
        ((DataListenerInterface) TempStorage.get("DLI")).setL(new Client.DataListener() {
            @Override
            public void gotData(JSONObject data) {
                LoginDataSource.this.dataRegister = data.toString();
                dataRecievedRegister = true;
            }
        });

        HashMap<String, String> sendData = new HashMap<>();
        sendData.put("username", username);
        sendData.put("password", Crypto.getSHA256(password));
        HashMap<String, String> userData = new HashMap<>();
        userData.put("username", username);
        userData.put("password", Crypto.getSHA256(password));
        ((Client) TempStorage.get("NCI")).updateState("REGISTER", sendData, userData);

        while(!dataRecievedRegister);
        dataRecievedRegister = false;

        JSONObject obj = new JSONObject(dataRegister);

        if(obj.getString("state").equals("USR_EXISTS"))
            return new Result.Error(new LoginException("Username with username \"" + username + "\" already exists!"));
        if(obj.getString("state").equals("LOGIN_SUCCESS"))
            return login(username, password);

        return new Result.Error(new LoginException("Invalid state!"));
    }
}