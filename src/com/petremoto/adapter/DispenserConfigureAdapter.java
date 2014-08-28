
package com.petremoto.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.petremoto.R;
import com.petremoto.model.Dispenser;
import com.petremoto.screen.dispenders.configure.DispenserConfigureActivity;

import java.util.ArrayList;

public class DispenserConfigureAdapter extends ArrayAdapter<Dispenser> {

    private final Context mContext;
    private final int mLayoutResourceId;
    private final ArrayList<Dispenser> mList;

    public DispenserConfigureAdapter(final Context context,
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

            View view = row.findViewById(R.id.textViewDispenserConfigureName);
            if (view instanceof TextView) {
                holder.textViewDispenserConfigureName = (TextView) view;
            }

            view.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(final View view) {
                    notifyDataSetChanged();
                    DispenserConfigureActivity.setSelectedDispenser(position);
                    view.setSelected(true);
                    ((Activity) mContext).invalidateOptionsMenu();
                    notifyDataSetChanged();

                    return true;
                }
            });

            row.setTag(holder);
        } else {
            holder = (DispenserHolder) row.getTag();
        }

        final Dispenser dispenser = mList.get(position);

        holder.textViewDispenserConfigureName.setText(dispenser.getName());

        return row;

    }

    /**
     * The Class PlayerHolder.
     */
    static class DispenserHolder {
        TextView textViewDispenserConfigureName;
    }
}
