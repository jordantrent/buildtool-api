package com.jt.buildtool.config;

public class JenkinsConfig {

    private String jenkinsUrl;
    private String jenkinsUsername;
    private String jenkinsApiToken;

    public JenkinsConfig(){
        //no args constructor for jackson
    }

    public String getJenkinsUrl() {
        return jenkinsUrl;
    }

    public void setJenkinsUrl(String jenkinsUrl) {
        this.jenkinsUrl = jenkinsUrl;
    }

    public String getJenkinsUsername() {
        return jenkinsUsername;
    }

    public void setJenkinsUsername(String jenkinsUsername) {
        this.jenkinsUsername = jenkinsUsername;
    }

    public String getJenkinsApiToken() {
        return jenkinsApiToken;
    }

    public void setJenkinsApiToken(String jenkinsApiToken) {
        this.jenkinsApiToken = jenkinsApiToken;
    }
}
