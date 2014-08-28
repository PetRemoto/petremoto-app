
package com.petremoto.screen.person;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.petremoto.R;
import com.petremoto.adapter.PersonAdapter;
import com.petremoto.asynctask.GetJSONTask;
import com.petremoto.asynctask.GetJSONTask.GetJSONInterface;
import com.petremoto.asynctask.PostJSONTask.PostJSONInterface;
import com.petremoto.model.Person;
import com.petremoto.utils.APIUtils;
import com.petremoto.utils.AuthPreferences;
import com.petremoto.utils.MyLog;
import com.petremoto.utils.ThinerUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The Class MainActivity.
 */
public final class PersonActivity extends Activity implements GetJSONInterface,
        PostJSONInterface {

    private ArrayList<Person> mListPersons;
    private PersonAdapter mPersonAdapter;
    private ListView mList;
    private ProgressBar mProgress;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        mListPersons = new ArrayList<Person>();

        mPersonAdapter = new PersonAdapter(this, R.layout.adapter_person,
                mListPersons);

        mList = (ListView) findViewById(android.R.id.list);

        mList.setAdapter(mPersonAdapter);
        mList.setEmptyView(findViewById(android.R.id.empty));
        mList.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(final AdapterView<?> parent,
                    final View view, final int position, final long id) {
                // Use the Builder class for convenient dialog construction
                final AlertDialog.Builder builder = new AlertDialog.Builder(
                        PersonActivity.this);

                builder.setTitle(R.string.remove_friend)
                        .setPositiveButton(R.string.remove,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(
                                            final DialogInterface dialog,
                                            final int id) {
                                        removeFriend(position);
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

                return true;
            }

        });

        mProgress = (ProgressBar) findViewById(R.id.progressBar);

    }

    @Override
    protected void onResume() {
        super.onResume();

        requestContacts();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();// Close The DatabaseloginDataBaseAdapter.close();
    }

    /*
     * (non-Javadoc)
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    // @Override
    // public boolean onOptionsItemSelected(final MenuItem item) {
    // switch (item.getItemId()) {
    // case android.R.id.home:
    // NavUtils.navigateUpFromSameTask(this);
    // return true;
    // case R.id.search:
    // final Intent intentSearch = new Intent(PersonActivity.this,
    // SearchActivity.class);
    // startActivity(intentSearch);
    // return true;
    // case R.id.profile:
    // final Intent intentProfile = new Intent(PersonActivity.this,
    // ProfileActivity.class);
    // startActivity(intentProfile);
    // return true;
    // case R.id.request:
    // final Intent intentRequest = new Intent(PersonActivity.this,
    // RequestActivity.class);
    // startActivity(intentRequest);
    // return true;
    // default:
    // return super.onOptionsItemSelected(item);
    // }

    // }

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

    public void updatePerson(final List<Person> array) {
        if (mListPersons != null && mPersonAdapter != null) {
            mListPersons.clear();

            mListPersons.addAll(array);

            Collections.sort(mListPersons);

            mPersonAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void callbackGetJSON(final JSONObject json) {
        MyLog.warning(json.toString());

        final List<Person> array = new ArrayList<Person>();

        try {
            final JSONArray friends = json.getJSONObject("users").getJSONArray(
                    "friends");

            for (int i = 0; i < friends.length(); i++) {
                // MyLog.info(friends.getJSONObject(i).toString());

                final JSONObject friend = friends.getJSONObject(i);

                final String id = friend.getString("_id");
                final String firstName = friend.getString("firstname");
                final String secondName = friend.getString("lastname");
                final String username = friend.getString("username");
                final String email = friend.getString("lastname");
                final String operadora = "TIM"; // friend.getString("operadora");

                final Person newPerson = new Person(id, firstName, secondName,
                        username, email,
                        operadora);
                array.add(newPerson);
            }

            updatePerson(array);

        } catch (final JSONException e) {
            e.printStackTrace();
        }

        mProgress.setVisibility(View.GONE);
        mList.setVisibility(View.VISIBLE);
    }

    public void removeFriend(final int position) {
        final Person person = mListPersons.get(position);

        final String params = APIUtils.putAttrs("id",
                AuthPreferences.getID(this))
                + "&" + APIUtils.putAttrs("friend", person.getID());

        // new PostJSONTask(this).execute(APIUtils.getApiUrlRemoveFriend(),
        // params);
    }

    public void requestContacts() {
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
                requestContacts();
            } else {
                ThinerUtils.showToast(this, json.getString("err"));
            }
        } catch (final JSONException e) {
            e.printStackTrace();
        }

    }
}
