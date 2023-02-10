package me.electronicsboy.yapca.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import me.electronicsboy.yapca.TempStorage;
import me.electronicsboy.yapca.databinding.ActivityLoginBinding;
import me.electronicsboy.yapca.ui.splah.ChatAppSplahScreen;

public class LoginActivity extends AppCompatActivity {
    private LoginViewModel loginViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        me.electronicsboy.yapca.databinding.ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;
        final Button registerButton = binding.register;
        final ProgressBar loadingProgressBar = binding.loading;

        loginViewModel.getLoginFormState().observe(this, loginFormState -> {
            if (loginFormState == null) {
                return;
            }
            loginButton.setEnabled(loginFormState.isDataValid());
            registerButton.setEnabled(loginFormState.isDataValid());

            if (loginFormState.getUsernameError() != null) {
                usernameEditText.setError(getString(loginFormState.getUsernameError()));
            }
            if (loginFormState.getPasswordError() != null) {
                passwordEditText.setError(getString(loginFormState.getPasswordError()));
            }
        });

        loginViewModel.getLoginResult().observe(this, loginResult -> {
            if (loginResult == null) {
                return;
            }
            loadingProgressBar.setVisibility(View.GONE);
            if (loginResult.getError() != null) {
                showLoginFailed(loginResult.getError());
                loginButton.setEnabled(true);
                registerButton.setEnabled(true);
            }
            if (loginResult.getSuccess() != null) {
                updateUiWithUser(loginResult.getSuccess());
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                try {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
            }
            return false;
        });

        loginButton.setOnClickListener(v -> {
            v.setEnabled(false);
            registerButton.setEnabled(false);
            loadingProgressBar.setVisibility(View.VISIBLE);
            try {
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        });
        registerButton.setOnClickListener(v -> {
            v.setEnabled(false);
            loginButton.setEnabled(false);
            loadingProgressBar.setVisibility(View.VISIBLE);
            try {
                loginViewModel.register(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {
        TempStorage.addOrSet("USERNAME", model.getDisplayName());
        TempStorage.addOrSet("PASSWORD_CLEARTXT", model.getPasswordClearText());
        TempStorage.addOrSet("PASSWORD_HASH", model.getPasswordHash());
        startActivity(new Intent(this, ChatAppSplahScreen.class));
        finish();
    }

    private void showLoginFailed(String errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}