package com.jt.buildtool.exception;

public class JenkinsServiceException extends RuntimeException {
    public JenkinsServiceException(String message) {
        super(message);
    }

    public JenkinsServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
