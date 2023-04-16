package me.electronicsboy.yapca.util;

import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ReportSystem {
    public static void report(String id, HashMap<String, String> mi) {
        System.out.println(mi);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("DeletedChats/" + TempStorage.get("OPEN_CHAT") + "/" + id).setValue(Crypto.encrypt(mi, (String) TempStorage.get("CT_CP")));
        database.getReference("Chat/" + TempStorage.get("OPEN_CHAT") + "/" + id).removeValue();
        String bannedUsers = ((HashMap<String, String>) TempStorage.get("BANNED_USERS_CHATS")).get(TempStorage.get("OPEN_CHAT"));
        bannedUsers = bannedUsers == null ? "" : bannedUsers + ",";
        bannedUsers += mi.get("user");
        database.getReference("BannedUsers/" + TempStorage.get("OPEN_CHAT")).setValue(bannedUsers);
        ((HashMap<String, String>) TempStorage.get("BANNED_USERS_CHATS")).put((String) TempStorage.get("OPEN_CHAT"), bannedUsers);
    }
}
