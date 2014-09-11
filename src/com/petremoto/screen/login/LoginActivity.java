//OK

package com.petremoto.screen.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.petremoto.R;
import com.petremoto.asynctask.GetJSONTask;
import com.petremoto.asynctask.GetJSONTask.GetJSONInterface;
import com.petremoto.screen.main.MainActivity;
import com.petremoto.screen.signup.SignUpActivity;
import com.petremoto.utils.APIUtils;
import com.petremoto.utils.AuthPreferences;
import com.petremoto.utils.PetRemotoUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

/**
 * The Class MainActivity.
 */
public final class LoginActivity extends Activity implements GetJSONInterface {

    private EditText mTxtLogin;
    private EditText mTxtPassword;

    private BootstrapButton mBtnSignIn;
    private BootstrapButton mBtnSignUp;

    private int mLoginFailCount;

    private List<View> mViews;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Start Login Count
        mLoginFailCount = 0;

        // Get The Refference Of TextEdits
        mTxtLogin = (EditText) findViewById(R.id.editTextLogin);
        mTxtPassword = (EditText) findViewById(R.id.editTextPassword);

        // Get The Refference Of Buttons
        mBtnSignIn = (BootstrapButton) findViewById(R.id.btnSignIn);
        mBtnSignUp = (BootstrapButton) findViewById(R.id.btnSignUp);

        mViews = new LinkedList<View>();
        mViews.add(mTxtLogin);
        mViews.add(mTxtPassword);
        mViews.add(mBtnSignIn);
        mViews.add(mBtnSignUp);

        mBtnSignIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                lockAll();
                signIn(v);
            }
        });

        // Set OnClick Listener on SignUp button
        mBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // / Create Intent for SignUpActivity and Start The Activity
                final Intent intentSignUP = new Intent(getApplicationContext(),
                        SignUpActivity.class);
                startActivity(intentSignUP);
            }
        });
    }

    // Method to handleClick Event of Sign In Button
    public void signIn(final View V) {
        final String username = mTxtLogin.getText().toString();
        final String password = mTxtPassword.getText().toString();

        if (Strings.isNullOrEmpty(username)) {
            PetRemotoUtils.showToast(this, "Missing username.");
        } else if (Strings.isNullOrEmpty(password)) {
            PetRemotoUtils.showToast(this, "Missing password.");
        } else {

            final String url = APIUtils.getApiUrlLogin() + "?"
                    + APIUtils.putAttrs("username", username) + "&"
                    + APIUtils.putAttrs("password", password);

            new GetJSONTask(this).execute(url);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();// Close The DatabaseloginDataBaseAdapter.close();
    }

    @Override
    public void callbackGetJSON(final JSONObject newJson) {
        final JSONObject json = Preconditions.checkNotNull(newJson);

        try {
            if (json.has("users") && json.getJSONArray("users").length() == 1) {
                startApp(json.getJSONArray("users").getJSONObject(0)
                        .getString("_id"));
            } else {
                loginFail();
            }
        } catch (final JSONException e) {
            e.printStackTrace();
        }

        unlockAll();
    }

    private void loginFail() {
        mLoginFailCount++;

        if (mLoginFailCount > 3) {
            PetRemotoUtils.showToast(this, "Do you have account here?");
        } else {
            PetRemotoUtils.showToast(this, "Username or Password incorrect.");
        }

        mTxtLogin.setText("");
        mTxtPassword.setText("");

    }

    private void startApp(final String id) {

        AuthPreferences.setId(this, id);
        final Intent intent = new Intent(LoginActivity.this,
                MainActivity.class);
        startActivity(intent);
    }

    private void lockAll() {
        for (final View v : mViews) {
            v.setEnabled(false);
        }
    }

    private void unlockAll() {
        for (final View v : mViews) {
            v.setEnabled(true);
        }
    }
}
