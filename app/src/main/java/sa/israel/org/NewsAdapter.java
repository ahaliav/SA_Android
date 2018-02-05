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
 * Created by ahaliav_fox on 25 אוקטובר 2017.
 */

public class NewsAdapter extends ArrayAdapter<News> {

    private ArrayList<News> dataSet;
    Context mContext;

    private static class ViewHolder {
        TextView tvTitle;
        Float ID;
        FrameLayout mainframe;
    }

    public NewsAdapter(ArrayList<News> data, Context context) {
        super(context, R.layout.item_news, data);
        this.dataSet = data;
        this.mContext = context;
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        News dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_news, parent, false);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.mainframe = (FrameLayout) convertView.findViewById(R.id.mainframe);
            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        lastPosition = position;

        viewHolder.tvTitle.setText(dataModel.getTitle());
        viewHolder.ID = dataModel.getId();
        viewHolder.mainframe.setTag(position);

        // Return the completed view to render on screen
        return convertView;
    }
}
