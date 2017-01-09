package jrouter.framework.exception;

/**
 * 定义参数异常。
 *
 * @see com.guanghua._common.interceptor.ExceptionInterceptor
 */
public class ParameterValidationException extends RuntimeException {

    private final String name;

    private final String errorMessage;

    //log exception or not
    private boolean needLog;

    public ParameterValidationException(String name, String errorMessage) {
        this(name, errorMessage, true);
    }

    public ParameterValidationException(String name, String errorMessage, boolean needLog) {
        super(name + " : " + errorMessage);
        this.name = name;
        this.errorMessage = errorMessage;
        this.needLog = needLog;
    }

    public ParameterValidationException(String name, String errorMessage, Throwable cause) {
        super(name + " - " + errorMessage, cause);
        this.name = name;
        this.errorMessage = errorMessage;
    }

    public String getName() {
        return name;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isNeedLog() {
        return needLog;
    }

    public void setNeedLog(boolean needLog) {
        this.needLog = needLog;
    }

}
