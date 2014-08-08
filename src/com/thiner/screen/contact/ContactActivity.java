
package com.thiner.screen.contact;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.thiner.R;
import com.thiner.asynctask.PostJSONTask;
import com.thiner.asynctask.PostJSONTask.PostJSONInterface;
import com.thiner.utils.APIUtils;
import com.thiner.utils.AuthPreferences;
import com.thiner.utils.MyLog;
import com.thiner.utils.ThinerUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

/**
 * The Class MainActivity.
 */
public final class ContactActivity extends Activity implements PostJSONInterface {

    private EditText mEditTxtLastName;
    private EditText mEditTxtFirstName;

    private Button mBtnSaveChanges;
    private Button mBtnEditNumber;

    private List<View> mViews;
    private EditText mEditTxtConfirmNewPassword;
    private EditText mEditTxtNewPassword;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mEditTxtFirstName = (EditText) findViewById(R.id.editTextFirstName);
        mEditTxtLastName = (EditText) findViewById(R.id.editTextLastName);
        mEditTxtNewPassword = (EditText) findViewById(R.id.editTextNewPassword);
        mEditTxtConfirmNewPassword = (EditText) findViewById(R.id.editTextConfirmNewPassword);

        mBtnSaveChanges = (Button) findViewById(R.id.btnSaveNameChange);

        mBtnSaveChanges.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                saveChanges();
            }
        });

        mBtnEditNumber = (Button) findViewById(R.id.btnEditNumber);

        mViews = new LinkedList<View>();
        mViews.add(mEditTxtFirstName);
        mViews.add(mEditTxtLastName);
        mViews.add(mEditTxtNewPassword);
        mViews.add(mEditTxtConfirmNewPassword);
        mViews.add(mBtnSaveChanges);
        mViews.add(mBtnEditNumber);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();// Close The DatabaseloginDataBaseAdapter.close();
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void saveChanges() {
        final String firstname = mEditTxtFirstName.getText().toString().trim();
        final String lastname = mEditTxtLastName.getText().toString().trim();
        final String newPass = mEditTxtNewPassword.getText().toString().trim();
        final String confirNewPass = mEditTxtConfirmNewPassword.getText().toString().trim();

        String params = APIUtils.putAttrs("id", AuthPreferences.getID(this));

        if (!Strings.isNullOrEmpty(firstname)) {
            params += "&" + APIUtils.putAttrs("firstname", firstname);
        }

        if (!Strings.isNullOrEmpty(lastname)) {
            params += "&" + APIUtils.putAttrs("lastname", lastname);

        }

        if (Strings.isNullOrEmpty(newPass) != Strings.isNullOrEmpty(confirNewPass)) {

            if (Strings.isNullOrEmpty(newPass)) {
                ThinerUtils.showToast(this, "You forget your new pass.");
                return;
            } else if (Strings.isNullOrEmpty(confirNewPass)) {
                ThinerUtils.showToast(this, "You forget to confirm your pass.");
                return;
            }

        } else if (!Strings.isNullOrEmpty(newPass) && !Strings.isNullOrEmpty(confirNewPass)) {

            if (newPass.equals(confirNewPass)) {
                params += "&" + APIUtils.putAttrs("password", newPass);
            } else {
                ThinerUtils.showToast(this, "Your confirm pass dont match.");
                return;
            }
        }

        if (params.length() == APIUtils.putAttrs("id", AuthPreferences.getID(this)).length()) {
            ThinerUtils.showToast(this, "You should change something.");
            return;
        }

        new PostJSONTask(this).execute(APIUtils.getApiUrlEditProfile(), params);

        lockAll();
    }

    @Override
    public void callbackPostJSON(final JSONObject json) {
        final JSONObject status = Preconditions.checkNotNull(json);

        MyLog.debug(json.toString());

        try {
            if (status.has("status") && status.getString("status").equalsIgnoreCase("success")) {
                ThinerUtils.showToast(this, "Profile edited.");
            } else {
                ThinerUtils.showToast(this, "Something wrong. :(");
            }
        } catch (final JSONException e) {
            e.printStackTrace();
        }

        mEditTxtFirstName.setText("");
        mEditTxtLastName.setText("");
        mEditTxtNewPassword.setText("");
        mEditTxtConfirmNewPassword.setText("");

        unlockAll();
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
