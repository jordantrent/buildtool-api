package com.jt.buildtool.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.jt.buildtool.config.JenkinsConfig;
import com.jt.buildtool.model.Build;
import com.jt.buildtool.model.Job;
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

    public Build getBuildDetails(String jobName, int buildId, JenkinsConfig jenkinsConfig) {
        try {
            String jobJson = fetchBuildDetailsFromJenkins(jobName, buildId, jenkinsConfig);
            return objectMapper.readValue(jobJson, Build.class);
        } catch (Exception e) {
            throw new JenkinsServiceException("Error fetching build details for job: " + jobName, e);
        }
    }

    private String fetchBuildDetailsFromJenkins(String jobName, int buildId, JenkinsConfig jenkinsConfig) throws IOException, InterruptedException {
        URI uri = URI.create(jenkinsConfig.getJenkinsUrl() + "/job/" + jobName + "/" + buildId + "/api/json");
        System.out.println("Constructed URL: " + uri.toString());

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

    public List<Job> getJobs(JenkinsConfig jenkinsConfig) {
        try {
            String jobJson = fetchJobsFromJenkins(jenkinsConfig);
            JsonNode rootNode = objectMapper.readTree(jobJson);
            JsonNode jobsNode = rootNode.path("jobs");
            return objectMapper.readValue(jobsNode.toString(), new TypeReference<List<Job>>() {});
        } catch (IOException | InterruptedException e) {
            throw new JenkinsServiceException("Error fetching job names", e);
        }
    }

    private String fetchJobsFromJenkins(JenkinsConfig jenkinsConfig) throws IOException, InterruptedException {
        URI uri = URI.create(jenkinsConfig.getJenkinsUrl() + "/api/json?depth=1");

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

    public List<Build> getBuildsForJob(String jobName, JenkinsConfig jenkinsConfig) {
        try {
            String buildsJson = fetchBuildsForJobFromJenkins(jobName, jenkinsConfig);
            JsonNode rootNode = objectMapper.readTree(buildsJson);
            JsonNode buildsNode = rootNode.path("builds");

            return objectMapper.readValue(buildsNode.toString(), new TypeReference<List<Build>>() {});
        } catch (IOException | InterruptedException e) {
            throw new JenkinsServiceException("Error fetching all build details for job: " + jobName, e);
        }
    }

    private String fetchBuildsForJobFromJenkins(String jobName, JenkinsConfig jenkinsConfig) throws IOException, InterruptedException {
        URI uri = URI.create(jenkinsConfig.getJenkinsUrl() + "/job/" + jobName + "/api/json?tree=builds[id,status,timestamp,duration,result]");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Authorization", getBasicAuthHeader(jenkinsConfig))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new JenkinsServiceException("Failed to fetch all build details. HTTP Status: " + response.statusCode());
        }

        return response.body();
    }
}
