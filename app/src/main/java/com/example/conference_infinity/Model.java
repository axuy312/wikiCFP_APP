package com.example.conference_infinity;

import java.util.ArrayList;

public class Model {

    private String conference_name, conference_time, conference_location;
    private ArrayList<String> prepareThings;
    private ArrayList<Boolean> prepareThingsStatus;

    public Model() {
        this.conference_name = "";
        this.conference_time = "";
        this.conference_location = "";
        this.prepareThings = new ArrayList<>();
        this.prepareThingsStatus = new ArrayList<>();
    }

    public Model(String conference_name, String conference_time, String conference_location, ArrayList<String> prepareThings, ArrayList<Boolean> prepareThingsStatus) {
        this.conference_name = conference_name;
        this.conference_time = conference_time;
        this.conference_location = conference_location;
        this.prepareThings = new ArrayList<>();
        this.prepareThings = prepareThings;
        this.prepareThingsStatus = new ArrayList<>();
        this.prepareThingsStatus = prepareThingsStatus;
    }

    public String getConference_name() {
        return conference_name;
    }

    public void setConference_name(String conference_name) {
        this.conference_name = conference_name;
    }

    public String getConference_time() {
        return conference_time;
    }

    public void setConference_time(String conference_time) {
        this.conference_time = conference_time;
    }

    public String getConference_location() {
        return conference_location;
    }

    public void setConference_location(String conference_location) {
        this.conference_location = conference_location;
    }

    public ArrayList<String> getPrepareThings() {
        return prepareThings;
    }

    public void setPrepareThings(ArrayList<String> prepareThings) {
        this.prepareThings = prepareThings;
    }

    public ArrayList<Boolean> getPrepareThingsStatus() {
        return prepareThingsStatus;
    }

    public void setPrepareThingsStatus(ArrayList<Boolean> prepareThingsStatus) {
        this.prepareThingsStatus = prepareThingsStatus;
    }
}
