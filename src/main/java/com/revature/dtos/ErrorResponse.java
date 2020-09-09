package com.revature.dtos;

import java.time.LocalDateTime;
import java.util.Objects;

public class ErrorResponse {
    private int status;
    private String message;
    private String timestamp;

    public ErrorResponse(){
        super();

    }

    public ErrorResponse(int status, String message){
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorResponse that = (ErrorResponse) o;
        return getStatus() == that.getStatus() &&
                Objects.equals(getMessage(), that.getMessage()) &&
                Objects.equals(getTimestamp(), that.getTimestamp());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStatus(), getMessage(), getTimestamp());
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
