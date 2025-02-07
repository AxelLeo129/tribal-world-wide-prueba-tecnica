package com.tp.models;

import lombok.Data;

import java.util.List;

@Data
public class RandomUserResponse {

    private List<UserResult> results;

    public List<UserResult> getResults() {
        return results;
    }

    public void setResults(List<UserResult> results) {
        this.results = results;
    }
}
