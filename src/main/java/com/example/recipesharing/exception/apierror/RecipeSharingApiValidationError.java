package com.example.recipesharing.exception.apierror;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class RecipeSharingApiValidationError extends RecipeSharingApiSubError {

    private String object;
    private String field;
    private Object rejectedValue;
    private String message;

    RecipeSharingApiValidationError(String object, String message) {
        this.object = object;
        this.message = message;
    }
}
