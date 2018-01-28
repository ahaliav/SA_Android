package com.fox.ahaliav.saapp;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int PERMS_REQUEST_CODE = 1;
    public static boolean IsRegistered = false;
    private Toolbar toolbar = null;
    private DrawerLayout drawer = null;
    private ActionBarDrawerToggle toggle = null;
    Menu menu;
    AccountsAdapter listAdapter;
    private List<String> listDataHeader; // header titles
    private HashMap<String, List<String>> listDataChild;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        context = getApplicationContext();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Fragment fragment = new MainFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();

        String goto_register_page = intent.getStringExtra(Constants.GO_TO_REGISTER_KEY);

        if (goto_register_page != null && goto_register_page.equals("true")) {
            fragment = new RegisterFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_fragment_container, fragment, "RegisterFragment")
                    .commit();
        } else {
            fragmentManager.beginTransaction()
                    .replace(R.id.main_fragment_container, fragment, "MainFragment")
                    .commit();
        }


        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                String tag = "";
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    tag = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
                }

                switch (tag) {
                    case "MainFragment":
                    case "":
                        getSupportActionBar().setTitle("");
                        getSupportActionBar().setIcon(R.drawable.favicon_sa);
                        break;
                    case "GroupsFragment":
                        getSupportActionBar().setTitle(R.string.groups_title);
                        getSupportActionBar().setIcon(null);
                        break;
                    case "NewsFragment":
                        getSupportActionBar().setTitle(R.string.news_title);
                        getSupportActionBar().setIcon(null);
                        break;
                    case "CalenderFragment":
                        getSupportActionBar().setTitle(R.string.calender_title);
                        getSupportActionBar().setIcon(null);
                        break;
                    case "CalenderAddFragment":
                        //getSupportActionBar().setTitle(R.string.calender__add_title);
                        getSupportActionBar().setIcon(null);
                        break;
                    case "EventsFragment":
                        getSupportActionBar().setTitle(R.string.events_title);
                        getSupportActionBar().setIcon(null);
                        break;
                    case "NewsDetailsFragment":
                        getSupportActionBar().setTitle(R.string.news_detalis_title);
                        getSupportActionBar().setIcon(null);
                        break;
                    case "ContactsFragment":
                        getSupportActionBar().setTitle(R.string.contacts);
                        getSupportActionBar().setIcon(null);
                        break;
                    case "EmailsFragment":
                        getSupportActionBar().setTitle(R.string.contact);
                        getSupportActionBar().setIcon(null);
                        break;
                }


                if (tag != "MainFragment" && getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true); // show back button
                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onBackPressed();

                        }
                    });
                } else {

                    for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); ++i) {
                        getSupportFragmentManager().popBackStack();
                    }

                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    toggle.syncState();
                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            drawer.openDrawer(GravityCompat.START);

                        }
                    });
                }
            }
        });

        loadAccounts(navigationView);
    }

    public static boolean IsLoggedIn() {
        SQLiteDbHelper db = new SQLiteDbHelper(context);
        String isLoggedin = db.selectSettingsString(Constants.IS_LOGEDIN_KEY);

        return  isLoggedin.equals("true");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem action_delete = menu.findItem(R.id.action_delete);
        action_delete.setVisible(false);

        MenuItem action_edit = menu.findItem(R.id.action_edit);
        action_edit.setVisible(false);

        MenuItem action_add = menu.findItem(R.id.action_add);
        action_add.setVisible(false);

        setLoginRegisterBar();

        return true;
    }

    private void setLoginRegisterBar() {
        SQLiteDbHelper db = new SQLiteDbHelper(getApplicationContext());
        String isReg = db.selectSettingsString(Constants.IS_REGISTERED_KEY);
        String isLoggedin = db.selectSettingsString(Constants.IS_LOGEDIN_KEY);

        if (isLoggedin == null)
            isLoggedin = "";
        if (isReg == null)
            isReg = "";

        if ((isLoggedin.equals("") || isLoggedin.equals("false")) && isReg != null && isReg.equals("true") && IsLoggedIn() == false) {
            MenuItem action_login = menu.findItem(R.id.action_login);
            action_login.setVisible(true);
            MenuItem action_register = menu.findItem(R.id.action_register);
            action_register.setVisible(false);
            MenuItem action_exit = menu.findItem(R.id.action_exit);
            action_exit.setVisible(false);
        } else if (isReg.equals("") || isReg.equals("false") && IsLoggedIn() == false) {
            MenuItem action_register = menu.findItem(R.id.action_register);
            action_register.setVisible(true);
            MenuItem action_login = menu.findItem(R.id.action_login);
            action_login.setVisible(false);
            MenuItem action_exit = menu.findItem(R.id.action_exit);
            action_exit.setVisible(false);
        } else {
            MenuItem action_exit = menu.findItem(R.id.action_exit);
            action_exit.setVisible(true);
            MenuItem action_register = menu.findItem(R.id.action_register);
            action_register.setVisible(false);
            MenuItem action_login = menu.findItem(R.id.action_login);
            action_login.setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_fragment_container, new SettingsFragment())
                    .addToBackStack("SettingsFragment")
                    .commit();
            return true;
        }

        if (id == R.id.action_login) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_register) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.main_fragment_container, new RegisterFragment())
                    .addToBackStack("RegisterFragment")
                    .commit();
            return true;
        }
        if (id == R.id.action_exit) {
            SQLiteDbHelper db = new SQLiteDbHelper(getApplicationContext());
            db.updateSettings(Constants.IS_LOGEDIN_KEY, "false");
            setLoginRegisterBar();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment = null;
        FragmentManager fragmentManager = getSupportFragmentManager();
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        String tag = "";
        if (id == R.id.nav_main) {
            fragment = new MainFragment();
            tag = "MainFragment";
        } else if (id == R.id.nav_groups) {
            fragment = new GroupsFragment();
            tag = "GroupsFragment";
        } else if (id == R.id.nav_calender) {
            fragment = new CalenderFragment();
            tag = "CalenderFragment";
        } else if (id == R.id.nav_events) {
            fragment = new EventsFragment();
            tag = "EventsFragment";
        } else if (id == R.id.nav_news) {
            fragment = new NewsFragment();
            tag = "NewsFragment";
        } else if (id == R.id.nav_contacts) {
            fragment = new ContactsFragment();
            tag = "ContactsFragment";
        } else if (id == R.id.nav_emails) {
            fragment = new EmailsFragment();
            tag = "EmailsFragment";
        }


        fragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, fragment)
                .addToBackStack(tag)
                .commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadAccounts(NavigationView navigationView) {

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        SQLiteDbHelper db = new SQLiteDbHelper(this.getApplicationContext());
        Cursor result = db.selectUser("");

        Boolean emailfound = false;
        String email = "";
        int i = 0;
        if (result != null) {
            while (result.moveToNext()) {
                int id = result.getInt(0);
                email = result.getString(2);
                emailfound = true;
                break;
            }
            if (!result.isClosed()) {
                result.close();
            }
        }


        if (email.equals("")) {
            email = getResources().getString(R.string.select_account);
        }

        listDataHeader.add(email);

        View headerView = navigationView.getHeaderView(0); //navigationView.inflateHeaderView(R.layout.nav_header_main);

        ArrayList<String> emails = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.GET_ACCOUNTS)) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.GET_ACCOUNTS, Manifest.permission.CALL_PHONE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMS_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.GET_ACCOUNTS, Manifest.permission.CALL_PHONE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMS_REQUEST_CODE);
            }
        } else {
            //do some stuff
            Pattern gmailPattern = Patterns.EMAIL_ADDRESS;
            Account[] accounts = AccountManager.get(this).getAccounts();
            for (Account account : accounts) {
                if (gmailPattern.matcher(account.name).matches()) {
                    emails.add(account.name);
                }
            }

            listDataChild.put(email, emails);
        }

        Spinner expAccounts = (Spinner) headerView.findViewById(R.id.email_address_view);

        ArrayAdapter<String> myAdapter = new AccountsAdapter(this, R.layout.item_account_spinner, emails);
        expAccounts.setAdapter(myAdapter);

        for (int j = 0; j < expAccounts.getCount(); j++) {
            if (((String) expAccounts.getAdapter().getItem(j)).equals(email)) {
                expAccounts.setSelection(j);
                break;
            }
        }


        if (emailfound)
            expAccounts.setEnabled(false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMS_REQUEST_CODE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! do the
                    // calendar task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'switch' lines to check for other
            // permissions this app might request
        }
    }
}
