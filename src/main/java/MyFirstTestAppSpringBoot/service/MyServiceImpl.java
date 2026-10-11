package MyFirstTestAppSpringBoot.service;

import MyFirstTestAppSpringBoot.exception.UnsupportedCodeException;
import MyFirstTestAppSpringBoot.model.*;
import MyFirstTestAppSpringBoot.util.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Date;

@Slf4j
@Service
public class MyServiceImpl implements MyService {
    @Override
    public Response process(Request request) {
        log.info("Проверка запроса: {}", request);
        if ("123".equals(request.getUid())) {
            log.error("Выбрасывается UnsupportedCodeException: uid={}", request.getUid());
            throw new UnsupportedCodeException("Unsupported Code Exception");
        }
        Response response = new Response(Codes.SUCCESS, ErrorCodes.EMPTY, ErrorMessages.EMPTY,
                request.getOperationUid(), DateTimeUtil.getCustomFormat().format(new Date()), request.getUid());
        log.info("Создан ответ: {}", response);
        return response;
    }
}
