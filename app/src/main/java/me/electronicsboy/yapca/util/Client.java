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

    public Client(DataListener dl) {
        this.dl = dl;
        try {
            Client.this.id = new ClientIdentity();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        new Thread(() -> {
            try {
                Client.this.socket = new Socket("192.168.1.33", 8000);
                System.out.println("Connected");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Scanner ss = null;
            try {
                ss = new Scanner(new BufferedReader(new InputStreamReader(socket.getInputStream())));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            while(true) {
                while(ss.hasNext()) {
                    System.out.println("I got something");
                    try {
                        String line = "";
                        System.out.println("I got something");
                        System.out.println(ss.hasNext());
                        while(ss.hasNext()) {System.out.println(ss.hasNext()); System.out.println("formed: " + line); line += ss.next();}
                        System.out.println("I got something");
                        System.out.println("I got: " + line);
                        dl.gotData(new JSONObject(line));
                    } catch (Exception e) {
                        System.out.println("I crash");
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
    }

    public void updateState(String state, HashMap<String, String> data, HashMap<String, String> userdata) throws JSONException, IOException {
        this.id.setState(state, data, userdata);
        this.sendData(this.id.getState().getState());
    }

    private void sendData(String state) throws IOException {
        System.out.println("SEND");
        new Thread(() -> {
            System.out.println("SEND");
            while(socket == null);
            System.out.println("SEND");
            PrintWriter printWriter = null;
            try {
                System.out.println("SEND");
                printWriter = new PrintWriter(socket.getOutputStream());
                System.out.println("SEND");
                printWriter.println(state);
                System.out.println("SEND");
                printWriter.flush();
                System.out.println("SEND");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
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
            JSONObject userObject = new JSONObject();
            userdata.forEach((key, value) -> {
                try {
                    userObject.put(key, value);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            });
            this.data.put("data", dataObject);
            this.data.put("userdata", userObject);
        }
        public String getState() {
            return this.data.toString();
        }
    }
}
