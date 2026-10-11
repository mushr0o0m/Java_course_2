package MyFirstTestAppSpringBoot.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ErrorMessages {
    EMPTY(""), VALIDATION("Ошибка валидации"), UNKNOWN("Произошла непредвиденная ошибка"), UNSUPPORTED("Не поддерживаемая ошибка");

    private final String value;

    ErrorMessages(String value) {
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
