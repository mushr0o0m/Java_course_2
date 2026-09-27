package MyFirstTestAppSpringBoot.service;

import MyFirstTestAppSpringBoot.exception.UnsupportedCodeException;
import MyFirstTestAppSpringBoot.model.Request;
import MyFirstTestAppSpringBoot.model.Response;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class MyServiceImpl implements MyService {

    @Override
    public Response process(Request request) {
        if ("123".equals(request.getUid())) {
            throw new UnsupportedCodeException("Unsupported Code Exception");
        }

        return new Response(
                "success",
                "",
                "",
                request.getOperationUid(),
                Instant.now().toString(),
                request.getUid());
    }
}