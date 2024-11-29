package com.jt.buildtool.controller;

import com.jt.buildtool.config.JenkinsConfig;
import com.jt.buildtool.model.BuildDetails;
import com.jt.buildtool.model.PipelineDetails;
import com.jt.buildtool.service.JenkinsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jenkins")
public class JenkinsController {

    private final JenkinsService jenkinsService;

    public JenkinsController(JenkinsService jenkinsService) {
        this.jenkinsService = jenkinsService;
    }

    @GetMapping("/jobs/names")
    public List<String> getJobNames(
            @RequestBody JenkinsConfig jenkinsConfig) {
        return jenkinsService.getJobNames(jenkinsConfig);
    }

    @GetMapping("/jobs/{jobName}")
    public ResponseEntity<PipelineDetails> getPipelineDetails(
            @PathVariable String jobName,
            @RequestBody JenkinsConfig jenkinsConfig) {

        PipelineDetails jobDetails = jenkinsService.getPipelineDetails(jobName, jenkinsConfig);
        return ResponseEntity.ok(jobDetails);
    }

    @GetMapping("/jobs/{jobName}/{buildId}")
    public ResponseEntity<BuildDetails> getBuildDetails(
            @PathVariable String jobName,
            @PathVariable int buildId,
            @RequestBody JenkinsConfig jenkinsConfig) {

        BuildDetails buildDetails = jenkinsService.getBuildDetails(jobName, buildId, jenkinsConfig);
        return ResponseEntity.ok(buildDetails);
    }

    @GetMapping("/jobs/{jobName}/{buildId}/consoleOutput")
    public ResponseEntity<String> getConsoleOutput(
            @PathVariable String jobName,
            @PathVariable int buildId,
            @RequestBody JenkinsConfig jenkinsConfig) {

        String consoleOutput = jenkinsService.getConsoleOutput(jobName, buildId, jenkinsConfig);
        return ResponseEntity.ok(consoleOutput);
    }
}
