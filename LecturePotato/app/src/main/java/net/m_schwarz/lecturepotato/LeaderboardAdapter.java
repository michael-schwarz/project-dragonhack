package net.m_schwarz.lecturepotato;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.security.MessageDigest;

/**
 * Created by michael on 15.05.16.
 */
public class LeaderboardAdapter extends ArrayAdapter<LeaderPosition> {

        private final Context context;
        private final LeaderPosition[] values;

        public LeaderboardAdapter(Context context, LeaderPosition[] values) {
            super(context, -1, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.leader_board_item, parent, false);

            TextView name = (TextView) rowView.findViewById(R.id.firstLine);
            TextView textView = (TextView) rowView.findViewById(R.id.secondLine);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);


            textView.setText(values[position].points);
            name.setText(values[position].username);

            return rowView;
        }
 }
