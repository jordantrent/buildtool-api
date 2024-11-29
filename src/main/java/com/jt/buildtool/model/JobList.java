package com.jt.buildtool.model;

public class JobList {

    public String name;

    public JobList(){
        // no-args constructor for Jackson
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
