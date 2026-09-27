package MyFirstTestAppSpringBoot.controller;

import MyFirstTestAppSpringBoot.exception.UnsupportedCodeException;
import MyFirstTestAppSpringBoot.exception.ValidationFailedException;
import MyFirstTestAppSpringBoot.model.Request;
import MyFirstTestAppSpringBoot.model.Response;
import MyFirstTestAppSpringBoot.service.MyService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.Instant;
import java.util.stream.Collectors;

@RestController
public class MyController {

    private final MyService myService;

    public MyController(MyService myService) {
        this.myService = myService;
    }

        @PostMapping({"/api/request", "/test"})
    public Response process(@Valid @RequestBody Request request) {
        return myService.process(request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> handleValidation(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldErrors().stream()
                .map(this::formatFieldError)
                .collect(Collectors.joining("; "));
        return ResponseEntity.badRequest().body(failedResponse("ValidationException", message));
    }

    @ExceptionHandler(ValidationFailedException.class)
    public ResponseEntity<Response> handleValidation(ValidationFailedException exception) {
        return ResponseEntity.badRequest()
                .body(failedResponse("ValidationException", exception.getMessage()));
    }

    @ExceptionHandler(UnsupportedCodeException.class)
    public ResponseEntity<Response> handleUnsupportedCode(UnsupportedCodeException exception) {
        return ResponseEntity.badRequest()
                .body(failedResponse("Unsupported Code Exception", "Не поддерживаемая ошибка"));
    }

    private String formatFieldError(FieldError error) {
        return error.getField() + ": " + error.getDefaultMessage();
    }

    private Response failedResponse(String errorCode, String errorMessage) {
        return new Response(
                "failed",
                errorCode,
                errorMessage,
                "",
                Instant.now().toString(),
                "");
    }
}