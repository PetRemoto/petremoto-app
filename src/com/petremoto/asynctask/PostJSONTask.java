
package com.petremoto.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.petremoto.utils.MyLog;
import com.petremoto.utils.PetRemotoUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class PostJSONTask extends AsyncTask<String, Void, String> {
    private final Context mContext;

    public PostJSONTask(final Context context) {
        mContext = context;
    }

    @Override
    protected String doInBackground(final String... paramns) {

        int trys = 10;
        String result = null;

        while (trys > 0) {
            trys--;

            try {

                MyLog.info("POST: " + paramns[0] + " - " + paramns[1]);

                result = PetRemotoUtils.sendPOST(paramns[0], paramns[1]);

                if (result != null) {
                    break;
                }

            } catch (final IOException e) {
                MyLog.debug("Unable to retrieve web page. URL may be invalid: "
                        + PetRemotoUtils.getServerURL() + " - " + paramns[0]);
            }

            try {
                Thread.sleep(1000);
            } catch (final InterruptedException e) {
                MyLog.error(e.getMessage());
            }
        }

        return result;
    }

    @Override
    protected void onPostExecute(final String msg) {
        try {
            ((PostJSONInterface) mContext).callbackPostJSON(new JSONObject(msg));
        } catch (final JSONException e) {
            MyLog.error("Error when creating JSON on PostJSONTask", e);
        }

    }

    public interface PostJSONInterface {
        public void callbackPostJSON(JSONObject json);
    }
}
