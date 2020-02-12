package com.taxi.friend.drivers;

import android.Manifest;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.appsync.AWSAppSyncClient;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.amazonaws.taxifriend.orders.ListOrdersQuery;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.exception.ApolloException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.taxi.friend.drivers.barcodereader.BarcodeCaptureActivity;
import com.taxi.friend.drivers.constants.Constants;
import com.taxi.friend.drivers.models.DriverLocation;
import com.taxi.friend.drivers.models.ResponseWrapper;
import com.taxi.friend.drivers.models.User;
import com.taxi.friend.drivers.services.DriverService;
import com.taxi.friend.drivers.utils.GPSCoordinate;
import com.taxi.friend.drivers.view.models.MenuMainUserViewModel;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;

import com.apollographql.apollo.api.Response;

import retrofit2.Call;
import retrofit2.Callback;

public class MainDriverActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    Marker lastMarker;

    private TextView textViewUserName;
    private TextView textViewCredit;
    private String credit;

    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient;
    private Location lastLocation;
    private Location preLastLocation;
    private int lastDirection = 0;
    FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback locationCallback;

    private static final int MY_PERMISSION_REQUEST_CODE = 7000;

    private static int UPDATE_INTERVAL = 5000;
    private static int FASTEST_INTERVAL = 3000;
    private static int DISPLACEMENT = 10;
    private static int MIN_DISTANCE = 8;
    AWSAppSyncClient client;
    private List<ListOrdersQuery.Item> orders;

    boolean isLargeLayout;
    boolean isShowOrderDialog = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_driver);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);

        textViewCredit = headerView.findViewById(R.id.txtTotalCredit);
        textViewUserName = headerView.findViewById(R.id.txtuserName);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //User Tax info
        TaxiGlobalInfo.mainViewModel = ViewModelProviders.of(this)
                .get(MenuMainUserViewModel.class);
        DriverService driverService = new DriverService();
        driverService.getDriver(TaxiGlobalInfo.DriverId);
        TaxiGlobalInfo.mainViewModel = new MenuMainUserViewModel( new MutableLiveData<User>());
        TaxiGlobalInfo.mainViewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                if(user != null){
                    textViewCredit.setText("Bs. " + user.getCredit());
                    textViewUserName.setText(user.getName());
                }

            }
        });

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }

                for (Location location : locationResult.getLocations()) {
                    if(lastLocation == null) {
                        lastLocation = location;
                    }

                    if(location != null){
                        double lat2 = location.getLatitude();
                        double lon2 = location.getLongitude();
                        double lat1 = lastLocation.getLatitude();
                        double lon1 = lastLocation.getLongitude();
                        displayLocation();
                        displayDrivers();

                        if(GPSCoordinate.distanceInMeterBetweenEarthCoordinates(lat2, lon2, lat1, lon1) > MIN_DISTANCE){
                            preLastLocation = lastLocation;
                            lastLocation = location;

                            updateDriverLocation();

                        }

                    }

                }
            }
        };


        startLocationUpdates();
        displayLocation();

        setUpLocation();
        displayDrivers();
        createAwsAppSync(this);

        ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);

        // This schedule a runnable task every 2 minutes
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {
                if(lastLocation != null) {
                    query();
                }
            }
        }, 0, 5, TimeUnit.SECONDS);

        isLargeLayout = getResources().getBoolean(R.bool.large_layout);
    }

    private void createAwsAppSync(Context context) {
        AWSConfiguration awsConfig = new AWSConfiguration(context);

        client = AWSAppSyncClient.builder()
                .context(context)
                .awsConfiguration(awsConfig)
                .build();
    }

    private void query() {
        if (client == null) {
            createAwsAppSync(this);
        }
        client.query(ListOrdersQuery.builder().build())
                .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
                .enqueue(eventsCallback);
    }

    private GraphQLCall.Callback<ListOrdersQuery.Data> eventsCallback = new GraphQLCall.Callback<ListOrdersQuery.Data>() {

        @Override
        public void onResponse(@NonNull  Response<ListOrdersQuery.Data> response) {
            ListOrdersQuery.Data data = response.data();
            if (data != null) {
                orders = data.listOrders().items();
            } else {
                orders = new ArrayList<>();
            }


            //adapter.setEvents(events);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if(orders.isEmpty()) {
                        return;
                    }

                    if(isShowOrderDialog){
                        return;
                    }

                    ListOrdersQuery.Item item = orders.get(0);

                    showDialog(item);


                    Log.d("NotifiedChanged", "Notifying data set changed");
                    Toast.makeText(MainDriverActivity.this, "Hello" + orders.size(), Toast.LENGTH_LONG).show();
                    //adapter.notifyDataSetChanged();
                }
            });
        }

        @Override
        public void onFailure(@NonNull ApolloException e) {
            Log.e("ErrorApollo", "Failed to make events api call", e);

        }
    };

    public void showDialog(ListOrdersQuery.Item item) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        CustomDialogFragment newFragment = new CustomDialogFragment(item, lastLocation);

            // The device is using a large layout, so show the fragment as a dialog
        newFragment.show(fragmentManager, "dialog");
        isShowOrderDialog = true;
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

        if (id == R.id.nav_credit_qr) {
            startActivity(new Intent(MainDriverActivity.this, BarcodeCaptureActivity.class));
        } else if (id == R.id.nav_perfil) {
        } else if (id == R.id.nav_travels) {
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.mapstyle));

            if (!success) {
                Log.e("ErrorFailLoadMapStyle", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("ErrorNotFoundResource", "Can't find style. Error: ", e);
        }

        displayLocation();

        displayDrivers();
    }


    private void buildGoogleApiclient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    private void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LocationRequest locationRequest = LocationRequest.create()
                .setInterval(5000)
                .setFastestInterval(1000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        mFusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback, null);

    }

    private void setUpLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, MY_PERMISSION_REQUEST_CODE);
        } else {
            buildGoogleApiclient();
            createLocationRequest();
            displayLocation();
        }
    }

    private void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setInterval(FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setSmallestDisplacement(DISPLACEMENT);

    }

    private void displayLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (lastLocation != null) {
            final double latitude = lastLocation.getLatitude();
            final double longitude = lastLocation.getLongitude();

            if (lastMarker != null) {
                lastMarker.remove();
            }

            int direction = getDirection(preLastLocation, lastLocation);
            lastMarker = mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_taxi_car_you))
                    .position(new LatLng(latitude, longitude)).title("You "));
            lastMarker.setRotation(direction);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15.0f));

        } else {
            Log.d("ERROR", "Cannot get your location");
        }
    }

    private int getDirection(Location preLastLocation, Location lastLocation) {

        if(preLastLocation == null || lastLocation == null) {
            return lastDirection;
        }

        double lon2 = lastLocation.getLongitude();
        double lon1 = preLastLocation.getLongitude();
        double lat2 = lastLocation.getLatitude();
        double lat1 = preLastLocation.getLatitude();

        if(GPSCoordinate.distanceInMeterBetweenEarthCoordinates(lat2, lon2, lat1, lon1) < MIN_DISTANCE){
            return lastDirection;
        }

        double dLon = lon2 - lon1;
        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) *Math.cos(dLon);
        int bearing = (int)(Math.atan2(y, x) * 180 / Math.PI);

        if (bearing < 0) bearing += 360;

        lastDirection = bearing;

        return lastDirection;
    }

    private void displayDrivers(){
        if(lastLocation == null) {
            return;
        }

        try{
            DriverService service = new DriverService();
            int radio = 8;
            Call<ResponseWrapper<List<DriverLocation>>> callDrivers = service.getDriverLocations(radio, lastLocation.getLatitude(), lastLocation.getLongitude());

            callDrivers.enqueue(new Callback<ResponseWrapper<List<DriverLocation>>>() {
                @Override
                public void onResponse(Call<ResponseWrapper<List<DriverLocation>>> call, retrofit2.Response<ResponseWrapper<List<DriverLocation>>> response) {
                    List<DriverLocation> drivers = response.body().getResult();
                    for ( int i = 0; i < drivers.size(); i++){
                        DriverLocation driver = drivers.get(i);
                        double latitude = driver.getLatitude();
                        double longitude = driver.getLongitude();
                        Log.i("taxi", "taxi" + driver.getId());
                        int taxiIcon = getDriverIcon(driver.getStatus());


                        mMap.addMarker(new MarkerOptions()
                                .icon(BitmapDescriptorFactory.fromResource(taxiIcon))
                                .position(new LatLng(latitude, longitude)).title(driver.getName()))
                                .setRotation(driver.getDirection());
                    }
                }

                @Override
                public void onFailure(Call<ResponseWrapper<List<DriverLocation>>> call, Throwable t) {
                    Log.e("ErrorGettingDrivers", t.getMessage());
                }
            });

        }
        catch (Exception e){
            Log.i("taxi", "taxi error");
        }
    }

    private void updateDriverLocation(){
        try{
            DriverService service = new DriverService();
            Call<String> callUpdate = service.updateLocation(TaxiGlobalInfo.DriverId, lastLocation);

            callUpdate.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                    int code = response.code();
                    if(code != HttpURLConnection.HTTP_OK){
                        Log.e("UpdateLocation", String.format("Response updating location: %d", code));
                    }

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.e("ErrorUpdateDriverLoc", t.getMessage());
                }
            });

        }
        catch (Exception e){
            Log.i("taxi", "taxi error");
        }
    }

    private int getDriverIcon(String status) {

        int driverIconResource = R.mipmap.ic_taxi_car_you;
        if(status.equals(Constants.DRIVER_STATUS_FREE)) {
            driverIconResource = R.mipmap.ic_taxi_car_green;
        }

        if( status.equals(Constants.DRIVER_STATUS_BUSY)) {
            driverIconResource = R.mipmap.ic_taxi_car_blue;
        }

        return driverIconResource;
    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
        displayLocation();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    buildGoogleApiclient();
                    createLocationRequest();
                    displayLocation();
                }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(locationCallback);
    }
}
