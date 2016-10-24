package farmersfridge.farmersfridge.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.w3c.dom.Document;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import farmersfridge.farmersfridge.R;
import farmersfridge.farmersfridge.asynctasks.LocationsNearestTask;
import farmersfridge.farmersfridge.asynctasks.LocationsTask;
import farmersfridge.farmersfridge.models.LocationsItemModel;
import farmersfridge.farmersfridge.models.LocationsMilesModel;
import farmersfridge.farmersfridge.models.LocationsModel;
import farmersfridge.farmersfridge.support.GMapV2DirectionAsyncTask;
import farmersfridge.farmersfridge.utils.Constants;
import farmersfridge.farmersfridge.utils.SessionStores;

/**
 * Created by karuppiah on 6/9/2016.
 */
public class MapMain extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private View v;
    private GoogleMap googleMap;
    private MapView mapView;
    private Polyline polyline;
    private PolylineOptions rectLine;
    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;
    private LatLng sourcePosition;
    private LatLng destPosition;
    private String WALKING = "walking";
    private String DRIVING = "driving";
    private static final int REQUEST_FINE_LOCATION = 0;
    public static ArrayList<LocationsItemModel> locationsArray = new ArrayList<>();
    public static ArrayList<LocationsMilesModel> locationsMilesArray = new ArrayList<>();
    List<Polyline> polylines = new ArrayList<Polyline>();
    private double milesDiff;
    private boolean routeType = false;

    @BindView(R.id.layForListView)
    LinearLayout layForListView;
    @BindView(R.id.itemlocation)
    TextView txtForItemLoc;
    @BindView(R.id.findyournapa)
    TextView txtForFindNapa;
    @BindView(R.id.btn_drive)
    Button btn_drive;
    @BindView(R.id.btn_walk)
    Button btn_walk;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        //View initialized
        v = inflater.inflate(R.layout.map_main, container, false);
        ButterKnife.bind(this, v);

        Typeface itemHeaderTf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GrottoIronic-SemiBold.otf");
        Typeface findNapaTf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GarageFonts - FreightMicroProSemibold-Italic.otf");
        txtForItemLoc.setTypeface(itemHeaderTf);

        //if screen from Menu info find button clicks then text updated with that menu text otherwise default text is displayed
        if (Constants.VEND_NAME.length() > 0) {
            txtForFindNapa.setText("Find Your " + Constants.VEND_NAME);
        } else {
            txtForFindNapa.setText("Find Your Fridges");
        }

        //permission for map
        loadPermissions(Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_FINE_LOCATION);

        //listview button click listener
        layForListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                LocationsList fragment = new LocationsList();
                fragmentTransaction.replace(R.id.realtabcontent, fragment);
                fragmentTransaction.commit();
            }
        });

        //this is destination from nearest fridge in direction mode
        destPosition = new LatLng(9.08, 78.27);

        return v;
    }

    private void loadPermissions(String perm, int requestCode) {
        //Checks if permission is granted or not
        if (ContextCompat.checkSelfPermission(getActivity(), perm) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), perm)) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{perm}, requestCode);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // granted
                } else {
                    // no granted
                }
                return;
            }

        }

    }

    //Click listener for Driving button
    @OnClick(R.id.btn_drive)
    void driveMode() {
        routeType = false;
        route(sourcePosition, destPosition, DRIVING);
        setDriving();

    }

    //Click listener for Walking button
    @OnClick(R.id.btn_walk)
    void walkMode() {
        routeType = true;
        route(sourcePosition, destPosition, WALKING);
        setWalking();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        //Map initialized
        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        SessionStores.LOC_MODEL = new LocationsModel();
        FragmentManager fragmentManager = getFragmentManager();
        if (Constants.LOC_FROM.length() == 0) { //if calls from Menu info find button
            new LocationsTask(getActivity(), MapMain.this, SessionStores.LOC_MODEL.locations(), fragmentManager);
        } else { //else default location is send to service
            double currentLat = 41.879517;
            double currentLong = -87.6361477;
            new LocationsNearestTask(getActivity(), MapMain.this, currentLat, currentLong, fragmentManager);
        }
        mapView.onResume();
        mapView.getMapAsync(this);
        //    mapView.setDrawingCacheBackgroundColor(Color.parseColor("#AE6118"));
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {

        googleMap = map;
    }

    @Override
    public void onConnected(Bundle bundle) {

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mCurrentLocation = LocationServices
                .FusedLocationApi
                .getLastLocation(mGoogleApiClient);
    }

    //Routes points are fetched
    protected void route(final LatLng sourcePosition, LatLng destPosition, String mode) {
        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                try {
                    Document doc = (Document) msg.obj;
                    GMapV2DirectionAsyncTask md = new GMapV2DirectionAsyncTask();
                    ArrayList<LatLng> directionPoint = md.getDirection(doc);
                    for (Polyline line : polylines) {
                        line.remove();
                    }
                    polylines.clear();
                    rectLine = new PolylineOptions().width(15).color(getActivity().getResources().getColor(R.color.red));

                    for (int i = 0; i < directionPoint.size(); i++) {
                        rectLine.add(directionPoint.get(i));
                    }

                    mCurrentLocation.setLatitude(sourcePosition.latitude);
                    mCurrentLocation.setLatitude(sourcePosition.longitude);

                    //positioning camera on current location
                    initCamera(mCurrentLocation);

                    //adding correct polyline with selected mode
                    polyline = googleMap.addPolyline(rectLine);
                    polylines.add(polyline);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        new GMapV2DirectionAsyncTask(handler, sourcePosition, destPosition, mode).execute();
    }

    private void initCamera(Location location) {

        //creating camera options
        CameraPosition position = CameraPosition.builder()
                .target(new LatLng(sourcePosition.latitude,
                        sourcePosition.longitude))
                .zoom(14f)
                .bearing(0.0f)
                .tilt(0.0f)
                .build();

        //possitioning camera on current location
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(position), null);

        //Checks is permission granted or not
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        googleMap.setMyLocationEnabled(true);

    }

    //Markers for set
    public void addMArkers(double lat, double lang, String name, String prettyName) {

        double currentLat = 41.879517;
        double currentLong = -87.6361477;

        milesDiff = distFrom(currentLat, currentLong, lat, lang);
        DecimalFormat df = new DecimalFormat("#.#");
        df.format(milesDiff);

        LocationsMilesModel milesModel = new LocationsMilesModel();
        milesModel.setMiles(df.format(milesDiff));
        locationsMilesArray.add(milesModel);

        sourcePosition = new LatLng(lat, lang);

        // create marker
        MarkerOptions marker = new MarkerOptions().position(sourcePosition).title(name);

        if (milesDiff < 2) { //if distance from current location is less than 2 miles
            // Changing marker icon
            Log.e("source posoitionm???", "" + sourcePosition.latitude + " " + sourcePosition.longitude);
            String hurryString = prettyName + "<br><font color='#8fb774'><big><b>Hurry up!<b></big></font></br>";
            googleMap.addMarker(new MarkerOptions()
                    .position(sourcePosition)
                    .title(name)
                    .snippet(hurryString)
                    .icon(BitmapDescriptorFactory
                            .fromResource(R.drawable.marker_home)));
        } else {
            // Changing marker icon
            googleMap.addMarker(new MarkerOptions()
                    .position(sourcePosition)
                    .title(name)
                    .snippet(prettyName)
                    .icon(BitmapDescriptorFactory
                            .fromResource(R.drawable.marker_dot)));
        }

        //marker clicks listener
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker arg0) {
                arg0.showInfoWindow();

                return false;
            }
        });

        //Custom adapter for marker info window
        googleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());

        //Marker info window clicks
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                double currentLat = 41.879517;
                double currentLong = -87.6361477;
                sourcePosition = new LatLng(currentLat, currentLong);
                destPosition = marker.getPosition();
                if (routeType == false) { //Drive mode
                    route(sourcePosition, destPosition, DRIVING);
                    setDriving();
                } else { //Walk mode
                    route(sourcePosition, destPosition, WALKING);
                    setWalking();
                }
                marker.hideInfoWindow();
            }
        });

        //creating camera options
        CameraPosition position = CameraPosition.builder()
                .target(new LatLng(lat,
                        lang))
                .zoom(14f)
                .bearing(0.0f)
                .tilt(0.0f)
                .build();

        //possitioning camera on current location
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(position), null);

        try {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
        } catch (Exception e) {

        }
        googleMap.setMyLocationEnabled(true);
    }

    //Custom info window adapter for marker
    class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        @Override
        public View getInfoContents(final Marker arg0) {
            //View initialized
            View v = getActivity().getLayoutInflater().inflate(R.layout.marker_info_window, null);
            TextView textForName = (TextView) v.findViewById(R.id.txtForInfoName);
            TextView textForPrettyName = (TextView) v
                    .findViewById(R.id.txtForInfoPrettyName);
            RelativeLayout layForDirections = (RelativeLayout) v.findViewById(R.id.layForDirections);

            Typeface typeFace = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GarageFonts - FreightMicroProSemibold-Italic.otf");
            textForName.setTypeface(typeFace);
            textForPrettyName.setTypeface(typeFace);
            textForName.setText(arg0.getTitle());
            textForPrettyName.setText(Html.fromHtml(arg0.getSnippet()));

            //Direction button clcik listener
            layForDirections.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    double currentLat = 41.879517;
                    double currentLong = -87.6361477;
                    sourcePosition = new LatLng(currentLat, currentLong);
                    destPosition = arg0.getPosition();
                    route(sourcePosition, destPosition, DRIVING);
                }
            });

            return v;
        }

        @SuppressLint("InflateParams")
        @Override
        public View getInfoWindow(final Marker arg0) {
            return null;
        }

    }

    //Distance calculates as miles
    public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 3958.75; // miles (or 6371.0 kilometers)
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = earthRadius * c;

        return dist;
    }

    private MarkerOptions addMarker(LatLng latLng) {

        //creating markers from latitude and longitude
        MarkerOptions options = new MarkerOptions().position(latLng);
        options.icon(BitmapDescriptorFactory.defaultMarker());
        return options;
    }

    private void setWalking() {
        btn_drive.setBackgroundColor(getResources().getColor(R.color.transparent));
        btn_walk.setBackgroundColor(getResources().getColor(R.color.grey));
    }

    private void setDriving() {
        btn_drive.setBackgroundColor(getResources().getColor(R.color.grey));
        btn_walk.setBackgroundColor(getResources().getColor(R.color.transparent));
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
}
