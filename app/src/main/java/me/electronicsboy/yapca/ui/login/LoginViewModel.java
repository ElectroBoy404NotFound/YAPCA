package me.electronicsboy.yapca.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.security.NoSuchAlgorithmException;

import me.electronicsboy.yapca.data.LoginRepository;
import me.electronicsboy.yapca.data.Result;
import me.electronicsboy.yapca.data.model.LoggedInUser;
import me.electronicsboy.yapca.R;
import me.electronicsboy.yapca.util.Crypto;

public class LoginViewModel extends ViewModel {

    private final MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private final MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private final LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) throws NoSuchAlgorithmException {
        // can be launched in a separate asynchronous job
        if(password.length() < 16)
            while(password.length() < 16)
                password += '0';
        Result<LoggedInUser> result = loginRepository.login(username, password);

        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName(), password, Crypto.getSHA256(password))));
        } else {
            loginResult.setValue(new LoginResult("Login Failed with error: " + ((Result.Error) result).getError().getMessage()));
        }
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@") || username.contains("/") || username.contains(".")) {
            return false;
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() <= 16 && password.trim().length() >= 5;
    }

    public void register(String username, String password) throws NoSuchAlgorithmException {
        if(password.length() < 16)
            while(password.length() < 16)
                password += '0';
        Result<LoggedInUser> result = loginRepository.register(username, password);

        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName(), password, Crypto.getSHA256(password))));
        } else {
            loginResult.setValue(new LoginResult("Register Failed with error: " + ((Result.Error) result).getError().getMessage()));
        }
    }
}