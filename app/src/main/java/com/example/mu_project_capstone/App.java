package com.example.mu_project_capstone;

import android.app.Application;

import com.example.mu_project_capstone.models.ContractorAvailability;
import com.example.mu_project_capstone.models.ContractorListing;
import com.parse.Parse;
import com.parse.ParseObject;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(ContractorListing.class);
        ParseObject.registerSubclass(ContractorAvailability.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build());
    }
}
