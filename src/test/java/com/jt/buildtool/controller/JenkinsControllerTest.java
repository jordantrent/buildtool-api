package com.jt.buildtool.controller;

import com.jt.buildtool.model.BuildDetails;
import com.jt.buildtool.model.PipelineDetails;
import com.jt.buildtool.service.JenkinsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
        PipelineDetails pipelineDetails = new PipelineDetails();
        pipelineDetails.setName(jobName);
        pipelineDetails.setUrl("https://example.com/job/exampleJob");

        when(jenkinsService.getPipelineDetails(jobName)).thenReturn(pipelineDetails);

        PipelineDetails response = jenkinsController.getPipelineDetails(jobName).getBody();

        assertNotNull(response);
        assertEquals(jobName, response.getName());
        assertEquals("https://example.com/job/exampleJob", response.getUrl());
    }

    @Test
    void testGetBuildDetails() {

        String jobName = "exampleBuild";
        int buildNumber = 1;
        BuildDetails buildDetails = new BuildDetails();
        buildDetails.setDisplayName(jobName);
        buildDetails.setUrl("https://example.com/job/exampleBuild");

        when(jenkinsService.getBuildDetails(jobName, buildNumber)).thenReturn(buildDetails);

        BuildDetails response = jenkinsController.getBuildDetails(jobName, buildNumber).getBody();

        assertNotNull(response);
        assertEquals(jobName, response.getDisplayName());
        assertEquals("https://example.com/job/exampleBuild", response.getUrl());
    }

    @Test
    void testGetConsoleOutput() {

        String jobName = "exampleBuild";
        int buildNumber = 1;
        String expectedOutput = "Build started\\nStep 1: Success\\nStep 2: Failed\\nBuild finished";

        when(jenkinsService.getConsoleOutput(jobName, buildNumber)).thenReturn(expectedOutput);

        String response = jenkinsController.getConsoleOutput(jobName, buildNumber);

        assertNotNull(response);
        assertEquals(expectedOutput, response);
    }
}