package sa.israel.org;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import sa.israel.org.R;

import java.util.ArrayList;

/**
 * Created by ahaliav_fox on 26 אוקטובר 2017.
 */

public class CalenderAdapter extends ArrayAdapter<Subriety> {
    private ArrayList<Subriety> dataSet;
    Context mContext;

    private static class ViewHolder {
        TextView tvTitle;
        TextView tvDays;
        TextView tvMoreDetails;
        FrameLayout mainframe;
    }

    public CalenderAdapter(ArrayList<Subriety> data, Context context) {
        super(context, R.layout.item_subriety, data);
        this.dataSet = data;
        this.mContext = context;
    }



    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Subriety dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        //dataModel = getSubriety(dataModel.getId());
        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_subriety, parent, false);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.tvDays = (TextView) convertView.findViewById(R.id.tvDays);
            viewHolder.tvMoreDetails = (TextView) convertView.findViewById(R.id.tvMoreDetails);
            viewHolder.mainframe = (FrameLayout) convertView.findViewById(R.id.mainframe);
            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        lastPosition = position;

        viewHolder.tvTitle.setText(dataModel.getName());
        viewHolder.tvDays.setText(dataModel.getDays());
        viewHolder.tvMoreDetails.setText(dataModel.getMorDetails());

       // viewHolder.mainframe.setOnClickListener(this);
        viewHolder.mainframe.setTag(position);

        // Return the completed view to render on screen
        return convertView;
    }


}
