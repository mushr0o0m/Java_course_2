package MyFirstTestAppSpringBoot.service;

import MyFirstTestAppSpringBoot.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import MyFirstTestAppSpringBoot.util.DateTimeUtil;
import java.util.Date;

@Slf4j
@Service
@Qualifier("ModifySystemTimeResponseService")
public class ModifySystemTimeResponseService implements ModifyResponseService {
    @Override
    public Response modify(Response response) {
        response.setSystemTime(DateTimeUtil.getCustomFormat().format(new Date()));
        log.info("Ответ изменен в ModifySystemTimeResponseService: {}", response);
        return response;
    }
}
