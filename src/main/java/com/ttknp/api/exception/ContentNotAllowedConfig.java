package com.ttknp.api.exception;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ContentNotAllowedConfig {

    static class ErrorResponse {

        private String message;
        private int status;
        private String causeClass;

        public ErrorResponse(String message, int status, String className) {
            this.message = message;
            this.status = status;
            this.causeClass = className;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getCauseClass() {
            return causeClass;
        }

        public void setCauseClass(String causeClass) {
            this.causeClass = causeClass;
        }
    }

    /**
        remember it will work after you throw it
        Like. return ... orElseThrow(throw new NotAllowed("There is not exist"))
        Just throw your handler will be enough
    */
    @ExceptionHandler(value = ContentNotAllowed.class)
    private ResponseEntity<?> getNotAllowed(ContentNotAllowed contentNotAllowed){
        return ResponseEntity
                .status(500)
                .body(new ErrorResponse(contentNotAllowed.getMessage(), 500, contentNotAllowed.getCause().getClass().getName()));
    }



}