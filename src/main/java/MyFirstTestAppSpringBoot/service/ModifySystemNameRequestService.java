package MyFirstTestAppSpringBoot.service;

import MyFirstTestAppSpringBoot.model.Request;
import MyFirstTestAppSpringBoot.model.Systems;
import org.springframework.stereotype.Service;

@Service("ModifySystemNameRequestService")
public class ModifySystemNameRequestService implements ModifyRequestService {
    @Override
    public void modify(Request request) {
        request.setSystemName(Systems.SERVICE_1);
    }
}
