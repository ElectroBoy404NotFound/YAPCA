package me.electronicsboy.yapca.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {
    private final String displayName;
    private final String passwordClearText;
    private final String passwordHash;
    //... other data fields that may be accessible to the UI

    LoggedInUserView(String displayName, String passwordClearText, String passwordHash) {
        this.displayName = displayName;
        this.passwordClearText = passwordClearText;
        this.passwordHash = passwordHash;
    }

    String getDisplayName() {
        return displayName;
    }
    String getPasswordClearText() {
        return passwordClearText;
    }
    String getPasswordHash() {
        return passwordHash;
    }
}