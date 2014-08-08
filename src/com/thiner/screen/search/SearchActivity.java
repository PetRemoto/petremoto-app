
package com.thiner.screen.search;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.common.base.Strings;
import com.thiner.R;
import com.thiner.adapter.PersonAdapter;
import com.thiner.asynctask.GetJSONTask;
import com.thiner.asynctask.GetJSONTask.GetJSONInterface;
import com.thiner.asynctask.PostJSONTask;
import com.thiner.asynctask.PostJSONTask.PostJSONInterface;
import com.thiner.model.Person;
import com.thiner.utils.APIUtils;
import com.thiner.utils.AuthPreferences;
import com.thiner.utils.MyLog;
import com.thiner.utils.ThinerUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class SearchActivity extends Activity implements GetJSONInterface, PostJSONInterface {

    private ImageButton mBtnGoSearch;
    private EditText mSearchText;

    private ListView mList;
    private ProgressBar mProgress;
    private ArrayList<Person> mListPersons;

    private List<View> mViews;
    private PersonAdapter mPersonAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mSearchText = (EditText) findViewById(R.id.TextViewSearch);

        mProgress = (ProgressBar) findViewById(R.id.progressBar);

        mProgress.setVisibility(View.GONE);

        mListPersons = new ArrayList<Person>();

        mPersonAdapter = new PersonAdapter(this, R.layout.adapter_person, mListPersons);

        mList = (ListView) findViewById(android.R.id.list);

        mList.setAdapter(mPersonAdapter);
        mList.setEmptyView(findViewById(android.R.id.empty));
        mList.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(final AdapterView<?> parent,
                    final View view, final int position, final long id) {
                // Use the Builder class for convenient dialog construction
                final AlertDialog.Builder builder = new AlertDialog.Builder(
                        SearchActivity.this);

                builder.setTitle(R.string.add_friend)
                        .setPositiveButton(R.string.add,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(
                                            final DialogInterface dialog,
                                            final int id) {
                                        addFriend(position);
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

        mBtnGoSearch = (ImageButton) findViewById(R.id.button_go);

        mBtnGoSearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                lockAll();
                mProgress.setVisibility(View.VISIBLE);
                mList.setVisibility(View.GONE);
                sendGetUserInformation();
            }

        });

        mViews = new LinkedList<View>();
        mViews.add(mSearchText);
        mViews.add(mBtnGoSearch);
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

    private void sendGetUserInformation() {
        final String search = mSearchText.getText().toString();

        if (Strings.isNullOrEmpty(search)) {
            ThinerUtils.showToast(this, "Search bar empty. :(");
        } else {

            final String url = APIUtils.getApiUrlSearch() + "?"
                    + APIUtils.putAttrs("username", search);

            new GetJSONTask(this).execute(url);
        }
    }

    public void addFriend(final int position) {
        final Person person = mListPersons.get(position);

        final String params = APIUtils.putAttrs("id", AuthPreferences.getID(this))
                + "&" + APIUtils.putAttrs("friend", person.getID());

        new PostJSONTask(this).execute(APIUtils.getApiUrlRequestFriend(), params);
    }

    private void updatePersons(final List<Person> array) {
        if (mListPersons != null && mPersonAdapter != null) {
            mListPersons.clear();

            mListPersons.addAll(array);

            Collections.sort(mListPersons);

            mPersonAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void callbackGetJSON(final JSONObject json) {

        final List<Person> array = new ArrayList<Person>();

        try {

            final JSONArray users = json.getJSONArray("users");

            for (int i = 0; i < users.length(); i++) {
                MyLog.info(users.getJSONObject(i).toString());

                final JSONObject user = users.getJSONObject(i);

                final String id = user.getString("_id");
                final String firstName = user.getString("firstname");
                final String secondName = user.getString("lastname");
                final String username = user.getString("username");
                final String email = user.getString("lastname");
                final String operadora = "TIM"; //user.getString("operadora");

                final Person newPerson = new Person(id, firstName, secondName, username, email,
                        operadora);
                array.add(newPerson);
            }

            updatePersons(array);

        } catch (final JSONException e) {
            e.printStackTrace();
        }

        unlockAll();

        mProgress.setVisibility(View.GONE);
        mList.setVisibility(View.VISIBLE);
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

    @Override
    public void callbackPostJSON(final JSONObject json) {
        MyLog.info(json.toString());

        try {
            if (json.has("status") && json.getString("status").equals("success")) {
                ThinerUtils.showToast(this, json.getString("msg"));
            } else {
                ThinerUtils.showToast(this, json.getString("err"));
            }
        } catch (final JSONException e) {
            e.printStackTrace();
        }
    }
}
