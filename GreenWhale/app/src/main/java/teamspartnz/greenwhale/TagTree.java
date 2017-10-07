package teamspartnz.greenwhale;

import android.Manifest;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import teamspartnz.greenwhale.fragment.FragBottomSheetTAG;

public class TagTree extends FragmentActivity implements OnMapReadyCallback, FragBottomSheetTAG.DoWork {

    private static final int DEFAULT_ZOOM = 20;
    ProgressDialog progressDialog;
    LatLng ourLatLng;
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
        ActivityCompat.requestPermissions(TagTree.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
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
        Toast.makeText(getApplicationContext(), "ready", Toast.LENGTH_SHORT).show();
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();


        //show tagged ones'
        progressDialog = new ProgressDialog(TagTree.this);
        progressDialog.setMessage("Loading tree nearby..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        JsonObject ob = new JsonObject();
        ob.addProperty("xxx", "xxx");
        Ion.with(TagTree.this)
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
                                            .title(name).alpha(0.5f));
                                else
                                    mMap.addMarker(new MarkerOptions().position(dat)
                                            .title(name));
                            }

                        }
                    }
                });


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                ourLatLng = latLng;

                Bundle bundle = new Bundle();
                bundle.putDouble("latitude", latLng.latitude);
                bundle.putDouble("longitude", latLng.longitude);

                FragBottomSheetTAG bottomSheet = new FragBottomSheetTAG();
                bottomSheet.setArguments(bundle);
                bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());

                mMap.addMarker(new MarkerOptions().position(latLng)
                        .title("New Tree Tagged!"));
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
    public void getName(String name) {
        if (name.contentEquals("")) {
            recreate();
        } else {
            progressDialog = new ProgressDialog(TagTree.this);
            progressDialog.setMessage("Adding tree..");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setIndeterminate(true);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            JsonObject ob = new JsonObject();
            ob.addProperty("name", name);
            ob.addProperty("lat", ourLatLng.latitude);
            ob.addProperty("lng", ourLatLng.longitude);
            ob.addProperty("marked", getSharedPreferences("Creden", 0).getString("user", "error"));
            Ion.with(TagTree.this)
                    .load(getResources().getString(R.string.tag))
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
