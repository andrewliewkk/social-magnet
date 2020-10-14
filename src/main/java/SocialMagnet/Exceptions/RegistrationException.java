package SocialMagnet.Exceptions;

/**
 * RegisterException
 */

public class RegistrationException extends IllegalArgumentException {
    public RegistrationException(String message) {
        super(message);
    }
}