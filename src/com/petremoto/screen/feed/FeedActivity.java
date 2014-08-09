//OK

package com.petremoto.screen.feed;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.petremoto.screen.dispensers.DispenserActivity;
import com.petremoto.screen.person.PersonActivity;
import com.thiner.R;

import java.util.LinkedList;
import java.util.List;

public final class FeedActivity extends Activity {

    private Button mBtnConfigure, mBtnFeed, mBtnSchedule;

    private List<View> mViews;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_activity);

        // Get The Refference Of Buttons
        mBtnFeed = (Button) findViewById(R.id.btnFeed);
        mBtnSchedule = (Button) findViewById(R.id.btnSchedule);
        mBtnConfigure = (Button) findViewById(R.id.btnConfigure);

        mViews = new LinkedList<View>();
        mViews.add(mBtnFeed);
        mViews.add(mBtnSchedule);
        mViews.add(mBtnConfigure);

        mBtnFeed.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                feed(v);
            }
        });

        mBtnSchedule.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                schedule(v);
            }
        });

        mBtnConfigure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View v) {
                configure(v);
            }
        });

    }

    private void feed(View v) {
        final Intent intent = new Intent(FeedActivity.this,
                DispenserActivity.class);
        startActivity(intent);
    }

    private void configure(View v) {
        final Intent intent = new Intent(FeedActivity.this,
                PersonActivity.class);
        startActivity(intent);
    }

    private void schedule(View v) {
        final Intent intent = new Intent(FeedActivity.this,
                PersonActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();// Close The DatabaseloginDataBaseAdapter.close();
    }

}
