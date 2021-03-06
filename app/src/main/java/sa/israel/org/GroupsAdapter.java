package sa.israel.org;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import sa.israel.org.R;

import java.util.ArrayList;

/**
 * Created by ahaliav_fox on 30 אוקטובר 2017.
 */

public class GroupsAdapter extends ArrayAdapter<Group> {
    ArrayList<Group> dataSet;
    double cur_latitude = 0;
    double cur_longitude = 0;
    Activity activity;

    //FragmentManager manager;
    private static class ViewHolder {
        TextView tvDay;
        TextView tvFromTime;
        TextView tvToTime;
        TextView tvComment;
        TextView tvLang;
        TextView tvLocation;
        TextView tvKm;
        ImageButton btnWaze;
        ImageButton btnGooglemaps;
        ImageButton btnEdit;
        ImageButton btnDelete;
        FrameLayout mainframe;
    }

    public GroupsAdapter(ArrayList<Group> data, Activity activity, double cur_latitude, double cur_longitude) {
        super(activity.getApplicationContext(), R.layout.item_group, data);
        this.dataSet = data;
        this.cur_latitude = cur_latitude;
        this.cur_longitude = cur_longitude;
        this.activity = activity;
        //this.manager = manager;
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Group dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        GroupsAdapter.ViewHolder viewHolder; // view lookup cache stored in tag

        //if (convertView == null) {

        viewHolder = new GroupsAdapter.ViewHolder();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.item_group, parent, false);
        viewHolder.tvDay = (TextView) convertView.findViewById(R.id.tvDay);
        viewHolder.tvFromTime = (TextView) convertView.findViewById(R.id.tvFromTime);
        viewHolder.tvToTime = (TextView) convertView.findViewById(R.id.tvToTime);
        viewHolder.tvComment = (TextView) convertView.findViewById(R.id.tvComment);
        viewHolder.tvLang = (TextView) convertView.findViewById(R.id.tvLang);
        viewHolder.tvLocation = (TextView) convertView.findViewById(R.id.tvLocation);
        viewHolder.tvKm = (TextView) convertView.findViewById(R.id.tvKm);
        viewHolder.btnWaze = (ImageButton) convertView.findViewById(R.id.btnWaze);
        viewHolder.btnGooglemaps = (ImageButton) convertView.findViewById(R.id.btnGooglemaps);
        viewHolder.btnEdit = (ImageButton) convertView.findViewById(R.id.btnEdit);
        viewHolder.btnDelete = (ImageButton) convertView.findViewById(R.id.btnDelete);
        viewHolder.mainframe = (FrameLayout) convertView.findViewById(R.id.mainframe);

        viewHolder.btnEdit.setTag(position);
        viewHolder.btnDelete.setTag(position);
        viewHolder.btnWaze.setTag(position);
        viewHolder.btnGooglemaps.setTag(position);

        convertView.setTag(viewHolder);
        //} else {
        //   viewHolder = (GroupsAdapter.ViewHolder) convertView.getTag();
        //}

        viewHolder.tvDay.setText(dataModel.getDay());
        viewHolder.tvFromTime.setText(dataModel.getFromTime());
        viewHolder.tvToTime.setText(dataModel.getToTime());

        if (MainActivity.IsLoggedIn())
            viewHolder.tvComment.setText(dataModel.getComment());
        else
            viewHolder.tvComment.setText("");

        viewHolder.tvLang.setText(dataModel.getLang());
        viewHolder.tvLocation.setText(dataModel.getLocation());

        if (activity.getResources().getString(R.string.local) == "he")
            viewHolder.tvKm.setText(" קמ" + String.valueOf(dataModel.getKm()));
        else
            viewHolder.tvKm.setText(String.valueOf(dataModel.getKm()) + " " + activity.getResources().getString(R.string.KM));

        viewHolder.btnWaze.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final int position = (int) v.getTag();
                Group dataModel = getItem(position);
                if (MainActivity.IsLoggedIn()) {
                    String w_uri = "geo: " + dataModel.getLatitude() + "," + dataModel.getLongitude() + "&navigate=yes";

                    String g_uri = "http://maps.google.com/maps?saddr="
                            + cur_latitude + ","
                            + cur_longitude + "&daddr="
                            + dataModel.getLatitude() + "," + dataModel.getLongitude();

                    try {
                        Intent navigation = new Intent(Intent.ACTION_VIEW, Uri.parse(w_uri));
                        navigation.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(navigation);
                    } catch (Exception ex) {
                        try {
                            Intent navigation = new Intent(Intent.ACTION_VIEW, Uri.parse(g_uri));
                            navigation.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            activity.startActivity(navigation);
                        } catch (Exception ex2) {
                            Toast.makeText(activity, "Error loading navigation",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    shoeLoginMessage();
                }


            }
        });

        viewHolder.btnGooglemaps.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                final int position = (int) v.getTag();
                Group dataModel = getItem(position);
                if (MainActivity.IsLoggedIn()) {
                    String g_uri = "http://maps.google.com/maps?saddr="
                            + cur_latitude + ","
                            + cur_longitude + "&daddr="
                            + dataModel.getLatitude() + "," + dataModel.getLongitude();

                    try {
                        Intent navigation = new Intent(Intent.ACTION_VIEW, Uri.parse(g_uri));
                        navigation.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(navigation);
                    } catch (Exception ex2) {
                        Toast.makeText(activity, "Error loading navigation",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    shoeLoginMessage();
                }

            }
        });


        viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (MainActivity.IsLoggedIn()) {

                    final int position = (int) v.getTag();
                    Group dataModel = getItem(position);

                    try {
                        String message = activity.getResources().getString(R.string.city) + ": " + dataModel.getLocation() + "\n";
                        message += activity.getResources().getString(R.string.day) + ": " + dataModel.getDay() + "\n";
                        message += activity.getResources().getString(R.string.fromtime) + ": " + dataModel.getFromTime() + "\n";
                        message += activity.getResources().getString(R.string.totime) + ": " + dataModel.getToTime() + "\n";
                        message += activity.getResources().getString(R.string.mynameis) + ": \n";
                        message += activity.getResources().getString(R.string.iwoulsliketoupdate) + ": \n";
                        message += activity.getResources().getString(R.string.myphoneis) + ": \n";

                        Uri uri = Uri.parse("mailto:?subject=" + activity.getResources().getString(R.string.hisaiwouldliketoupdateagroup) + "&body=" + message + "&to=website@sa-israel.org");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(Intent.createChooser(intent, ""));
                    } catch (Exception ex) {
                        Toast.makeText(activity, ex.getMessage().toString(),
                                Toast.LENGTH_LONG).show();
                    }

                } else {
                    shoeLoginMessage();
                }


            }
        });

        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (MainActivity.IsLoggedIn()) {
                    try {
                        final int position = (int) v.getTag();
                        Group dataModel = getItem(position);
                        String message = activity.getResources().getString(R.string.city) + ": " + dataModel.getLocation() + "\n";
                        message += activity.getResources().getString(R.string.day) + ": " + dataModel.getDay() + "\n";
                        message += activity.getResources().getString(R.string.fromtime) + ": " + dataModel.getFromTime() + "\n";
                        message += activity.getResources().getString(R.string.totime) + ": " + dataModel.getToTime() + "\n";
                        message += activity.getResources().getString(R.string.mynameis) + ": \n";
                        message += activity.getResources().getString(R.string.iwoulsliketoupdate) + ": \n";
                        message += activity.getResources().getString(R.string.myphoneis) + ": \n";

                        Uri data = Uri.parse("mailto:?subject=" + activity.getResources().getString(R.string.hisaiwouldliketoupdatethisremoved) + "&body=" + message + "&to=office@sa-israel.org;website@sa-israel.org");
                        Intent intent = new Intent(Intent.ACTION_VIEW, data);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(Intent.createChooser(intent, ""));
                    } catch (Exception ex) {
                        Toast.makeText(activity, ex.getMessage().toString(),
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    shoeLoginMessage();
                }
            }
        });


        return convertView;
    }

    private void shoeLoginMessage() {
        Toast.makeText(activity, activity.getResources().getString(R.string.you_are_not_loggedin),
                Toast.LENGTH_LONG).show();
    }
}

