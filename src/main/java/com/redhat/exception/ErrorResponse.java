package com.redhat.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@Getter
@EqualsAndHashCode
public class ErrorResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String errorId;
    private List<ErrorMessage> errors;

    public ErrorResponse(String errorId, ErrorMessage errorMessage) {
        this.errorId = errorId;
        this.errors = List.of(errorMessage);
    }

    public ErrorResponse(ErrorMessage errorMessage) {
        this(null, errorMessage);
    }

    public ErrorResponse(List<ErrorMessage> errors) {
        this.errorId = null;
        this.errors = errors;
    }

    public ErrorResponse() {
    }

    @Getter
    @EqualsAndHashCode
    public static class ErrorMessage {

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String path;
        private String message;

        public ErrorMessage(String path, String message) {
            this.path = path;
            this.message = message;
        }

        public ErrorMessage(String message) {
            this.path = null;
            this.message = message;
        }

        public ErrorMessage() {
        }
    }

}
