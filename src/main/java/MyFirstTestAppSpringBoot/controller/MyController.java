package MyFirstTestAppSpringBoot.controller;

import MyFirstTestAppSpringBoot.exception.UnsupportedCodeException;
import MyFirstTestAppSpringBoot.exception.ValidationFailedException;
import MyFirstTestAppSpringBoot.model.*;
import MyFirstTestAppSpringBoot.service.MyService;
import MyFirstTestAppSpringBoot.service.ModifyRequestService;
import MyFirstTestAppSpringBoot.service.RequestForwardingService;
import MyFirstTestAppSpringBoot.service.ModifyResponseService;
import MyFirstTestAppSpringBoot.util.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Date;

@Slf4j
@RestController
public class MyController {
    private final MyService myService;
    private final ModifyResponseService operationUidService;
    private final ModifyResponseService systemTimeService;

    private final ModifyRequestService systemNameRequestService;
    private final ModifyRequestService sourceRequestService;
    private final RequestForwardingService forwardingService;

    public MyController(MyService myService,
            @Qualifier("ModifyOperationUidResponseService") ModifyResponseService operationUidService,
            @Qualifier("ModifySystemTimeResponseService") ModifyResponseService systemTimeService,
            @Qualifier("ModifySystemNameRequestService") ModifyRequestService systemNameRequestService,
            @Qualifier("ModifySourceRequestService") ModifyRequestService sourceRequestService,
            RequestForwardingService forwardingService) {
        this.myService = myService;
        this.operationUidService = operationUidService;
        this.systemTimeService = systemTimeService;
        this.systemNameRequestService = systemNameRequestService;
        this.sourceRequestService = sourceRequestService;
        this.forwardingService = forwardingService;
    }

    @PostMapping({"/api/request", "/test", "/feedback"})
    public Response process(@Valid @RequestBody Request request) {
        long receivedAt = System.currentTimeMillis();
        log.info("Получен запрос: {}", request);
        Response response = myService.process(request);
        operationUidService.modify(response);
        systemTimeService.modify(response);
        systemNameRequestService.modify(request);
        sourceRequestService.modify(request);
        forwardingService.forward(request, receivedAt);
        log.info("Отправлен ответ: {}", response);
        return response;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> handleValidation(MethodArgumentNotValidException exception) {
        log.error("Ошибка проверки запроса: {}", exception.getBindingResult().getTarget());
        for (FieldError error : exception.getBindingResult().getFieldErrors()) {
            log.error("bindingResult: {}: {}", error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(
                failedResponse(ErrorCodes.VALIDATION_EXCEPTION, ErrorMessages.VALIDATION));
    }

    @ExceptionHandler(ValidationFailedException.class)
    public ResponseEntity<Response> handleValidation(ValidationFailedException exception) {
        log.error("Ошибка валидации: {}", exception.getMessage());
        return ResponseEntity.badRequest().body(
                failedResponse(ErrorCodes.VALIDATION_EXCEPTION, ErrorMessages.VALIDATION));
    }

    @ExceptionHandler(UnsupportedCodeException.class)
    public ResponseEntity<Response> handleUnsupportedCode(UnsupportedCodeException exception) {
        log.error("Ошибка обработки запроса: {}", exception.getMessage());
        return ResponseEntity.badRequest().body(
                failedResponse(ErrorCodes.UNSUPPORTED_EXCEPTION, ErrorMessages.UNSUPPORTED));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Response> handleInvalidJson(HttpMessageNotReadableException exception) {
        log.error("Ошибка чтения JSON: {}", exception.getMessage());
        return ResponseEntity.badRequest().body(
                failedResponse(ErrorCodes.VALIDATION_EXCEPTION, ErrorMessages.VALIDATION));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> handleUnknownError(Exception exception) {
        log.error("Непредвиденная ошибка", exception);
        return ResponseEntity.internalServerError().body(
                failedResponse(ErrorCodes.UNKNOWN_EXCEPTION, ErrorMessages.UNKNOWN));
    }

    private Response failedResponse(ErrorCodes errorCode, ErrorMessages errorMessage) {
        Response response = new Response(Codes.FAILED, errorCode, errorMessage, "",
                DateTimeUtil.getCustomFormat().format(new Date()), "");
        log.info("Создан и отправлен ответ с ошибкой: {}", response);
        return response;
    }
}
