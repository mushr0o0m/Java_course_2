package MyFirstTestAppSpringBoot.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Systems {
    ERP,
    CRM,
    WMS,
    @JsonProperty("Service 1")
    SERVICE_1
}
