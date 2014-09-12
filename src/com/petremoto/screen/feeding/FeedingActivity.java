//OK

package com.petremoto.screen.feeding;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.common.base.Preconditions;
import com.petremoto.R;
import com.petremoto.asynctask.PostJSONTask;
import com.petremoto.asynctask.PostJSONTask.PostJSONInterface;
import com.petremoto.utils.APIUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public final class FeedingActivity extends Activity implements
PostJSONInterface {
    private TextView textViewFeeding;
    private List<View> mViews;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeding);

        final Intent intent = getIntent();
        final String serial = intent.getStringExtra("serial");

        textViewFeeding = (TextView) findViewById(R.id.text_view_feeding);
        mViews = new LinkedList<View>();
        mViews.add(textViewFeeding);

        sendPOSTFeedRequest(serial);
    }

    public void sendPOSTFeedRequest(final String serial) {
        final String params = "&" + APIUtils.putAttrs("serial", serial)
                + "&" + APIUtils.putAttrs("feed", "true");

        new PostJSONTask(this).execute(APIUtils.getApiUrlFeeding(), params);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();// Close The DatabaseloginDataBaseAdapter.close();
    }

    @Override
    public void callbackPostJSON(final JSONObject json) {
        final JSONObject status = Preconditions.checkNotNull(json);

        try {
            if (status.has("status")
                    && status.getString("status").equalsIgnoreCase("failed")) {
                //                PetRemotoUtils.showToast(this, status.getString("err"));
                textViewFeeding.setText("Houve algum problema, tente novamente mais tarde. :(");
            } else {
                //                PetRemotoUtils.showToast(this, status.getString("msg"));
                textViewFeeding.setText("Alimentado!");
            }
        } catch (final JSONException e) {
            e.printStackTrace();
        }
    }

}
