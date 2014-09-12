
package com.petremoto.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.petremoto.R;
import com.petremoto.model.Dispenser;
import com.petremoto.utils.CircleImageView;
import com.petremoto.utils.PetRemotoUtils;

import org.joda.time.DateTime;

import java.util.ArrayList;

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

            view = row.findViewById(R.id.imageViewDispenserStatus);
            if (view instanceof ImageView) {
                holder.imgViewStatus = (CircleImageView) view;
            }

            view = row.findViewById(R.id.textViewDispenserStatus);
            if (view instanceof TextView) {
                holder.textViewDispenserStatus = (TextView) view;
            }

            row.setTag(holder);
        } else {
            holder = (DispenserHolder) row.getTag();
        }

        final Dispenser dispenser = mList.get(position);

        holder.textViewDispenserName.setText(dispenser.getName());

        holder.textViewLastTimeFed.setText(new DateTime(dispenser
                .getLastTimeFed()).toString(PetRemotoUtils.getDateTimeFormat()));

        if ("NORMAL".equalsIgnoreCase(dispenser.getStatus())) {
            holder.imgViewStatus.setImageResource(R.drawable.green);
            holder.textViewDispenserStatus.setText("Normal");
        } else if ("EMPTY".equalsIgnoreCase(dispenser.getStatus())) {
            holder.imgViewStatus.setImageResource(R.drawable.orange);
            holder.textViewDispenserStatus.setText("Vazio");
        } else if ("OFFLINE".equalsIgnoreCase(dispenser.getStatus())) {
            holder.imgViewStatus.setImageResource(R.drawable.grey);
            holder.textViewDispenserStatus.setText("Offline");
        } else if ("ALMOST_EMPTY".equalsIgnoreCase(dispenser.getStatus())) {
            holder.imgViewStatus.setImageResource(R.drawable.yellow);
            holder.textViewDispenserStatus.setText("Quase Vazio");
        } else if ("BLOCKED".equalsIgnoreCase(dispenser.getStatus())) {
            holder.imgViewStatus.setImageResource(R.drawable.red);
            holder.textViewDispenserStatus.setText("Bloqueado");
        } else {
            holder.imgViewStatus.setImageResource(R.drawable.grey);
            holder.textViewDispenserStatus.setText("Offline");
        }

        return row;

    }

    /**
     * The Class PlayerHolder.
     */
    static class DispenserHolder {
        TextView textViewDispenserName;
        TextView textViewLastTimeFed;
        CircleImageView imgViewStatus;
        TextView textViewDispenserStatus;
    }
}
