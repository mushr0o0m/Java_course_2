package MyFirstTestAppSpringBoot.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void validRequestReturnsSuccessResponse() throws Exception {
        mockMvc.perform(post("/api/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRequest("1")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("success"))
                .andExpect(jsonPath("$.uid").value("1"))
                .andExpect(jsonPath("$.operationUid").value(org.hamcrest.Matchers.matchesPattern(
                        "[0-9a-f]{8}(-[0-9a-f]{4}){3}-[0-9a-f]{12}")))
                .andExpect(jsonPath("$.systemTime").value(org.hamcrest.Matchers.matchesPattern(
                        "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d{3}Z")))
                .andExpect(jsonPath("$.errorCode").value(""));
    }

    @Test
    void blankUidIsRejected() throws Exception {
        mockMvc.perform(post("/api/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRequest("")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("failed"))
                .andExpect(jsonPath("$.errorCode").value("ValidationException"));
    }

    @Test
    void oversizedUidIsRejected() throws Exception {
        mockMvc.perform(post("/api/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRequest("123456789012345678901234567890123")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("ValidationException"));
    }

    @Test
    void blankOperationUidIsRejected() throws Exception {
        mockMvc.perform(post("/api/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRequestWithOperation("")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("ValidationException"));
    }

    @Test
    void blankSystemTimeIsRejected() throws Exception {
        mockMvc.perform(post("/api/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRequestWithSystemTime("")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("ValidationException"));
    }

    @Test
    void communicationIdOutsideRangeIsRejected() throws Exception {
        mockMvc.perform(post("/api/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRequestWithCommunicationId(100001)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("ValidationException"));
    }

    @Test
    void allInvalidFieldsAreReportedAsValidationError() throws Exception {
        mockMvc.perform(post("/api/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("failed"))
                .andExpect(jsonPath("$.errorCode").value("ValidationException"));
    }

    @Test
    void uid123ReturnsUnsupportedCodeError() throws Exception {
        mockMvc.perform(post("/api/request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRequest("123")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("failed"))
                .andExpect(jsonPath("$.errorCode").value("UnsupportedException"));
    }

    @Test
    void allSystemsAreAccepted() throws Exception {
        for (String system : new String[]{"ERP", "CRM", "WMS"}) {
            mockMvc.perform(post("/test").contentType(MediaType.APPLICATION_JSON)
                            .content(validRequest("1").replace("ERP", system)))
                    .andExpect(status().isOk());
        }
    }

    @Test
    void unknownSystemIsRejected() throws Exception {
        mockMvc.perform(post("/api/request").contentType(MediaType.APPLICATION_JSON)
                        .content(validRequest("1").replace("ERP", "UNKNOWN")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("ValidationException"));
    }

    private String validRequest(String uid) {
        return "{\"uid\":\"" + uid + "\",\"operationUid\":\"operation-1\","
                + "\"systemName\":\"ERP\",\"systemTime\":\"2023-10-07T07:47:56.503Z\","
                + "\"source\":\"notification\",\"communicationId\":100,"
                + "\"templateId\":8,\"productCode\":1500,\"smsCode\":10}";
    }

    private String validRequestWithOperation(String operationUid) {
        return validRequest("1").replace("operation-1", operationUid);
    }

    private String validRequestWithSystemTime(String systemTime) {
        return validRequest("1").replace("2023-10-07T07:47:56.503Z", systemTime);
    }

    private String validRequestWithCommunicationId(int communicationId) {
        return validRequest("1").replace("\"communicationId\":100", "\"communicationId\": " + communicationId);
    }
}
