package com.jt.buildtool.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JenkinsConfig {

    @Value("${jenkins.url}")
    private String jenkinsUrl;

    @Value("${jenkins.username}")
    private String jenkinsUsername;

    @Value("${jenkins.apiToken}")
    private String jenkinsApiToken;

    public String getJenkinsUrl() {
        return jenkinsUrl;
    }

    public String getJenkinsUsername() {
        return jenkinsUsername;
    }

    public String getJenkinsApiToken() {
        return jenkinsApiToken;
    }
}