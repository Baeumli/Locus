package com.locusapp.locus.activities.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.locusapp.locus.R;
import com.locusapp.locus.activities.auth.LoginActivity;
import com.locusapp.locus.fragments.ListFragment;
import com.locusapp.locus.fragments.MapFragment;
import com.locusapp.locus.fragments.DashboardFragment;

public class DashboardActivity extends AppCompatActivity implements
        DashboardFragment.OnFragmentInteractionListener,
        MapFragment.OnFragmentInteractionListener,
        ListFragment.OnFragmentInteractionListener
        {

    private TextView mTextMessage;
    private FrameLayout fragmentContainer;
    private DrawerLayout drawerLayout;
    private MapFragment mapFragment;
    private ListFragment listFragment;
    private GoogleSignInClient mGoogleSignInClient;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
                switch (item.getItemId()) {
                    case R.id.navigation_bounty:
                        switchToListFragment();
                        return true;
                    case R.id.navigation_map:
                        switchToMapFragment();
                        return true;
                }
                return false;
            };

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mTextMessage = (TextView) findViewById(R.id.message);
        fragmentContainer = findViewById(R.id.fragment_container);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        configureToolbar();
        configureNavigationDrawer();

        mapFragment = new MapFragment();
        listFragment =  new ListFragment();

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .add(R.id.fragment_container, mapFragment).hide(mapFragment)
                .addToBackStack(null)
                .add(R.id.fragment_container, listFragment).hide(listFragment)
                .addToBackStack(null)
                .show(listFragment)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch(itemId) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    public void switchToMapFragment() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().hide(listFragment).show(mapFragment).commit();
    }

    public void switchToListFragment() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().hide(mapFragment).show(listFragment).commit();
    }

    private void configureToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setHomeAsUpIndicator(R.drawable.ic_action_menu_drawer);
        actionbar.setDisplayHomeAsUpEnabled(true);
    }

            @Override
            public void onBackPressed() {
                finish();
            }

            private void configureNavigationDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView drawerNav = (NavigationView) findViewById(R.id.drawer_navigation);
        drawerNav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.logout) {
                    FirebaseAuth.getInstance().signOut();

                    mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(),"Logged Out",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                    drawerLayout.closeDrawers();
                    return true;
                }
                return false;
            }
        });
    }
}
