package com.fox.ahaliav.saapp;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.pm.PackageManager;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Pattern;

import static android.util.Patterns.*;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int PERMS_REQUEST_CODE = 1;
    private Toolbar toolbar = null;
    private DrawerLayout drawer = null;
    private ActionBarDrawerToggle toggle = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Fragment fragment = new MainFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.main_fragment_container, fragment, "MainFragment")
                .commit();

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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem item = menu.findItem(R.id.action_delete);
        item.setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
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
        } else if (id == R.id.nav_exit) {
            fragment = new NewsFragment();
            tag = "NewsFragment";
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

        View headerView = navigationView.getHeaderView(0); //navigationView.inflateHeaderView(R.layout.nav_header_main);


        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.GET_ACCOUNTS)) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.GET_ACCOUNTS}, PERMS_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.GET_ACCOUNTS}, PERMS_REQUEST_CODE);
            }
        } else {
            //do some stuff
            ArrayList<String> emails = new ArrayList<>();

            String email = "";
            Pattern gmailPattern = Patterns.EMAIL_ADDRESS;
            Account[] accounts = AccountManager.get(this).getAccounts();
            for (Account account : accounts) {
                if (gmailPattern.matcher(account.name).matches()) {
                    emails.add(account.name);
                    if (account.name.toLowerCase().equals("ahaliav@gmail.com")) {
                        if (Locale.getDefault().getLanguage() == "he")
                            email = account.name + " " + getResources().getString(R.string.wellcome);
                        else
                            email = getResources().getString(R.string.wellcome) + " " + account.name;
                        break;
                    }
                }
            }

            TextView viewEmail = (TextView) headerView.findViewById(R.id.email_address_view);
            viewEmail.setText(email);
        }
    }
}
