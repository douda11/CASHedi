package com.example.cashedi.exceptions;

public class AprilApiException extends RuntimeException {
    private final int statusCode;

    public AprilApiException(String message) {
        super(message);
        this.statusCode = 0; // 0 indique un code non spécifié
    }

    public AprilApiException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public AprilApiException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = 0;
    }

    public AprilApiException(String message, int statusCode, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String toString() {
        if (statusCode > 0) {
            return String.format("AprilApiException[status=%d]: %s", statusCode, getMessage());
        }
        return String.format("AprilApiException: %s", getMessage());
    }
}