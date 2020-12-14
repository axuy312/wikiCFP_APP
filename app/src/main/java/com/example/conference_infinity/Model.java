package com.example.conference_infinity;

import android.util.Log;

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

    // TODO: 增加一個PrepareThing
    public void addPrepareThing(String thing) {
        if (!thing.isEmpty()) {
            if (this.prepareThings.isEmpty()) {
                this.prepareThings = new ArrayList<>();
            }
            this.prepareThings.add(thing);
            addPrepareThingBool(false);
        }
    }

    // TODO:增加一個 PrepareThing 的 bool 判斷 checkbox 是否已被勾選
    public void addPrepareThingBool(Boolean thing_Status) {
        if (this.prepareThingsStatus.isEmpty()) {
            this.prepareThingsStatus = new ArrayList<>();
        }
        this.prepareThingsStatus.add(thing_Status);
    }

    public void setPrepareThingBool(Boolean thing_Status, String item_name) {
        if (!this.prepareThingsStatus.isEmpty() && !this.prepareThings.isEmpty()) {
            for (int i = 0; i < prepareThings.size(); ++i) {
                if (prepareThings.get(i).equals(item_name)) {
                    prepareThingsStatus.set(i, thing_Status);
                    break;
                }
            }
        } else {
            Log.d("debug", "Arraylist Empty");
        }
    }

    public boolean getPrepareThingBool(int pos) {
        boolean tmp = false;
        if (!this.prepareThingsStatus.isEmpty() && !this.prepareThings.isEmpty()) {
            tmp = prepareThingsStatus.get(pos);
        }

        return tmp;
    }
}
