package com.jt.buildtool.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.buildtool.config.JenkinsConfig;
import com.jt.buildtool.model.BuildDetails;
import com.jt.buildtool.model.PipelineDetails;
import org.springframework.stereotype.Service;
import com.jt.buildtool.exception.JenkinsServiceException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

@Service
public class JenkinsService {

    private final JenkinsConfig jenkinsConfig;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public JenkinsService(JenkinsConfig jenkinsConfig, ObjectMapper objectMapper) {
        this.jenkinsConfig = jenkinsConfig;
        this.httpClient = HttpClient.newBuilder().build();
        this.objectMapper = objectMapper;
    }

    public PipelineDetails getPipelineDetails(String jobName) {
        try {
            String jobJson = fetchPipelineDetailsFromJenkins(jobName);
            return objectMapper.readValue(jobJson, PipelineDetails.class);
        } catch (JsonProcessingException e) {
            throw new JenkinsServiceException("Error processing JSON for job: " + jobName, e);
        }
    }

    private String fetchPipelineDetailsFromJenkins(String jobName) {
        try {

            URI uri = URI.create(jenkinsConfig.getJenkinsUrl() + "/job/" + jobName + "/api/json?depth=1");

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Authorization", getBasicAuthHeader())
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new JenkinsServiceException("Failed to fetch job details. HTTP Status: " + response.statusCode());
            }

            return response.body();
        } catch (IOException e) {
            throw new JenkinsServiceException("Error fetching pipeline details from Jenkins for job: " + jobName, e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new JenkinsServiceException("Request was interrupted while fetching pipeline details for job: " + jobName, e);
        }
    }

    public BuildDetails getBuildDetails(String jobName, int id) {
        try {
            String jobJson = fetchBuildDetailsFromJenkins(jobName, id);

            JsonNode rootNode = objectMapper.readTree(jobJson);

            JsonNode buildDataNode = null;
            for (JsonNode action : rootNode.path("actions")) {
                if (action.has("_class") && action.get("_class").asText().equals("hudson.plugins.git.util.BuildData")) {
                    buildDataNode = action.path("remoteUrls");
                    break;
                }
            }

            String gitHubRepoUrl = buildDataNode != null && buildDataNode.isArray() ? buildDataNode.get(0).asText() : null;

            BuildDetails buildDetails = objectMapper.readValue(jobJson, BuildDetails.class);

            buildDetails.setGitHubRepoUrl(gitHubRepoUrl);

            return buildDetails;

        } catch (JsonProcessingException e) {
            throw new JenkinsServiceException("Error processing JSON for job: " + jobName, e);
        }
    }

    private String fetchBuildDetailsFromJenkins(String jobName, int id) {
        try {

            URI uri = URI.create(jenkinsConfig.getJenkinsUrl() + "/job/" + jobName + "/" + id + "/api/json");

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Authorization", getBasicAuthHeader())
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new JenkinsServiceException("Failed to fetch build details. HTTP Status: " + response.statusCode());
            }

            return response.body();
        } catch (IOException e) {
            throw new JenkinsServiceException("Error fetching build details from Jenkins for job: " + jobName, e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new JenkinsServiceException("Request was interrupted while fetching build details for job: " + jobName, e);
        }
    }

    public String getConsoleOutput(String jobName, int buildNumber) {
        try {
            return fetchConsoleOutputFromJenkins(jobName, buildNumber);
        } catch (IOException e) {
            throw new JenkinsServiceException("Error fetching console output from Jenkins for job: " + jobName, e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new JenkinsServiceException("Request was interrupted while fetching console output details for job: " + jobName, e);
        }
    }

    private String fetchConsoleOutputFromJenkins(String jobName, int buildNumber) throws IOException, InterruptedException {
        URI uri = URI.create(jenkinsConfig.getJenkinsUrl() + "/job/" + jobName + "/" + buildNumber + "/consoleText");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Authorization", getBasicAuthHeader())
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new JenkinsServiceException("Failed to fetch console output. HTTP Status: " + response.statusCode());
        }

        return response.body();
    }

    private String getBasicAuthHeader() {
        String credentials = jenkinsConfig.getJenkinsUsername() + ":" + jenkinsConfig.getJenkinsApiToken();
        return "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());
    }
}
