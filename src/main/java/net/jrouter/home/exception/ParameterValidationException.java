package net.jrouter.home.exception;

@lombok.Builder
public class ParameterValidationException extends RuntimeException {

    @lombok.Getter
    private String name;

    /**
     * @see RuntimeException#getMessage()
     */
    private String message;

    /**
     * @see RuntimeException#getCause()
     */
    private Throwable cause;

    /**
     * @see RuntimeException#RuntimeException(String, Throwable, boolean, boolean)
     */
    @lombok.Getter
    @lombok.Builder.Default
    private boolean writableStackTrace = true;

    /**
     * @see RuntimeException#RuntimeException(String, Throwable, boolean, boolean)
     */
    public ParameterValidationException(String name, String message, Throwable cause, boolean writableStackTrace) {
        super(message, cause, true, writableStackTrace);
        this.name = name;
    }
}
