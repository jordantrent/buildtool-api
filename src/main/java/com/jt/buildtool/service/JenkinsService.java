package com.jt.buildtool.service;

import com.jt.buildtool.config.JenkinsConfig;
import com.jt.buildtool.model.BuildDetails;
import com.jt.buildtool.model.PipelineDetails;
import com.jt.buildtool.exception.JenkinsServiceException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.List;

@Service
public class JenkinsService {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public JenkinsService(ObjectMapper objectMapper) {
        this.httpClient = HttpClient.newBuilder().build();
        this.objectMapper = objectMapper;
    }

    public PipelineDetails getPipelineDetails(String jobName, JenkinsConfig jenkinsConfig) {
        try {
            String jobJson = fetchPipelineDetailsFromJenkins(jobName, jenkinsConfig);
            return objectMapper.readValue(jobJson, PipelineDetails.class);
        } catch (Exception e) {
            throw new JenkinsServiceException("Error fetching pipeline details for job: " + jobName, e);
        }
    }

    private String fetchPipelineDetailsFromJenkins(String jobName, JenkinsConfig jenkinsConfig) throws IOException, InterruptedException {
        URI uri = URI.create(jenkinsConfig.getJenkinsUrl() + "/job/" + jobName + "/api/json?depth=1");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Authorization", getBasicAuthHeader(jenkinsConfig))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new JenkinsServiceException("Failed to fetch job details. HTTP Status: " + response.statusCode());
        }

        return response.body();
    }

    public BuildDetails getBuildDetails(String jobName, int buildId, JenkinsConfig jenkinsConfig) {
        try {
            String jobJson = fetchBuildDetailsFromJenkins(jobName, buildId, jenkinsConfig);
            return objectMapper.readValue(jobJson, BuildDetails.class);
        } catch (Exception e) {
            throw new JenkinsServiceException("Error fetching build details for job: " + jobName, e);
        }
    }

    private String fetchBuildDetailsFromJenkins(String jobName, int buildId, JenkinsConfig jenkinsConfig) throws IOException, InterruptedException {
        URI uri = URI.create(jenkinsConfig.getJenkinsUrl() + "/job/" + jobName + "/" + buildId + "/api/json");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Authorization", getBasicAuthHeader(jenkinsConfig))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new JenkinsServiceException("Failed to fetch build details. HTTP Status: " + response.statusCode());
        }

        return response.body();
    }

    public String getConsoleOutput(String jobName, int buildNumber, JenkinsConfig jenkinsConfig) {
        try {
            return fetchConsoleOutputFromJenkins(jobName, buildNumber, jenkinsConfig);
        } catch (IOException | InterruptedException e) {
            throw new JenkinsServiceException("Error fetching console output for job: " + jobName, e);
        }
    }

    private String fetchConsoleOutputFromJenkins(String jobName, int buildNumber, JenkinsConfig jenkinsConfig) throws IOException, InterruptedException {
        URI uri = URI.create(jenkinsConfig.getJenkinsUrl() + "/job/" + jobName + "/" + buildNumber + "/consoleText");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Authorization", getBasicAuthHeader(jenkinsConfig))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new JenkinsServiceException("Failed to fetch console output. HTTP Status: " + response.statusCode());
        }

        return response.body();
    }

    public List<String> getJobNames(JenkinsConfig jenkinsConfig) {
        try {
            String jobsJson = fetchJobNamesFromJenkins(jenkinsConfig);
            JsonNode rootNode = objectMapper.readTree(jobsJson);
            JsonNode jobsNode = rootNode.path("jobs");
            return jobsNode.isArray() ? jobsNode.findValuesAsText("name") : List.of();
        } catch (IOException | InterruptedException e) {
            throw new JenkinsServiceException("Error fetching job names", e);
        }
    }

    private String fetchJobNamesFromJenkins(JenkinsConfig jenkinsConfig) throws IOException, InterruptedException {
        URI uri = URI.create(jenkinsConfig.getJenkinsUrl() + "/api/json?tree=jobs[name]");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Authorization", getBasicAuthHeader(jenkinsConfig))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new JenkinsServiceException("Failed to fetch job names. HTTP Status: " + response.statusCode());
        }

        return response.body();
    }

    private String getBasicAuthHeader(JenkinsConfig jenkinsConfig) {
        String credentials = jenkinsConfig.getJenkinsUsername() + ":" + jenkinsConfig.getJenkinsApiToken();
        return "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());
    }
}
