package com.jt.buildtool.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BuildDetails {

    private String id;
    private String displayName;
    private String description;
    private String url;
    private boolean building;
    private String result;
    private long timestamp;
    private long duration;
    private String gitHubRepoUrl;

    public BuildDetails() {
        // no-args constructor for Jackson
    }

    public String getGitHubRepoUrl() {
        return gitHubRepoUrl;
    }

    public void setGitHubRepoUrl(String gitHubRepoUrl) {
        this.gitHubRepoUrl = gitHubRepoUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isBuilding() {
        return building;
    }

    public void setBuilding(boolean building) {
        this.building = building;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
