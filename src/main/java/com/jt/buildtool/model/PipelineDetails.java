package com.jt.buildtool.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PipelineDetails {
    private String name;
    private String url;
    private String color;
    private List<Build> builds;
    private String result;
    private Long duration;
    private Long timestamp;

    public PipelineDetails() {
        // no-args constructor for Jackson
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<Build> getBuilds() {
        return builds;
    }

    public void setBuilds(List<Build> builds) {
        this.builds = builds;
    }

    public String getResult() {
        return result != null ? result : "Pending";
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Long getDuration() {
        return duration != null ? duration : 0L;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Long getTimestamp() {
        return timestamp != null ? timestamp : 0L;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public static class Build {

        private int number;
        private String url;

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
