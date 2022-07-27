package com.example.mu_project_capstone.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import android.os.Looper;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.mu_project_capstone.BuildConfig;
import com.example.mu_project_capstone.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import okhttp3.Headers;
import static com.example.mu_project_capstone.keys.ParseObjectKeys.*;
import static com.example.mu_project_capstone.keys.ConstantsKeys.*;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    FusedLocationProviderClient fusedLocationProviderClient;
    int PERMISSION_ID = 44;
    GoogleMap map;

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
        supportMapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        googleMap.setMyLocationEnabled(true);
        setCurrentUserLocation(googleMap);
        getContractors(googleMap);
    }

    public void setCurrentUserLocation(GoogleMap googleMap) {

        Activity MapFragmentActivity = getActivity();
        Context MapFragmentContext = getContext();
        if (MapFragmentActivity != null) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MapFragmentActivity);
            if (checkPermissions()) {
                if (isLocationEnabled()) {
                    fusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            LatLng currentUserLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            googleMap.moveCamera(CameraUpdateFactory.zoomTo(10));
                            googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentUserLocation));
                        }
                    });
                } else {
                    Toast.makeText(MapFragmentContext, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            } else {
                requestPermissions();
            }
        }
    }

    private boolean checkPermissions() {
        Context MapFragmentContext = getContext();
        return ActivityCompat.checkSelfPermission(MapFragmentContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapFragmentContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        Activity MapFragmentActivity = getActivity();
        ActivityCompat.requestPermissions(MapFragmentActivity, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);

    }

    private boolean isLocationEnabled() {
        Activity MapFragmentActivity = getActivity();
        LocationManager locationManager = (LocationManager) MapFragmentActivity.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void requestNewLocationData() {
        Activity MapFragmentActivity = getActivity();
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5);
        locationRequest.setFastestInterval(0);
        locationRequest.setNumUpdates(1);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MapFragmentActivity);
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location location = locationResult.getLastLocation();
            LatLng currentUserLocation = new LatLng(location.getLatitude(), location.getLongitude());
            map.moveCamera(CameraUpdateFactory.zoomTo(10));
            map.moveCamera(CameraUpdateFactory.newLatLng(currentUserLocation));
        }
    };


    private void getContractors(GoogleMap googleMap) {
        ParseQuery<ParseUser> queryForUser = ParseUser.getQuery();
        queryForUser.whereEqualTo(IS_SERVICE_SEEKER, false);
        queryForUser.findInBackground((userList, e) -> {
            if (e == null) {
                ParseUser currentUser = ParseUser.getCurrentUser();
                String currentUserZipcode = currentUser.get(CURRENT_USER_ZIPCODE_KEY).toString();
                String zipcodeList = getContractorZipcodes(userList);
                String zipcodesUrl = getZipcodeUrl(currentUserZipcode, zipcodeList);
                getContractorDistances(zipcodesUrl, googleMap);

            } else {
                e.printStackTrace();
            }
        });
    }

    private String getContractorZipcodes(List<ParseUser> userList) {
        List<String> zipcodeList = new ArrayList<>();
        for (ParseUser currentContractor : userList) {
            zipcodeList.add(currentContractor.get(SERVICE_PROVIDER_ZIPCODE_KEY).toString());
        }
        String zipcodesListString = String.join(",", zipcodeList);

        return zipcodesListString;
    }

    private String getZipcodeUrl(String currentUserZipcode, String zipcodeList) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(URL_SCHEME)
                .authority(URL_AUTHORITY)
                .appendPath(URL_TYPE_REST)
                .appendPath(BuildConfig.ZIPCODE_API_KEY)
                .appendPath(URL_JSON_MULTI_DISTANCE)
                .appendPath(currentUserZipcode)
                .appendPath(zipcodeList)
                .appendPath(URL_UNITS_MILE);

        return builder.build().toString();
    }

    private void getContractorDistances(String zipcodesUrl, GoogleMap googleMap) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(zipcodesUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                Map<String, Double> distanceMap = new LinkedHashMap<>();
                try {
                    JSONObject distances = jsonObject.getJSONObject(JSON_RESPONSE_DISTANCES);
                    Double contractorDistance;

                    for (Iterator<String> iterator = distances.keys(); iterator.hasNext(); ) {
                        Object zipcode = iterator.next();
                        contractorDistance = Double.parseDouble(distances.get(zipcode.toString()).toString());
                        distanceMap.put(zipcode.toString(), contractorDistance);
                        if (contractorDistance < 40) {
                            distanceMap.put(zipcode.toString(), contractorDistance);
                        }
                    }
                    setContractorMarker(distanceMap, googleMap);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    private void setContractorMarker(Map<String, Double> distanceMap, GoogleMap googleMap) {
        Map<String, Double> sortedMap = distanceMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));

        String contractorURL;
        AsyncHttpClient client = new AsyncHttpClient();
        for (String zipcode : sortedMap.keySet()) {
            contractorURL = getURL(zipcode);
            client.get(contractorURL, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Headers headers, JSON json) {

                    JSONObject jsonObject = json.jsonObject;

                    try {
                        double latitude = Double.parseDouble(jsonObject.get(LATITUDE).toString());
                        double longitude = Double.parseDouble(jsonObject.get(LONGITUDE).toString());

                        LatLng newPosition = new LatLng(latitude, longitude);
                        googleMap.addMarker(new MarkerOptions().position(newPosition));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                    throwable.printStackTrace();
                }
            });
        }

    }

    public String getURL(String zipcode) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(URL_SCHEME)
                .authority(URL_AUTHORITY)
                .appendPath(URL_TYPE_REST)
                .appendPath(BuildConfig.ZIPCODE_API_KEY)
                .appendPath(URL_JSON_INFO)
                .appendPath(zipcode)
                .appendPath(URL_UNITS_DEGREES);
        String myURL = builder.build().toString();
        return myURL;
    }

}









