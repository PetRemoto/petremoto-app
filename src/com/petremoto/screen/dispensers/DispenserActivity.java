
package com.petremoto.screen.dispensers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.petremoto.R;
import com.petremoto.adapter.DispenserAdapter;
import com.petremoto.asynctask.GetJSONTask;
import com.petremoto.asynctask.GetJSONTask.GetJSONInterface;
import com.petremoto.asynctask.PostJSONTask.PostJSONInterface;
import com.petremoto.model.Dispenser;
import com.petremoto.screen.feeding.FeedingActivity;
import com.petremoto.utils.APIUtils;
import com.petremoto.utils.AuthPreferences;
import com.petremoto.utils.MyLog;
import com.petremoto.utils.PetRemotoUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The Class MainActivity.
 */
public final class DispenserActivity extends Activity implements
        GetJSONInterface,
        PostJSONInterface {

    private ArrayList<Dispenser> mListDispenser;
    private DispenserAdapter mDispenserAdapter;
    private ListView mList;
    private ProgressDialog mDialog;
    private RelativeLayout mLayout;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispensers);

        mLayout = (RelativeLayout) findViewById(R.id.layout_top);

        mListDispenser = new ArrayList<Dispenser>();

        mDispenserAdapter = new DispenserAdapter(this,
                R.layout.adapter_dispencer,
                mListDispenser);

        mList = (ListView) findViewById(android.R.id.list);

        mList.setAdapter(mDispenserAdapter);
        mList.setEmptyView(findViewById(android.R.id.empty));

        mList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View v,
                    final int position, final long id) {

                // Use the Builder class for convenient dialog construction
                final AlertDialog.Builder builder = new AlertDialog.Builder(
                        DispenserActivity.this);

                builder.setTitle(R.string.feed_dispenser)
                        .setPositiveButton(R.string.continuar,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(
                                            final DialogInterface dialog,
                                            final int id) {
                                        final Dispenser newDispenser = mListDispenser.get(position);
                                        feed(v, newDispenser);
                                    }
                                })
                        .setNegativeButton(R.string.cancel,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(
                                            final DialogInterface dialog,
                                            final int id) {
                                        // User cancelled the dialog
                                    }
                                });
                // Create the AlertDialog object and return it
                builder.create().show();

            }

        });

    }

    private void feed(final View v, final Dispenser dispenser) {
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

    @Override
    public void callbackGetJSON(final JSONObject json) {
        MyLog.warning(json.toString());

        final List<Dispenser> array = new ArrayList<Dispenser>();

        try {
            final JSONArray dispensers = json.getJSONObject("users")
                    .getJSONArray(
                            "dispensers");

            for (int i = 0; i < dispensers.length(); i++) {
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

        mDialog.dismiss();
        mLayout.setVisibility(View.VISIBLE);
    }

    public void requestDispenser() {
        mLayout.setVisibility(View.INVISIBLE);

        mDialog = new ProgressDialog(this);
        mDialog.setTitle(R.string.request_dispensers);
        mDialog.setMessage(getResources().getString(R.string.please_wait));
        mDialog.setCancelable(false);
        mDialog.show();

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
                PetRemotoUtils.showToast(this, json.getString("msg"));
                requestDispenser();
            } else {
                PetRemotoUtils.showToast(this, json.getString("err"));
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
