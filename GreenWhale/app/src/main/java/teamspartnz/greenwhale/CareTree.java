package teamspartnz.greenwhale;

import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

import teamspartnz.greenwhale.Container.TreeContainer;
import teamspartnz.greenwhale.fragment.FragBottomSheetCARE;

public class CareTree extends FragmentActivity implements OnMapReadyCallback, FragBottomSheetCARE.GetStatus {

    private static final int DEFAULT_ZOOM = 20;
    ArrayList<TreeContainer> treeData = new ArrayList<>();
    ProgressDialog progressDialog;
    GeoDataClient mGeoDataClient;
    PlaceDetectionClient mPlaceDetectionClient;
    FusedLocationProviderClient mFusedLocationProviderClient;
    Location mLastKnownLocation;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_tree);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        //perm
        ActivityCompat.requestPermissions(CareTree.this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                }, 1);

        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(this, null);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


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
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

        progressDialog = new ProgressDialog(CareTree.this);
        progressDialog.setMessage("Loading trees nearby..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        JsonObject ob = new JsonObject();
        ob.addProperty("xxx", "xxx");
        Ion.with(CareTree.this)
                .load(getResources().getString(R.string.list))
                .setJsonObjectBody(ob)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        progressDialog.cancel();
                        if (result == null)
                            Toast.makeText(getApplicationContext(), "Connection error!", Toast.LENGTH_SHORT).show();
                        else {
                            for (int i = 0; i < result.size(); i++) {
                                JsonObject o = result.get(i).getAsJsonObject();
                                String uid = o.get("uid").getAsString();
                                double lat = o.get("lat").getAsDouble();
                                double lng = o.get("lng").getAsDouble();
                                String name = o.get("name").getAsString();
                                LatLng dat = new LatLng(lat, lng);

                                if (o.get("locked").getAsString().contentEquals("true"))
                                    mMap.addMarker(new MarkerOptions().position(dat)
                                            .title(name).snippet(uid).alpha(0.45f));
                                else
                                    mMap.addMarker(new MarkerOptions().position(dat)
                                            .title(name).snippet(uid));

                                treeData.add(new TreeContainer(lat, lng, name, uid));
                            }

                        }
                    }
                });


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Bundle bundle = new Bundle();
                bundle.putString("uid", marker.getSnippet());
                FragBottomSheetCARE bottomSheet = new FragBottomSheetCARE();
                bottomSheet.setArguments(bundle);
                bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());

                return false;
            }
        });
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()) {
                        // Set the map's camera position to the current location of the device.
                        mLastKnownLocation = task.getResult();
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(mLastKnownLocation.getLatitude(),
                                        mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                    }
                }
            });
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                }

            }
        }
    }

    @Override
    public void getStatus(boolean status, String uid) {
        if (status) {
            progressDialog = new ProgressDialog(CareTree.this);
            progressDialog.setMessage("Claiming...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setIndeterminate(true);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            JsonObject ob = new JsonObject();
            ob.addProperty("uid", uid);
            ob.addProperty("email", getSharedPreferences("Creden", 0).getString("user", "error"));
            Ion.with(CareTree.this)
                    .load(getResources().getString(R.string.care))
                    .setJsonObjectBody(ob)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            progressDialog.cancel();
                            if (result == null)
                                Toast.makeText(getApplicationContext(), "Connection error!", Toast.LENGTH_SHORT).show();
                            else {
                                Toast.makeText(getApplicationContext(), result.get("details").getAsString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
