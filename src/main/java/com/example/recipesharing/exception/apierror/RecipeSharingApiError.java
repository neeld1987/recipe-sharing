package com.example.recipesharing.exception.apierror;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
public class RecipeSharingApiError {

    private HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;
    private String debugMessage;
    private List<RecipeSharingApiSubError> subErrors;
    private List<String> errors;

    private RecipeSharingApiError() {
        timestamp = LocalDateTime.now();
    }

    public RecipeSharingApiError(HttpStatus status) {
        this();
        this.status = status;
    }

    RecipeSharingApiError(HttpStatus status, Throwable ex) {
        this();
        this.status = status;
        this.message = "Unexpected error";
        this.debugMessage = ex.getLocalizedMessage();
    }

    RecipeSharingApiError(HttpStatus status, String message, Throwable ex) {
        this();
        this.status = status;
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();
    }
}

