package MyFirstTestAppSpringBoot.service;

import MyFirstTestAppSpringBoot.model.Request;

import org.springframework.stereotype.Service;

@Service("ModifySourceRequestService")
public class ModifySourceRequestService implements ModifyRequestService {
    @Override
    public void modify(Request request) {
        request.setSource("Service 1");
    }
}
