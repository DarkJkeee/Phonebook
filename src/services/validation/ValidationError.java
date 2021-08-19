package services.validation;

import java.util.ArrayList;

/**
 * Class which describes an error.
 */
public class ValidationError {

    private final String message;                       // Message of an error.
    private final ArrayList<FailedField> failedFields;  // Array of failed fields which represented as enum type.

    public ValidationError(String message, ArrayList<FailedField> failedField) {
        this.message = message;
        this.failedFields = failedField;
    }

    public ArrayList<FailedField> getFailedFields() {
        return failedFields;
    }
    public String getMessage() {
        return message;
    }
}
