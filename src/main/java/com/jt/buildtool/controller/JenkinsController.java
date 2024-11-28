package com.jt.buildtool.controller;

import com.jt.buildtool.model.BuildDetails;
import com.jt.buildtool.model.PipelineDetails;
import com.jt.buildtool.service.JenkinsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jenkins")
public class JenkinsController {

    private final JenkinsService jenkinsService;

    public JenkinsController(JenkinsService jenkinsService) {
        this.jenkinsService = jenkinsService;
    }

    @GetMapping("/jobs/{jobName}")
    public ResponseEntity<PipelineDetails> getPipelineDetails(@PathVariable String jobName) {
        PipelineDetails jobDetails = jenkinsService.getPipelineDetails(jobName);
        return ResponseEntity.ok(jobDetails);
    }

    @GetMapping("/jobs/{jobName}/{buildId}")
    public ResponseEntity<BuildDetails> getBuildDetails(@PathVariable String jobName, @PathVariable int buildId) {
        BuildDetails buildDetails = jenkinsService.getBuildDetails(jobName, buildId);
        return ResponseEntity.ok(buildDetails);
    }
}