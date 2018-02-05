package sa.israel.org;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ahaliav_fox on 14 נובמבר 2017.
 */

public class EmailContactAdapter  extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    private HashMap<Integer, EmailContact> _listDataChild;
    FragmentManager manager;
    Activity activity;
    private ArrayList<EmailContact> dataSet;

    public EmailContactAdapter(Activity activity, Context context, List<String> listDataHeader,
                               HashMap<Integer, EmailContact> listChildData, FragmentManager manager) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.manager = manager;
        this.activity = activity;
    }

    @Override
    public int getGroupCount() {
        return _listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        try{
            String headerTitle = (String) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.item_emailcontact_group, null);
            }

            TextView tvListHeader = (TextView) convertView
                    .findViewById(R.id.tvListHeader);
            //tvListHeader.setTypeface(null, Typeface.BOLD);
            tvListHeader.setText(headerTitle);
        }
        catch (Exception ex){
            Toast.makeText(_context, ex.getMessage(),
                    Toast.LENGTH_LONG).show();
        }


        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final EmailContact contact = (EmailContact) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_emailcontact, null);
        }

        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        TextView tvEmail = (TextView) convertView.findViewById(R.id.tvEmail);
        FrameLayout mainframe = (FrameLayout) convertView.findViewById(R.id.mainframe);

        tvTitle.setText(contact.getTitle());
        tvEmail.setText(contact.getEmail());

        ImageButton btnEmail=(ImageButton)convertView.findViewById(R.id.btnEmail);


        btnEmail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try{
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri data = Uri.parse("mailto:?subject=" + "" + "&body=" + "" + "&to=" + contact.getEmail());
                    intent.setData(data);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(Intent.createChooser(intent, ""));
                }
                catch (Exception ex){
                    Toast.makeText(_context, ex.getMessage(),
                            Toast.LENGTH_LONG).show();
                }

            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

}
