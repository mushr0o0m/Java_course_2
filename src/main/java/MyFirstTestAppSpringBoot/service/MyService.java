package MyFirstTestAppSpringBoot.service;

import MyFirstTestAppSpringBoot.model.Request;
import MyFirstTestAppSpringBoot.model.Response;

public interface MyService {

    Response process(Request request);
}