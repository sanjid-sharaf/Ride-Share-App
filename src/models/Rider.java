package models;

import java.util.List;

public class Rider {

    private List<String> preferredPickup;

    public List<String> getPreferredPickup(){ return preferredPickup; }
    public void setPreferredPickup(List<String> preferredPickup){ this.preferredPickup = preferredPickup; }
}
