package MyFirstTestAppSpringBoot.service;

import MyFirstTestAppSpringBoot.model.Request;
import MyFirstTestAppSpringBoot.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.Duration;

@Slf4j
@Service
public class RequestForwardingService {
    private final RestTemplate restTemplate;
    private final String service2Url;

    public RequestForwardingService(RestTemplateBuilder builder,
            @Value("${service2.url}") String service2Url) {
        this.restTemplate = builder.setConnectTimeout(Duration.ofSeconds(3))
                .setReadTimeout(Duration.ofSeconds(5)).build();
        this.service2Url = service2Url;
    }

    public void forward(Request request, long receivedAt) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Request-Received-At", String.valueOf(receivedAt));
        log.info("Отправка запроса в Сервис 2: {}", request);
        Response response = restTemplate.postForObject(service2Url,
                new HttpEntity<>(request, headers), Response.class);
        log.info("Получен ответ от Сервиса 2: {}", response);
    }
}
