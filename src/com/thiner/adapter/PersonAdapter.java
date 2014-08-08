
package com.thiner.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.thiner.R;
import com.thiner.model.Person;

import java.util.ArrayList;

public class PersonAdapter extends ArrayAdapter<Person> {

    private final Context mContext;
    private final int mLayoutResourceId;
    private final ArrayList<Person> mList;

    public PersonAdapter(final Context context, final int layoutResourceId,
            final ArrayList<Person> mListPersons) {
        super(context, layoutResourceId, mListPersons);

        mContext = context;
        mLayoutResourceId = layoutResourceId;
        mList = mListPersons;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        View row = convertView;

        PersonHolder holder = null;

        if (row == null) {

            final LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(mLayoutResourceId, parent, false);

            holder = new PersonHolder();

            View view = row.findViewById(R.id.textViewFirstName);
            if (view instanceof TextView) {
                holder.txtFirstName = (TextView) view;
            }

            view = row.findViewById(R.id.textViewSecondName);
            if (view instanceof TextView) {
                holder.txtSecondName = (TextView) view;
            }

            view = row.findViewById(R.id.textViewOperadora);
            if (view instanceof TextView) {
                holder.txtOperadora = (TextView) view;
            }

            row.setTag(holder);
        } else {
            holder = (PersonHolder) row.getTag();
        }

        final Person person = mList.get(position);

        holder.txtFirstName.setText(person.getFirstName());
        holder.txtSecondName.setText(person.getSecondName());
        holder.txtOperadora.setText(person.getOperadora());

        return row;

    }

    /**
     * The Class PlayerHolder.
     */
    static class PersonHolder {

        TextView txtFirstName;

        TextView txtSecondName;

        TextView txtOperadora;

    }
}
