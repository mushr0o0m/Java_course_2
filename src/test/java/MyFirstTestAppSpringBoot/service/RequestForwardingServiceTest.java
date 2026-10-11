package MyFirstTestAppSpringBoot.service;

import MyFirstTestAppSpringBoot.model.Request;
import MyFirstTestAppSpringBoot.model.Systems;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RequestForwardingServiceTest {
    @Test
    void sendsModifiedRequestAndReceptionTime() {
        MockRestServiceServer[] server = new MockRestServiceServer[1];
        RestTemplateBuilder builder = new RestTemplateBuilder().additionalCustomizers(
                template -> server[0] = MockRestServiceServer.bindTo(template).build());
        RequestForwardingService service = new RequestForwardingService(builder,
                "http://localhost:8084/feedback");
        Request request = new Request();
        request.setUid("1");
        request.setSystemName(Systems.ERP);
        request.setSource("notification");
        new ModifySystemNameRequestService().modify(request);
        new ModifySourceRequestService().modify(request);
        server[0].expect(requestTo("http://localhost:8084/feedback"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("X-Request-Received-At", "1000"))
                .andExpect(jsonPath("$.systemName").value("Service 1"))
                .andExpect(jsonPath("$.source").value("Service 1"))
                .andExpect(jsonPath("$.uid").value("1"))
                .andRespond(withSuccess("{\"code\":\"success\"}", MediaType.APPLICATION_JSON));
        service.forward(request, 1000);
        server[0].verify();
    }

    @Test
    void service2FailureIsNotSilentlyIgnored() {
        MockRestServiceServer[] server = new MockRestServiceServer[1];
        RestTemplateBuilder builder = new RestTemplateBuilder().additionalCustomizers(
                template -> server[0] = MockRestServiceServer.bindTo(template).build());
        RequestForwardingService service = new RequestForwardingService(builder,
                "http://localhost:8084/feedback");
        server[0].expect(requestTo("http://localhost:8084/feedback"))
                .andRespond(withServerError());
        assertThrows(org.springframework.web.client.HttpServerErrorException.class,
                () -> service.forward(new Request(), 1000));
        server[0].verify();
    }
}
