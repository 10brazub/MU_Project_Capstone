package com.example.mu_project_capstone.fragments;

import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
        getContractors(googleMap);
    }

    public void setCurrentUserLocation(GoogleMap googleMap) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(getActivity(), location -> {
            LatLng currentUserLocation = new LatLng(location.getLatitude(), location.getLongitude());
            googleMap.moveCamera(CameraUpdateFactory.zoomTo(10));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentUserLocation));
        });
    }

    private void getContractors(GoogleMap googleMap) {

        ParseQuery<ParseUser> queryForUser = ParseUser.getQuery();
        queryForUser.whereEqualTo("serviceSeeker", false);
        queryForUser.findInBackground((objects, e) -> {
            if (e == null){

                ParseUser currentUser = ParseUser.getCurrentUser();
                String currentUserZipcode = currentUser.get("zipcode").toString();
                String zipcodeList = getContractorZipcodes(objects);
                String zipcodesUrl = getZipcodeUrl(currentUserZipcode, zipcodeList);
                getContractorDistances(zipcodesUrl, googleMap);

            } else {
                e.printStackTrace();
            }
        });
    }

    private String getContractorZipcodes(List<ParseUser> objects) {

        List<String> zipcodeList = new ArrayList<>();
        for (ParseUser currentContractor: objects) {
            zipcodeList.add(currentContractor.get("zipcode").toString());
        }
        String zipcodesListString = String.join(",", zipcodeList);

        return zipcodesListString;
    }

    private String getZipcodeUrl(String currentUserZipcode, String zipcodeList) {

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("www.zipcodeapi.com")
                .appendPath("rest")
                .appendPath(BuildConfig.ZIPCODE_API_KEY)
                .appendPath("multi-distance.json")
                .appendPath(currentUserZipcode)
                .appendPath(zipcodeList)
                .appendPath("mile");

        return builder.build().toString();
    }

    private void getContractorDistances(String zipcodesUrl, GoogleMap googleMap) {

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(zipcodesUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                Map<String,Double> distanceMap = new LinkedHashMap<>();
                try {
                    JSONObject distances = jsonObject.getJSONObject("distances");
                    Double contractorDistance;

                    for (Iterator<String> iterator = distances.keys(); iterator.hasNext(); ) {
                        Object zipcode = iterator.next();

                        contractorDistance = Double.parseDouble(distances.get(zipcode.toString()).toString());
                        distanceMap.put(zipcode.toString(), contractorDistance);

                        if (contractorDistance < 30) {
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
        for (String zipcode: sortedMap.keySet()){
            contractorURL = getURL(zipcode);
            client.get(contractorURL, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Headers headers, JSON json) {
                    
                    JSONObject jsonObject = json.jsonObject;

                    try {
                        double latitude = Double.parseDouble(jsonObject.get("lat").toString());
                        double longitude = Double.parseDouble(jsonObject.get("lng").toString());

                        LatLng newPosition = new LatLng(latitude, longitude);
                        googleMap.addMarker(new MarkerOptions().position(newPosition));
                        
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                
                @Override
                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

                }
            });
        }
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
        String myURL = builder.build().toString();
        return myURL;
    }

}









