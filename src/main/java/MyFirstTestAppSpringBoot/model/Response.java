package MyFirstTestAppSpringBoot.model;

public class Response {

    private String code;
    private String errorCode;
    private String errorMessage;
    private String operationUid;
    private String systemTime;
    private String uid;

    public Response() {
    }

    public Response(String code, String errorCode, String errorMessage,
                    String operationUid, String systemTime, String uid) {
        this.code = code;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.operationUid = operationUid;
        this.systemTime = systemTime;
        this.uid = uid;
    }

    public String getCode() {
        return code;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getOperationUid() {
        return operationUid;
    }

    public String getSystemTime() {
        return systemTime;
    }

    public String getUid() {
        return uid;
    }
}