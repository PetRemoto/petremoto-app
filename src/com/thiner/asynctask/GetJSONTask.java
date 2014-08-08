
package com.thiner.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.thiner.utils.MyLog;
import com.thiner.utils.ThinerUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class GetJSONTask extends AsyncTask<String, Void, String> {
    private final Context mContext;

    public GetJSONTask(final Context context) {
        mContext = context;
    }

    @Override
    protected String doInBackground(final String... urls) {
        // params comes from the execute() call: params[0] is the url.

        int trys = 10;
        String result = null;

        while (trys > 0) {
            trys--;

            MyLog.info("GET: " + urls[0]);

            result = ThinerUtils.downloadUrl(urls[0]);

            if (result == null) {

                try {
                    Thread.sleep(1000);
                } catch (final InterruptedException e) {
                    MyLog.error(e.getMessage());
                }
            } else {
                break;
            }

        }

        return result;
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(final String result) {
        JSONObject mainObject;

        try {
            if (result != null) {
                mainObject = new JSONObject(result);
            } else {
                mainObject = new JSONObject("{'status': 'failed'}");
            }
            ((GetJSONInterface) mContext).callbackGetJSON(mainObject);

        } catch (final JSONException e) {
            MyLog.debug(result);
            MyLog.error(e.getMessage());

        }
    }

    public interface GetJSONInterface {
        public void callbackGetJSON(JSONObject json);
    }
}
