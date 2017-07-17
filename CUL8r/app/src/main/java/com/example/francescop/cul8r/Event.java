package com.example.francescop.cul8r;

class Event {
    private int id;
    private String description;
    private String start;
    private String end;
    private double lat;
    private double lng;
    private boolean verified;

    Event(int id, String description, String start, String end, double lat, double lng, boolean verified) {
        this.id = id;
        this.description = description;
        this.start = start;
        this.end = end;
        this.lat = lat;
        this.lng = lng;
        this.verified = verified;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public boolean isVerified() {
        return verified;
    }

}
