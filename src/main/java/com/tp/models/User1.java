package com.tp.models;

public class User1 {

    String gender;
    Name name;
    Location location;

    public User1(String gender, Name name, Location location) {
        this.gender = gender;
        this.name = name;
        this.location = location;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
