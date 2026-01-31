package jax;


/**
 * Jax-specific Exceptions.
 */
public class JaxException extends Exception {
    /**
     * Generic Exception.
     * @param message original exception message to be inherited.
     */
    public JaxException(String message) {
        super(message);
    }
}
