package com.example.mu_project_capstone;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;
import org.json.JSONArray;

@ParseClassName("ContractorAvailability")
public class ContractorAvailability extends ParseObject {

    public static final String KEY_USER = "user";
    public static final String KEY_SUNDAY = "Sunday";
    public static final String KEY_SUNDAY_REQUESTS = "sundayRequests";

    public ContractorAvailability(){}

    public JSONArray getSundayAvailability() {
        return getJSONArray(KEY_SUNDAY);
    }

    public void setSundayAvailability(JSONArray sundayAvailability) {
        put(KEY_SUNDAY, sundayAvailability);
    }

    public JSONArray getSundayRequests() {
        return getJSONArray(KEY_SUNDAY_REQUESTS);
    }

    public void setSundayRequests(JSONArray sundayRequests) {
        put(KEY_SUNDAY_REQUESTS, sundayRequests);
    }

    public ParseUser getKeyUser(){
        return getParseUser(KEY_USER);
    }

    public void setKeyUser(ParseUser parseUser) {
        put(KEY_USER, parseUser);
    }
}
