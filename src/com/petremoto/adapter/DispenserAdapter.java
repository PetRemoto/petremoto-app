
package com.petremoto.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.petremoto.model.Dispenser;
import com.petremoto.utils.ThinerUtils;
import com.thiner.R;

import java.util.ArrayList;

import org.joda.time.DateTime;

public class DispenserAdapter extends ArrayAdapter<Dispenser> {

    private final Context mContext;
    private final int mLayoutResourceId;
    private final ArrayList<Dispenser> mList;

    public DispenserAdapter(final Context context,
            final int layoutResourceId,
            final ArrayList<Dispenser> mListDispenser) {
        super(context, layoutResourceId, mListDispenser);

        mContext = context;
        mLayoutResourceId = layoutResourceId;
        mList = mListDispenser;
    }

    @Override
    public View getView(final int position, final View convertView,
            final ViewGroup parent) {

        View row = convertView;

        DispenserHolder holder = null;

        if (row == null) {

            final LayoutInflater inflater = ((Activity) mContext)
                    .getLayoutInflater();
            row = inflater.inflate(mLayoutResourceId, parent, false);

            holder = new DispenserHolder();

            View view = row.findViewById(R.id.textViewDispenserName);
            if (view instanceof TextView) {
                holder.textViewDispenserName = (TextView) view;
            }

            view = row.findViewById(R.id.textViewLastTimeFed);
            if (view instanceof TextView) {
                holder.textViewLastTimeFed = (TextView) view;
            }

            row.setTag(holder);
        } else {
            holder = (DispenserHolder) row.getTag();
        }

        final Dispenser dispenser = mList.get(position);

        holder.textViewDispenserName.setText(dispenser.getName());

        holder.textViewLastTimeFed.setText(new DateTime(dispenser
                .getLastTimeFed()).toString(ThinerUtils.getDateTimeFormat()));

        return row;

    }

    /**
     * The Class PlayerHolder.
     */
    static class DispenserHolder {
        TextView textViewDispenserName;
        TextView textViewLastTimeFed;
    }
}
