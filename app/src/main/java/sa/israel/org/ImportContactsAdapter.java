package sa.israel.org;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Ahaliav on 15/02/2018.
 */

public class ImportContactsAdapter extends ArrayAdapter<ImportContact> {
    private ArrayList<ImportContact> dataSet;
    private ArrayList<ImportContact> imports;
    Context mContext;
    private int selectedPosition = -1;
    private final boolean[] mCheckedState;

    private final static class ViewHolder {
        TextView tvPhone;
        TextView tvName;
        CheckBox chkImport;
        FrameLayout mainframe;
    }

    public ImportContactsAdapter(ImportContactsFragment fragment, ArrayList<ImportContact> data, Context context) {
        super(context, R.layout.item_import_contact, data);
        this.dataSet = data;
        this.mContext = context;
        imports = new ArrayList<ImportContact>();
        mCheckedState = new boolean[data.size()];
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final ImportContact dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        final ImportContactsAdapter.ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {

            viewHolder = new ImportContactsAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_import_contact, parent, false);
            viewHolder.tvPhone = (TextView) convertView.findViewById(R.id.tvPhone);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            viewHolder.mainframe = (FrameLayout) convertView.findViewById(R.id.mainframe);
            viewHolder.chkImport = (CheckBox) convertView.findViewById(R.id.chkImport);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ImportContactsAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.chkImport.setChecked(mCheckedState[position]);

        lastPosition = position;

        viewHolder.mainframe.setTag(position);
        viewHolder.tvPhone.setText(dataModel.getPhoneNumber());
        viewHolder.tvName.setText(dataModel.getName());
        viewHolder.mainframe.setTag(position);
        viewHolder.chkImport.setTag(position);
        viewHolder.chkImport.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try{
                    selectedPosition = (int) v.getTag();
                    ImportContact model = getItem(selectedPosition);
                    model.setPosition(selectedPosition);
                    if(viewHolder.chkImport.isChecked()){
                        mCheckedState[selectedPosition] = true;
                        imports.add(model);
                        SQLiteDbHelper db = new SQLiteDbHelper(mContext);
                        db.insertContact(model.getName(),model.getPhoneNumber(),"", model.getEmail());
                    }else {
                        mCheckedState[selectedPosition] = false;
                        imports.remove(model);
                        SQLiteDbHelper db = new SQLiteDbHelper(mContext);
                        db.deleteContact(-1,model.getPhoneNumber());
                    }
                }
                catch (Exception ex){

                }
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }
}
