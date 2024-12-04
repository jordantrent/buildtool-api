package com.jt.buildtool.controller;

import com.jt.buildtool.config.JenkinsConfig;
import com.jt.buildtool.model.Build;
import com.jt.buildtool.model.Job;
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

    @PostMapping("/jobs/names")
    public List<Job> getJobs(
            @RequestBody JenkinsConfig jenkinsConfig) {
        return jenkinsService.getJobs(jenkinsConfig);
    }

    @PostMapping("/jobs/builds/{jobName}")
    public List<Build> getBuildsForJob(
            @PathVariable String jobName,
            @RequestBody JenkinsConfig jenkinsConfig) {
        return jenkinsService.getBuildsForJob(jobName, jenkinsConfig);
    }

    @PostMapping("/jobs/{jobName}/{buildId}")
    public ResponseEntity<Build> getBuildDetails(
            @PathVariable String jobName,
            @PathVariable int buildId,
            @RequestBody JenkinsConfig jenkinsConfig) {

        Build build = jenkinsService.getBuildDetails(jobName, buildId, jenkinsConfig);
        return ResponseEntity.ok(build);
    }

    @PostMapping("/jobs/{jobName}/{buildId}/consoleOutput")
    public ResponseEntity<String> getConsoleOutput(
            @PathVariable String jobName,
            @PathVariable int buildId,
            @RequestBody JenkinsConfig jenkinsConfig) {

        String consoleOutput = jenkinsService.getConsoleOutput(jobName, buildId, jenkinsConfig);
        return ResponseEntity.ok(consoleOutput);
    }
}
