package MyFirstTestAppSpringBoot.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ErrorCodes {
    EMPTY(""), VALIDATION_EXCEPTION("ValidationException"), UNKNOWN_EXCEPTION("UnknownException"), UNSUPPORTED_EXCEPTION("UnsupportedException");

    private final String value;

    ErrorCodes(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
