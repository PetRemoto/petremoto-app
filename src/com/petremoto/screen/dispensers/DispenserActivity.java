
package com.petremoto.screen.dispensers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.petremoto.adapter.DispenserAdapter;
import com.petremoto.asynctask.GetJSONTask;
import com.petremoto.asynctask.GetJSONTask.GetJSONInterface;
import com.petremoto.asynctask.PostJSONTask.PostJSONInterface;
import com.petremoto.model.Dispenser;
import com.petremoto.screen.feeding.FeedingActivity;
import com.petremoto.utils.APIUtils;
import com.petremoto.utils.AuthPreferences;
import com.petremoto.utils.MyLog;
import com.petremoto.utils.ThinerUtils;
import com.thiner.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The Class MainActivity.
 */
public final class DispenserActivity extends Activity implements
        GetJSONInterface,
        PostJSONInterface {

    private ArrayList<Dispenser> mListDispenser;
    private DispenserAdapter mDispenserAdapter;
    private ListView mList;
    private ProgressBar mProgress;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispensers);

        mListDispenser = new ArrayList<Dispenser>();

        mDispenserAdapter = new DispenserAdapter(this,
                R.layout.adapter_dispencer,
                mListDispenser);

        mList = (ListView) findViewById(android.R.id.list);

        mList.setAdapter(mDispenserAdapter);
        mList.setEmptyView(findViewById(android.R.id.empty));

        mList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                    int position, long id) {
                Dispenser newDispenser = mListDispenser.get(position);
                feed(v, newDispenser);
            }

        });

        mProgress = (ProgressBar) findViewById(R.id.progressBar);

    }

    private void feed(View v, Dispenser dispenser) {
        final Intent intent = new Intent(DispenserActivity.this,
                FeedingActivity.class);
        intent.putExtra("serial", dispenser.getSerial());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestDispenser();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();// Close The DatabaseloginDataBaseAdapter.close();
    }

    /*
     * (non-Javadoc)
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.contact, menu);
        return true;
    }

    @Override
    public void callbackGetJSON(final JSONObject json) {
        MyLog.warning(json.toString());

        final List<Dispenser> array = new ArrayList<Dispenser>();

        try {
            final JSONArray dispensers = json.getJSONObject("users")
                    .getJSONArray(
                            "dispensers");

            for (int i = 0; i < dispensers.length(); i++) {
                // MyLog.info(friends.getJSONObject(i).toString());

                final JSONObject dispenser = dispensers.getJSONObject(i);

                final String serial = dispenser.getString("serial");
                final String id = dispenser.getString("_id");
                final String name = dispenser.getString("name");
                final String lastTimeChecked = dispenser
                        .getString("last_time_check");
                final String lastTimeFed = dispenser
                        .getString("last_time_fed");
                final Boolean feed = dispenser.getBoolean("feed");
                final String status = dispenser.getString("status");

                final Dispenser newDispenser = new Dispenser(serial, name, id,
                        lastTimeChecked, lastTimeFed, feed, status);
                array.add(newDispenser);
            }

            updateDispenser(array);

        } catch (final JSONException e) {
            e.printStackTrace();
        }

        mProgress.setVisibility(View.GONE);
        mList.setVisibility(View.VISIBLE);
    }

    public void requestDispenser() {
        mList.setVisibility(View.GONE);
        mProgress.setVisibility(View.VISIBLE);

        final String url = APIUtils.getApiUrl() + "?"
                + APIUtils.putAttrs("id", AuthPreferences.getID(this));

        new GetJSONTask(this).execute(url);
    }

    @Override
    public void callbackPostJSON(final JSONObject json) {
        MyLog.info(json.toString());

        try {
            if (json.has("status")
                    && json.getString("status").equals("success")) {
                ThinerUtils.showToast(this, json.getString("msg"));
                requestDispenser();
            } else {
                ThinerUtils.showToast(this, json.getString("err"));
            }
        } catch (final JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateDispenser(final List<Dispenser> array) {
        if (mListDispenser != null && mDispenserAdapter != null) {
            mListDispenser.clear();

            mListDispenser.addAll(array);

            Collections.sort(mListDispenser);

            mDispenserAdapter.notifyDataSetChanged();
        }
    }
}
