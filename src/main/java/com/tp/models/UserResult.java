package com.tp.models;

import lombok.Data;

@Data
public class UserResult {

    private String gender;
    private Name name;
    private Location location;

    public User toUser() {
        return new User(
                gender,
                name.getFirst(),
                name.getLast(),
                location.getCity(),
                location.getCountry()
        );
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
