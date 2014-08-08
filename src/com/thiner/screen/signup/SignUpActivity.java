
package com.thiner.screen.signup;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.thiner.R;
import com.thiner.asynctask.PostJSONTask;
import com.thiner.asynctask.PostJSONTask.PostJSONInterface;
import com.thiner.utils.APIUtils;
import com.thiner.utils.ThinerUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class SignUpActivity extends Activity implements PostJSONInterface {
    private EditText mEdUsername;
    private EditText mEdFirstName;
    private EditText mEdLastName;
    private EditText mEdEmail;
    private EditText mEdPassword;
    private EditText mEdConfirmPassword;
    private Button btCreateAccount;
    private List<View> mViews;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        mEdUsername = (EditText) findViewById(R.id.editTextUserName);
        mEdFirstName = (EditText) findViewById(R.id.etFirstName);
        mEdLastName = (EditText) findViewById(R.id.etLastName);
        mEdEmail = (EditText) findViewById(R.id.etEmail);
        mEdPassword = (EditText) findViewById(R.id.etPassword);
        mEdConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
        btCreateAccount = (Button) findViewById(R.id.buttonCreateAccount);
        btCreateAccount.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                lockAll();
                sendPOSTUserInformation();

            }
        });

        mViews = new LinkedList<View>();
        mViews.add(mEdUsername);
        mViews.add(mEdFirstName);
        mViews.add(mEdLastName);
        mViews.add(mEdEmail);
        mViews.add(mEdPassword);
        mViews.add(mEdConfirmPassword);
        mViews.add(btCreateAccount);
    }

    public void sendPOSTUserInformation() {
        final String username = mEdUsername.getText().toString().trim();
        final String password = mEdPassword.getText().toString().trim();
        final String confPassword = mEdConfirmPassword.getText().toString().trim();
        final String email = mEdEmail.getText().toString().trim();
        final String firstname = mEdFirstName.getText().toString().trim();
        final String lastname = mEdLastName.getText().toString().trim();
        String params = "";

        if (Strings.isNullOrEmpty(username)) {
            ThinerUtils.showToast(this, "Username bar empty. :(");
        } else if (Strings.isNullOrEmpty(firstname)) {
            ThinerUtils.showToast(this, "First Name bar empty. :(");
        } else if (Strings.isNullOrEmpty(lastname)) {
            ThinerUtils.showToast(this, "Last Name bar empty. :(");
        } else if (Strings.isNullOrEmpty(email)) {
            ThinerUtils.showToast(this, "Email bar empty. :(");
        } else if (Strings.isNullOrEmpty(password)) {
            ThinerUtils.showToast(this, "Password bar empty. :(");
        } else if (Strings.isNullOrEmpty(confPassword)) {
            ThinerUtils.showToast(this, "Confirm Password bar empty. :(");
        } else if (!confPassword.equals(password)) {
            ThinerUtils.showToast(this, "Confirm Password and Password are different. :(");
        } else {
            params += "&" + APIUtils.putAttrs("username", username);
            params += "&" + APIUtils.putAttrs("password", password);
            params += "&" + APIUtils.putAttrs("email", email);
            params += "&" + APIUtils.putAttrs("firstname", firstname);
            params += "&" + APIUtils.putAttrs("lastname", lastname);

            final String url = APIUtils.getApiUrl();

            new PostJSONTask(this).execute(url, params);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void callbackPostJSON(final JSONObject msg) {
        final JSONObject status = Preconditions.checkNotNull(msg);

        try {
            if (status.has("status") && status.getString("status").equalsIgnoreCase("failed")) {
                ThinerUtils.showToast(this, status.getString("err"));
            } else {
                ThinerUtils.showToast(this, status.getString("msg"));

            }
        } catch (final JSONException e) {
            e.printStackTrace();
        }
        unlockAll();
        finish();

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
