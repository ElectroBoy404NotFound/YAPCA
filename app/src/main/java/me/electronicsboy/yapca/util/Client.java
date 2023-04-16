package me.electronicsboy.yapca.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

/*
Based on https://github.com/ElectroGamingStudios/JavaGameEngine/tree/main/src/main/java/me/ElectronicsBoy/PureJavaGameEngine/networking
 */
public class Client {
    private Socket socket;
    private ClientIdentity id;
    private DataListener dl;

    public Client(DataListener dl) throws JSONException, IOException {
        id = new ClientIdentity();
        this.dl = dl;
        this.socket = new Socket("127.0.0.1", 8000);

        new Thread(() -> {
            Scanner ss = null;
            try {
                ss = new Scanner(new BufferedReader(new InputStreamReader(socket.getInputStream())));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String finaljson = "";
            boolean gotData = false;
            while(true) {
                while(ss.hasNext()) {
                    gotData = true;
                    finaljson += ss.next();
                }
                if(gotData) {
                    try {
                        dl.gotData(new JSONObject(finaljson));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    gotData = false;
                }
            }
        }).start();
    }

    public void updateState(String state, HashMap<String, String> data, HashMap<String, String> userdata) throws JSONException, IOException {
        this.id.setState(state, data, userdata);
        this.sendData(this.id.getState().getState());
    }

    private void sendData(String state) throws IOException {
        if(socket != null) {
            PrintWriter printWriter = null;
            printWriter = new PrintWriter(socket.getOutputStream());
            if(socket.isConnected()) {
                printWriter.println(state);
                printWriter.flush();
            }
        }
    }
    public interface DataListener {
        void gotData(JSONObject data);
    }
    public class ClientIdentity {
        private UserState state;
        private boolean loggedIn;
        public ClientIdentity() throws JSONException {
            loggedIn = false;
            this.state = new UserState();
        }
        public void setState(String state, HashMap<String, String> data, HashMap<String, String> userdata) throws JSONException {
            this.state.setState(state, data, userdata);
        }
        public void setState(UserState state) {
            this.state = state;
        }
        public UserState getState() {
            return this.state;
        }
    }
    public class UserState {
        private String state;
        private JSONObject data;
        public UserState() throws JSONException {
            state = "undefined";
            data = new JSONObject();
            data.put("state", state);
            data.put("data", new JSONObject());
            JSONObject userdata = new JSONObject();
            userdata.put("username", "");
            userdata.put("password", "");
            data.put("userdata", userdata);
        }

        public void setState(String state, HashMap<String, String> data, HashMap<String, String> userdata) throws JSONException {
            this.state = state;
            this.data.put("state", state);
            JSONObject dataObject = new JSONObject();
            data.forEach((key, value) -> {
                try {
                    dataObject.put(key, value);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            });
            this.data.put("data", dataObject);
            this.data.put("userdata", userdata);
            System.out.println(this.data.toString());
        }
        public String getState() {
            return this.data.toString();
        }
    }
}
