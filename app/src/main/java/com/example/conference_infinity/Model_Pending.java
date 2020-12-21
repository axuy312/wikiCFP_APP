package com.example.conference_infinity;

import java.util.HashMap;

public class Model_Pending {

    private String conference_name, conference_time, conference_location;
    private HashMap<String, Boolean> prepareThings;
    private String abbreviation = "N/A";
    private Boolean attend;

    public Model_Pending() {
        this.conference_name = "";
        this.conference_time = "";
        this.conference_location = "";
        this.prepareThings = new HashMap<>();

    }

    public Model_Pending(String conference_name, String conference_time, String conference_location, HashMap<String, Boolean> prepareThings) {
        this.conference_name = conference_name;
        this.conference_time = conference_time;
        this.conference_location = conference_location;
        this.prepareThings = prepareThings;
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
        if (conference_time != null)
            this.conference_time = conference_time;
    }

    public String getConference_location() {
        return conference_location;
    }

    public void setConference_location(String conference_location) {
        this.conference_location = conference_location;
    }

    public HashMap<String, Boolean> getPrepareThings() {
        return prepareThings;
    }

    public void setPrepareThings(HashMap<String, Boolean> prepareThings) {
        if (prepareThings != null) {
            for (String prepare : prepareThings.keySet().toArray(new String[0])) {
                this.prepareThings.put(prepare, prepareThings.get(prepare));
            }
        }

    }

    public String[] getPrepareThing() {
        return prepareThings.keySet().toArray(new String[0]);
    }

    public Boolean getPrepareThingState(String thing) {
        return prepareThings.get(thing);
    }

    // TODO: 增加一個PrepareThing
    public void setPrepareThingState(String thing, Boolean bool) {
        if (prepareThings == null) {
            prepareThings = new HashMap<>();
        }

        if (thing != null) {
            this.prepareThings.put(thing, bool);
        }
    }

    public int getPrepareThingCount() {
        if (prepareThings == null)
            return 0;
        else
            return prepareThings.size();
    }

    public String getAbbr() {
        return abbreviation;
    }

    public void setAbbr(String tmp)
    {
        if(tmp!=null)
        {
            abbreviation = tmp;
        }
    }

    public Boolean getAttend() {
        return attend;
    }

    public void setAttend(Boolean attend) {
        this.attend = attend;
    }
}
