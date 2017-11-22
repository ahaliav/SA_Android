package com.fox.ahaliav.saapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;


public class GroupsFragment extends Fragment implements SearchView.OnQueryTextListener, ICallbackMethod {

    ListView listview = null;
    SearchView searchgroup = null;
    ArrayList<Group> list = null;
    GroupsAdapter adapter = null;
    Filter filter = null;
    private ProgressBar spinner;
    TextView tvLocation;
    LocationManager locationManager;
    private FusedLocationProviderClient mFusedLocationClient;

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
        tvLocation = (TextView) v.findViewById(R.id.tvLocation);
        spinner = (ProgressBar) v.findViewById(R.id.progressBar);
        spinner.setVisibility(View.VISIBLE);

        searchgroup = (SearchView) v.findViewById(R.id.searchgroup);
        list = new ArrayList<Group>();
        loadgroups();

        LocationManager locationManager = (LocationManager)
                getActivity().getSystemService(Context.LOCATION_SERVICE);


        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    123);
        } else {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location loc) {
                            // Got last known location. In some rare situations this can be null.
                            tvLocation.setText("");
                            if (loc != null) {

                                Toast.makeText(
                                        getContext(),
                                        "Location changed: Lat: " + loc.getLatitude() + " Lng: "
                                                + loc.getLongitude(), Toast.LENGTH_SHORT).show();
                                String longitude = "Longitude: " + loc.getLongitude();

                                String latitude = "Latitude: " + loc.getLatitude();

                                String res = "";
                                double latitude1 = 0;
                                double longitude1 = 0;
                                double latitude2 = 0;
                                double longitude2 = 0;
        /*------- To get city name from coordinates -------- */
                                String cityName = null;
                                Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());
                                List<Address> addresses;
                                try {
                                    addresses = gcd.getFromLocation(loc.getLatitude(),
                                            loc.getLongitude(), 1);

                                    if (addresses.size() > 0) {
                                        System.out.println(addresses.get(0).getLocality());
                                        cityName = addresses.get(0).getLocality();
                                        cityName = addresses.get(0).getThoroughfare();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                String s = longitude + "\n" + latitude + "\n\nMy Current City is: "
                                        + cityName;

                                Geocoder selected_place_geocoder = new Geocoder(getContext());
                                List<Address> address;
                                List<Address> address2;

                                try {
                                    address = selected_place_geocoder.getFromLocationName("שטנר 7 ירושלים", 5);
                                    address2 =  selected_place_geocoder.getFromLocationName("טללים 21 ירושלים", 5);

                                    if (address == null) {

                                    } else {
                                        Address location = address.get(0);
                                        Address location2 = address2.get(0);
                                        res = "Latitude: " + location.getLatitude();
                                        res += ", Longitude: " + location.getLongitude();
                                        res += ", City: " + location.getLocality();
                                        res += ", Street: " + location.getThoroughfare();

                                        latitude1 = location2.getLatitude();
                                        longitude1 = location2.getLongitude();

                                        latitude2 = location.getLatitude();
                                        longitude2 = location.getLongitude();
                                    }

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }


                                LatLng l1 = new LatLng(latitude1, longitude1);
                                LatLng l2 = new LatLng(latitude2,longitude2);
                                //distance(loc.getLatitude(), loc.getLongitude(),latitude2,longitude2)
                                tvLocation.setText(calculateDistance(latitude1,longitude1, latitude2, longitude2)  + " KM");
                            }
                        }
                    });
        }


        // locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, (LocationListener) this);

        return v;
    }



    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
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

    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));

        return Radius * c;
    }

    private void loadgroups() {
        WebSiteHelper helper = new WebSiteHelper(this);
        helper.getGroups();
    }

    @Override
    public void onTaskDone(List<Object> objs) {
        for (int i = 0; i < objs.size(); ++i) {

            Group n = (Group) objs.get(i);
            list.add(n);
        }

        adapter = new GroupsAdapter(list, getActivity().getApplicationContext());
        listview.setAdapter(adapter);
        listview.setTextFilterEnabled(true);
        filter = adapter.getFilter();
        searchgroup.setOnQueryTextListener(this);

        spinner.setVisibility(View.GONE);
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

        adapter = new GroupsAdapter(filterList, getActivity().getApplicationContext());
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

        adapter = new GroupsAdapter(filterList, getActivity().getApplicationContext());
        listview.setAdapter(adapter);


        return false;
    }


//    @Override
//    public void onLocationChanged(Location loc) {
//        tvLocation.setText("");
//
//        Toast.makeText(
//                getContext(),
//                "Location changed: Lat: " + loc.getLatitude() + " Lng: "
//                        + loc.getLongitude(), Toast.LENGTH_SHORT).show();
//        String longitude = "Longitude: " + loc.getLongitude();
//
//        String latitude = "Latitude: " + loc.getLatitude();
//
//
//        /*------- To get city name from coordinates -------- */
//        String cityName = null;
//        Geocoder gcd = new Geocoder(getContext(), Locale.getDefault());
//        List<Address> addresses;
//        try {
//            addresses = gcd.getFromLocation(loc.getLatitude(),
//                    loc.getLongitude(), 1);
//            if (addresses.size() > 0) {
//                System.out.println(addresses.get(0).getLocality());
//                cityName = addresses.get(0).getLocality();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        String s = longitude + "\n" + latitude + "\n\nMy Current City is: "
//                + cityName;
//        tvLocation.setText(s);
//    }
//
//    @Override
//    public void onProviderDisabled(String provider) {
//
//        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(getActivity(),
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
//                    123);
//        }
//
//        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this);
//        } else {
//            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) this);
//        }
//    }
//
//    @Override
//    public void onProviderEnabled(String provider) {
//
//        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(getActivity(),
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
//                    123);
//        }
//
//        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) this);
//        } else {
//            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) this);
//        }
//    }
//
//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {
//        // TODO Auto-generated method stub
//
//    }

}
