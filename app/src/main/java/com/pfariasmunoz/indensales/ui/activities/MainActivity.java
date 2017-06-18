package com.pfariasmunoz.indensales.ui.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.pfariasmunoz.indensales.BuildConfig;
import com.pfariasmunoz.indensales.R;
import com.pfariasmunoz.indensales.data.FbContract.UserEntry;
import com.pfariasmunoz.indensales.data.models.IndenUser;
import com.pfariasmunoz.indensales.ui.fragments.ClientsFragment;
import com.pfariasmunoz.indensales.ui.fragments.SalesFragment;
import com.pfariasmunoz.indensales.ui.fragments.UsersFragment;
import com.pfariasmunoz.indensales.utils.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String ANONYMOUS = "anonymous";
    private String mUserName = ANONYMOUS;
    private String mUserEmail;

    // views for the drawer views
    private TextView mNavBarUserEmailTextView;
    private TextView mNavBarUserNameTextView;
    private ImageView mNavBarUserPhotoImageView;
    private DrawerLayout mDrawerLayout;
    @BindView(R.id.main_content)
    CoordinatorLayout mCoordinatorLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    // Firebase authentication
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    // recuest code for firebase sign in
    // this is a flag for when we return from starting the activity for the result
    public static final int RC_SIGN_IN = 1;
    public static final int MAKE_SALE_REQUEST = 2;
    public static final int ADD_CLIENTS_TO_USER_REQUEST = 3;
    private DatabaseReference mUserReference;
    private ValueEventListener mUsersListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        // Adding Toolbar to Main screen
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Setting ViewPager for each Tabs
        //setupViewPager(mViewPager);

        //setupViewPager(viewPager);
        // Set Tabs inside Toolbar
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(mViewPager);
        // Create Navigation drawer and inlfate layout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent = getIntent();
        String saleResult = intent.getStringExtra(Constants.SALE_SUCCESS_KEY);
        if (saleResult != null) {
            if (saleResult.equals("S")) {
                Snackbar snackbar = Snackbar
                        .make(mCoordinatorLayout, "Welcome to AndroidHive", Snackbar.LENGTH_LONG);

                snackbar.show();

            }
            Toast.makeText(this, saleResult, Toast.LENGTH_SHORT).show();
        }

        // Initialize Firebase components
        mFirebaseAuth = FirebaseAuth.getInstance();

        // Initialize the authentication statle listener of firebase
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {

                    // the user is signed in
                    onSignedInInitialize(user);

                } else {
                    // the user is signed out, so, launch the sign in flow
                    startSignInFlow();
                }
            }
        };
    }

    private void setupViewPager(ViewPager viewPager) {

        String clientsFragmentTitle = getResources().getString(R.string.clients_fragment_title);
        String salesFragmentTitle = getResources().getString(R.string.sales_fragment_title);
        String userFragmentTitle = getResources().getString(R.string.users_fragment_title);
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new ClientsFragment(), clientsFragmentTitle);
        adapter.addFragment(new SalesFragment(), salesFragmentTitle);
        adapter.addFragment(new UsersFragment(), userFragmentTitle);
        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }


    }

    private void onSignedInInitialize(FirebaseUser user) {
        setupViewPager(mViewPager);
        writeNewUserIfNeeded(user);
        mUserName = user.getDisplayName();
        mUserEmail = user.getEmail();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        mNavBarUserEmailTextView = (TextView) headerView.findViewById(R.id.tv_email_nav_bar);
        mNavBarUserNameTextView = (TextView) headerView.findViewById(R.id.tv_user_name_nav_bar);
        mNavBarUserPhotoImageView = (ImageView) headerView.findViewById(R.id.imv_user_photo);
        if (!TextUtils.isEmpty(mUserEmail)) {
            mNavBarUserEmailTextView.setText(mUserEmail);
        }
        if (!TextUtils.isEmpty(mUserName)) {
            mNavBarUserNameTextView.setText(mUserName);
        }

        if (user.getPhotoUrl() != null) {
            Glide.with(this).load(user.getPhotoUrl().toString()).into(mNavBarUserPhotoImageView);
        }

    }

    private void startSignInFlow() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setLogo(R.drawable.indenlogo2)
                            .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                            .setProviders(Arrays.asList(

                                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()
                            ))
                            .build(),
                    RC_SIGN_IN);
        } else {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setLogo(R.drawable.indenlogo2)
                            .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                            .setProviders(Arrays.asList(
                                    new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build(),
                                    new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()
                            ))
                            .build(),
                    RC_SIGN_IN);
        }

    }

    public void startSale(String clientId, String addressId) {

        if (addressId != null) {
            Intent makeSaleIntent = new Intent(getApplicationContext(), CreateSaleActivity.class);
            makeSaleIntent.putExtra(Constants.CLIENT_ID_KEY, clientId);
            makeSaleIntent.putExtra(Constants.ADDRESS_ID_KEY, addressId);
            startActivityForResult(makeSaleIntent, MAKE_SALE_REQUEST);
        } else {
            Intent makeSaleIntent = new Intent(getApplicationContext(), ClientAddressesActivity.class);
            makeSaleIntent.putExtra(Constants.CLIENT_ID_KEY, clientId);
            startActivityForResult(makeSaleIntent, MAKE_SALE_REQUEST);
        }

    }

    public void addClientsToUser(String userId) {
        Intent addClientsToUserIntent = new Intent(getApplicationContext(), AddClientsActivity.class);
        addClientsToUserIntent.putExtra(Constants.USER_ID_KEY, userId);
        startActivityForResult(addClientsToUserIntent, ADD_CLIENTS_TO_USER_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                String signInMessage = getResources().getString(R.string.sign_in);

                Toast.makeText(this, signInMessage, Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                String signInCanceledMessage = getResources().getString(R.string.sign_in_canceled);
                Toast.makeText(this, signInCanceledMessage, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        if (requestCode == MAKE_SALE_REQUEST) {
            if (resultCode == RESULT_OK) {

                CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content);

                String message = getResources().getString(R.string.message_sale_made);
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, message, Snackbar.LENGTH_LONG);

                snackbar.show();
            }
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
        if (mUsersListener != null) {
            mUserReference.removeEventListener(mUsersListener);
        }
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {


        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_log_out) {
            // Handle singing out of the app
            AuthUI.getInstance().signOut(this);
        } else if (id == R.id.nav_articles_fragemnt) {
            //initializeFragment(new ArticlesFragment());
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void writeNewUserIfNeeded(final FirebaseUser user) {
        mUserReference = UserEntry.sRef.child(getUid());
        mUsersListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    IndenUser indenUser = getIndenUser(user);
                    mUserReference.setValue(indenUser);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mUserReference.addListenerForSingleValueEvent(mUsersListener);
    }


}
