package com.example.mywardrobe.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.mywardrobe.R;
import com.example.mywardrobe.fragments.CalendarFragment;
import com.example.mywardrobe.fragments.CategoriesFragment;
import com.example.mywardrobe.fragments.OutfitsFragment;
import com.example.mywardrobe.fragments.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.parse.ParseUser;

import org.parceler.Parcels;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String TAG = "MainActivity";
    public final FragmentManager fragmentManager = getSupportFragmentManager();
    private BottomNavigationView bottomNavigationView;

    Toolbar toolbar;
    DrawerLayout dlDrawerLayout;
    NavigationView nvNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment;
                switch (menuItem.getItemId()) {
                    case R.id.action_clothes:
                        fragment = new CategoriesFragment();
                        break;
                    case R.id.action_outfits:
                        fragment = new OutfitsFragment();
                        break;
                    case R.id.action_calendar:
                    default:
                        fragment = new CalendarFragment();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        // Set default menu item selection so that there is fragment showing, never empty
        bottomNavigationView.setSelectedItemId(R.id.action_clothes);

        dlDrawerLayout = findViewById(R.id.dlDrawerLayout);
        nvNavigationView = findViewById(R.id.nvNavigationView);

        nvNavigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, dlDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        dlDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        nvNavigationView.setNavigationItemSelectedListener(this);

        //Set NavigationView Menu
        Menu menu = nvNavigationView.getMenu();
        ParseUser currentUser = ParseUser.getCurrentUser();
        menu.findItem(R.id.nav_username).setTitle("Welcome, " + currentUser.getUsername());
    }

    @Override
    public void onBackPressed() {
        if(dlDrawerLayout.isDrawerOpen(GravityCompat.START)){
            dlDrawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_profile:
                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_logot:
                ParseUser.logOut();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_settings:
            default:
                fragmentManager.beginTransaction().replace(R.id.flContainer, new SettingsFragment()).commit();
                break;
        }
        dlDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}