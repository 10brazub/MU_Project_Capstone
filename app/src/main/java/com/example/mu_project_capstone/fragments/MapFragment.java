package com.example.mu_project_capstone.fragments;

import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.mu_project_capstone.BuildConfig;
import com.example.mu_project_capstone.R;
import com.google.android.gms.location.FusedLocationProviderClient;
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
import okhttp3.Headers;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private FusedLocationProviderClient fusedLocationProviderClient;

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
        googleMap.setMyLocationEnabled(true);
        setCurrentUserLocation(googleMap);
        setContractorMarkers(googleMap);
    }

    public void setCurrentUserLocation(GoogleMap googleMap) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(getActivity(), location -> {
            LatLng currentUserLocation = new LatLng(location.getLatitude(), location.getLongitude());
            googleMap.moveCamera(CameraUpdateFactory.zoomTo(12));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentUserLocation));
        });
    }

    public void setContractorMarkers(GoogleMap googleMap) {
        ParseQuery<ParseUser> queryForUser = ParseUser.getQuery();
        queryForUser.whereEqualTo("serviceSeeker", false);
        queryForUser.findInBackground((objects, e) -> {
            if (e == null){
                String contractorZipcode;
                String contractorURL;
                AsyncHttpClient client = new AsyncHttpClient();

                for (ParseUser currContractor: objects) {
                    contractorZipcode = currContractor.get("zipcode").toString();
                    contractorURL = getURL(contractorZipcode);
                    client.get(contractorURL, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Headers headers, JSON json) {
                            JSONObject jsonObject = json.jsonObject;
                            try {

                                double latitude = Double.parseDouble(jsonObject.get("lat").toString());
                                double longitude = Double.parseDouble(jsonObject.get("lng").toString());

                                LatLng newPosition = new LatLng(latitude, longitude);
                                googleMap.addMarker(new MarkerOptions().position(newPosition));

                            } catch (JSONException ex) {
                                ex.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    });
                }
            } else {
                e.printStackTrace();
            }
        });
    }

    public String getURL(String zipcode) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("www.zipcodeapi.com")
                .appendPath("rest")
                .appendPath(BuildConfig.ZIPCODE_API_KEY)
                .appendPath("info.json")
                .appendPath(zipcode)
                .appendPath("degrees");
        return builder.build().toString();
    }
}









