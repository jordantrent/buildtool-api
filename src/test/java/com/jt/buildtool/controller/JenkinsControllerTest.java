package com.jt.buildtool.controller;

import com.jt.buildtool.config.JenkinsConfig;
import com.jt.buildtool.model.Build;
import com.jt.buildtool.model.Job;
import com.jt.buildtool.service.JenkinsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
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
    void testGetBuildDetails() {

        String jobName = "exampleJob";
        int buildNumber = 1;

        JenkinsConfig jenkinsConfig = new JenkinsConfig();
        jenkinsConfig.setJenkinsUrl("http://localhost:8080");
        jenkinsConfig.setJenkinsUsername("testuser");
        jenkinsConfig.setJenkinsApiToken("testpass");

        Build exampleBuild = new Build();
        exampleBuild.setId(buildNumber);
        exampleBuild.setResult("BUILD_SUCCESS");

        when(jenkinsService.getBuildDetails(jobName, buildNumber, jenkinsConfig)).thenReturn(exampleBuild);

        Build response = jenkinsController.getBuildDetails(jobName, buildNumber, jenkinsConfig).getBody();

        assertNotNull(response);
        assertEquals(buildNumber, response.getId());
        assertEquals("BUILD_SUCCESS", response.getResult());
    }

    @Test
    void testGetConsoleOutput() {

        String jobName = "exampleBuild";
        int buildNumber = 1;

        JenkinsConfig jenkinsConfig = new JenkinsConfig();
        jenkinsConfig.setJenkinsUrl("http://localhost:8080");
        jenkinsConfig.setJenkinsUsername("testuser");
        jenkinsConfig.setJenkinsApiToken("testpass");

        String expectedOutput = "Build started\\nStep 1: Success\\nStep 2: Failed\\nBuild finished";

        when(jenkinsService.getConsoleOutput(jobName, buildNumber, jenkinsConfig)).thenReturn(expectedOutput);

        String response = jenkinsController.getConsoleOutput(jobName, buildNumber, jenkinsConfig).getBody();

        assertNotNull(response);
        assertEquals(expectedOutput, response);
    }

    @Test
    void testGetJobs() {

        JenkinsConfig jenkinsConfig = new JenkinsConfig();
        jenkinsConfig.setJenkinsUrl("http://localhost:8080");
        jenkinsConfig.setJenkinsUsername("testuser");
        jenkinsConfig.setJenkinsApiToken("testpass");

        Job exampleJob = new Job();
        exampleJob.setName("exampleJob1");
        exampleJob.setUrl("https://example.com/job/exampleJob1");

        Job exampleJob2 = new Job();
        exampleJob2.setName("exampleJob2");
        exampleJob2.setUrl("https://example.com/job/exampleJob2");

        List<Job> exampleOutput = new ArrayList<>();
        exampleOutput.add(exampleJob);
        exampleOutput.add(exampleJob2);

        when(jenkinsService.getJobs(jenkinsConfig)).thenReturn(exampleOutput);

        List<Job> response = jenkinsController.getJobs(jenkinsConfig);

        assertNotNull(response);
        assertEquals(exampleOutput, response);
    }

    @Test
    void testGetBuildsForJob() {

        String jobName = "exampleJob";

        JenkinsConfig jenkinsConfig = new JenkinsConfig();
        jenkinsConfig.setJenkinsUrl("http://localhost:8080");
        jenkinsConfig.setJenkinsUsername("testuser");
        jenkinsConfig.setJenkinsApiToken("testpass");

        Build successfulBuild = new Build();
        successfulBuild.setId(1);
        successfulBuild.setResult("BUILD_SUCCESS");

        Build failedBuild = new Build();
        failedBuild.setId(2);
        failedBuild.setResult("BUILD_FAIL");

        List<Build> exampleOutput = new ArrayList<>();
        exampleOutput.add(successfulBuild);
        exampleOutput.add(failedBuild);

        when(jenkinsService.getBuildsForJob(jobName, jenkinsConfig)).thenReturn(exampleOutput);

        List<Build> response = jenkinsController.getBuildsForJob(jobName, jenkinsConfig);

        assertNotNull(response);
        assertEquals(exampleOutput, response);
    }
}
