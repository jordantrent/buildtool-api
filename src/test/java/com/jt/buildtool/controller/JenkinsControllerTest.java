package com.jt.buildtool.controller;

import com.jt.buildtool.config.JenkinsConfig;
import com.jt.buildtool.model.BuildDetails;
import com.jt.buildtool.model.PipelineDetails;
import com.jt.buildtool.service.JenkinsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class JenkinsControllerTest {

    @Mock
    private JenkinsService jenkinsService;

    @InjectMocks
    private JenkinsController jenkinsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPipelineDetails() {

        String jobName = "exampleJob";

        JenkinsConfig jenkinsConfig = new JenkinsConfig();
        jenkinsConfig.setJenkinsUrl("https://example.com/job/exampleJob");
        jenkinsConfig.setJenkinsUsername("testuser");
        jenkinsConfig.setJenkinsApiToken("testpass");

        PipelineDetails pipelineDetails = new PipelineDetails();
        pipelineDetails.setName(jobName);
        pipelineDetails.setUrl("https://example.com/job/exampleJob");

        when(jenkinsService.getPipelineDetails(jobName, jenkinsConfig)).thenReturn(pipelineDetails);

        PipelineDetails response = jenkinsController.getPipelineDetails(jobName,
                jenkinsConfig).getBody();

        assertNotNull(response);
        assertEquals(jobName, response.getName());
        assertEquals("https://example.com/job/exampleJob", response.getUrl());
    }

    @Test
    void testGetBuildDetails() {

        String jobName = "exampleBuild";
        int buildNumber = 1;

        JenkinsConfig jenkinsConfig = new JenkinsConfig();
        jenkinsConfig.setJenkinsUrl("https://example.com/job/exampleJob");
        jenkinsConfig.setJenkinsUsername("testuser");
        jenkinsConfig.setJenkinsApiToken("testpass");

        BuildDetails buildDetails = new BuildDetails();
        buildDetails.setDisplayName(jobName);
        buildDetails.setUrl("https://example.com/job/exampleBuild");

        when(jenkinsService.getBuildDetails(jobName, buildNumber, jenkinsConfig)).thenReturn(buildDetails);

        BuildDetails response = jenkinsController.getBuildDetails(jobName, buildNumber, jenkinsConfig).getBody();

        assertNotNull(response);
        assertEquals(jobName, response.getDisplayName());
        assertEquals("https://example.com/job/exampleBuild", response.getUrl());
    }

    @Test
    void testGetConsoleOutput() {

        String jobName = "exampleBuild";
        int buildNumber = 1;

        JenkinsConfig jenkinsConfig = new JenkinsConfig();
        jenkinsConfig.setJenkinsUrl("https://example.com/job/exampleJob");
        jenkinsConfig.setJenkinsUsername("testuser");
        jenkinsConfig.setJenkinsApiToken("testpass");

        String expectedOutput = "Build started\\nStep 1: Success\\nStep 2: Failed\\nBuild finished";

        when(jenkinsService.getConsoleOutput(jobName, buildNumber, jenkinsConfig)).thenReturn(expectedOutput);

        String response = jenkinsController.getConsoleOutput(jobName, buildNumber, jenkinsConfig).getBody();

        assertNotNull(response);
        assertEquals(expectedOutput, response);
    }

    @Test
    void testGetJobNames() {

        JenkinsConfig jenkinsConfig = new JenkinsConfig();
        jenkinsConfig.setJenkinsUrl("https://example.com/job/exampleJob");
        jenkinsConfig.setJenkinsUsername("testuser");
        jenkinsConfig.setJenkinsApiToken("testpass");

        List<String> expectedOutput = List.of("Job1", "Job2", "Job3");

        when(jenkinsService.getJobNames(jenkinsConfig)).thenReturn(expectedOutput);

        List<String> response = jenkinsController.getJobNames(jenkinsConfig);

        assertNotNull(response);
        assertEquals(expectedOutput, response);
    }
}
