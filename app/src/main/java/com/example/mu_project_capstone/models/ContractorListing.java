package com.example.mu_project_capstone.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("ContractorListing")
public class ContractorListing extends ParseObject {

    public static final String KEY_CONTRACTOR_USER = "user";
    public static final String KEY_CONTRACTOR_FIRST_NAME = "firstName";
    public static final String KEY_CONTRACTOR_LAST_NAME = "lastName";
    public static final String KEY_CONTRACTOR_DESCRIPTION = "description";
    public static final String KEY_CONTRACTOR_ZIPCODE = "zipcode";

    public ContractorListing(){}

    public ParseUser getKeyContractorUser() {
        return getParseUser(KEY_CONTRACTOR_USER);
    }

    public void setKeyContractorUser (ParseUser user){
        put(KEY_CONTRACTOR_USER, user);
    }

    public String getKeyContractorFirstName(){
        return getString(KEY_CONTRACTOR_FIRST_NAME);
    }

    public void setKeyContractorFirstName(String firstName) {
        put(KEY_CONTRACTOR_FIRST_NAME, firstName);
    }

    public String getKeyContractorLastName(){
        return getString(KEY_CONTRACTOR_LAST_NAME);
    }

    public void setKeyContractorLastName(String lastName) {
        put(KEY_CONTRACTOR_LAST_NAME, lastName);
    }

    public String getKeyContractorDescription(){

        return getString(KEY_CONTRACTOR_DESCRIPTION);
    }

    public void setKeyContractorDescription(String contractorDescription) {
        put(KEY_CONTRACTOR_DESCRIPTION, contractorDescription);
    }

    public String getKeyContractorZipcode() {
        return getString(KEY_CONTRACTOR_ZIPCODE);
    }

    public void setKeyContractorZipcode(String contractorZipcode) {
        put(KEY_CONTRACTOR_ZIPCODE, contractorZipcode);
    }

}
