package sa.israel.org;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;

import sa.israel.org.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class GroupsFragment extends Fragment implements SearchView.OnQueryTextListener, ICallbackMethod {

    ListView listview = null;
    SearchView searchgroup = null;
    ArrayList<Group> list = null;
    GroupsAdapter adapter = null;
    Filter filter = null;
    private ProgressBar spinner;
    LocationManager locationManager;
    Menu menu;
    double cur_latitude = 0;
    double cur_longitude = 0;

    private FusedLocationProviderClient mFusedLocationClient;

    LatLng l1 = null;
    LatLng l2 = null;

    public GroupsFragment() {
        // Required empty public constructor
    }


    public static GroupsFragment newInstance(String param1, String param2) {
        GroupsFragment fragment = new GroupsFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_groups, container, false);

        listview = (ListView) v.findViewById(R.id.listviewGroups);
        spinner = (ProgressBar) v.findViewById(R.id.progressBar);
        spinner.setVisibility(View.VISIBLE);

        searchgroup = (SearchView) v.findViewById(R.id.searchgroup);
        searchgroup.setOnQueryTextListener(this);
        list = new ArrayList<Group>();

        FloatingActionButton floatingActionButton = (FloatingActionButton) v.findViewById(R.id.floatingActionButton);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String message = getResources().getString(R.string.city) + ": \n";
                message += getResources().getString(R.string.day) + ": \n";
                message += getResources().getString(R.string.fromtime) + ": \n";
                message += getResources().getString(R.string.totime) + ": \n";
                message += getResources().getString(R.string.mynameis) + ": \n";
                message += getResources().getString(R.string.myphoneis) + ": \n";

                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("mailto:?subject=" + getResources().getString(R.string.hisaiwouldliketoupdatenewgoup) + "&body=" + message + "&to=office@sa-israel.org;website@sa-israel.org");
                intent.setData(data);

                startActivity(Intent.createChooser(intent, ""));
            }
        });

        loadgroups();

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    123);
        } else {

            if (MainActivity.IsLoggedIn()) {
                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

                mFusedLocationClient.getLastLocation()
                        .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location loc) {
                                // Got last known location. In some rare situations this can be null.
                                if (loc != null) {
                                    cur_latitude = loc.getLatitude();
                                    cur_longitude = loc.getLongitude();

                                    loadgroups_from_db();
                                }
                            }
                        });
            }

        }

        setHasOptionsMenu(true);
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);

        this.menu = menu;

        MenuItem action_add = menu.findItem(R.id.action_add);
        action_add.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                String message = getResources().getString(R.string.city) + ": \n";
                message += getResources().getString(R.string.day) + ": \n";
                message += getResources().getString(R.string.fromtime) + ": \n";
                message += getResources().getString(R.string.totime) + ": \n";
                message += getResources().getString(R.string.mynameis) + ": \n";
                message += getResources().getString(R.string.myphoneis) + ": \n";

                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("mailto:?subject=" + getResources().getString(R.string.hisaiwouldliketoupdatenewgoup) + "&body=" + message + "&to=office@sa-israel.org;website@sa-israel.org");
                intent.setData(data);

                startActivity(Intent.createChooser(intent, ""));
                return false;
            default:
                break;
        }

        return false;
    }

    public final static double AVERAGE_RADIUS_OF_EARTH = 6371;

    public float calculateDistance(double userLat, double userLng, double venueLat, double venueLng) {

        double latDistance = Math.toRadians(userLat - venueLat);
        double lngDistance = Math.toRadians(userLng - venueLng);

        double a = (Math.sin(latDistance / 2) * Math.sin(latDistance / 2)) +
                (Math.cos(Math.toRadians(userLat))) *
                        (Math.cos(Math.toRadians(venueLat))) *
                        (Math.sin(lngDistance / 2)) *
                        (Math.sin(lngDistance / 2));

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)) * 1.609;

        return (float) (Math.round(AVERAGE_RADIUS_OF_EARTH * c));

    }

    private void loadgroups() {
        SQLiteDbHelper db = new SQLiteDbHelper(this.getContext());

        Cursor result = db.selectSettings("group");
        String val = "";
        boolean loadgroups = true;
        if (result != null) {

            while (result.moveToNext()) {
                val = result.getString(2);
            }

            if (!result.isClosed()) {
                result.close();
            }

            try {
                if (val == "" || getDays(val) > 1) {
                    WebSiteHelper helper = new WebSiteHelper(this, getContext());
                    helper.getGroups();
                } else {
                    loadgroups_from_db();
                }
            } catch (Exception ex) {

            }
        }
    }

    private void loadgroups_from_db() {
        getGroupsFromDb();

        setGroupList();
    }


    private void getGroupsFromDb() {
        SQLiteDbHelper db = new SQLiteDbHelper(this.getContext());

        Cursor result = db.selectGroups();

        if (result != null) {
            list = new ArrayList<Group>();
            while (result.moveToNext()) {
                String day = result.getString(1);
                String fromtime = result.getString(2);
                String tomtime = result.getString(3);
                String location = result.getString(4);
                String comment = result.getString(5);
                String lang = result.getString(6);
                float latitude = result.getFloat(7);
                float longitude = result.getFloat(8);
                float db_km = result.getFloat(8);

                float km = 0;

                if (cur_latitude > 0) {
                    km = calculateDistance(cur_latitude, cur_longitude, latitude, longitude);
                }


                Group g = new Group(day, fromtime, tomtime, comment, location, lang, latitude, longitude, km);
                list.add(g);
            }

            if (!result.isClosed()) {
                result.close();
            }

            Collections.sort(list, new GroupComparator());
        }
    }


    private void setGroupList() {
        adapter = new GroupsAdapter(list, getActivity(), cur_latitude, cur_longitude);
        listview.setAdapter(adapter);

        spinner.setVisibility(View.GONE);
    }

    public Integer getDays(String date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = null;
        String days = "";
        try {
            startDate = df.parse(date);
            Date currentTime = Calendar.getInstance().getTime();
            long mills = currentTime.getTime() - startDate.getTime();
            days = String.valueOf(mills / (24 * 60 * 60 * 1000));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return Integer.parseInt(days);
    }

    @Override
    public void onTaskDone(List<Object> objs) {
        list = new ArrayList<Group>();
        if (objs != null && objs.size() > 0 && objs.get(0) instanceof Group) {
            for (int i = 0; i < objs.size(); ++i) {

                Group n = (Group) objs.get(i);
                list.add(n);
            }


            listview.setTextFilterEnabled(true);
            if (adapter == null)
                setGroupList();
            filter = adapter.getFilter();


            SQLiteDbHelper db = new SQLiteDbHelper(this.getContext());
            db.insertGroups(list);


            Date currentTime = Calendar.getInstance().getTime();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            db.insertSettings("group", df.format(currentTime));

            setGroupList();

            spinner.setVisibility(View.GONE);

        } else {

        }

    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        ArrayList<Group> filterList = new ArrayList<Group>();
        for (int i = 0; i < list.size(); i++) {
            Group dataModel = list.get(i);
            if ((list.get(i).getDay()).contains(s.toUpperCase())
                    || (list.get(i).getLocation()).contains(s.toUpperCase())
                    || (list.get(i).getLang()).contains(s.toUpperCase())
                    || (list.get(i).getFromTime()).contains(s.toUpperCase())) {
                filterList.add(dataModel);
            }
        }

        adapter = new GroupsAdapter(filterList, getActivity(), cur_latitude, cur_longitude);
        listview.setAdapter(adapter);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        ArrayList<Group> filterList = new ArrayList<Group>();
        for (int i = 0; i < list.size(); i++) {
            Group dataModel = list.get(i);
            if ((list.get(i).getDay()).contains(s.toUpperCase())
                    || (list.get(i).getLocation()).contains(s.toUpperCase())
                    || (list.get(i).getLang()).contains(s.toUpperCase())
                    || (list.get(i).getFromTime()).contains(s.toUpperCase())) {
                filterList.add(dataModel);
            }
        }

        adapter = new GroupsAdapter(filterList, getActivity(), cur_latitude, cur_longitude);
        listview.setAdapter(adapter);


        return false;
    }


}
