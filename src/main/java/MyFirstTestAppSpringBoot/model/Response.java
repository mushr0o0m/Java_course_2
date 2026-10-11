package MyFirstTestAppSpringBoot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response {
    private Codes code;
    private ErrorCodes errorCode;
    private ErrorMessages errorMessage;
    private String operationUid;
    private String systemTime;
    private String uid;
}
