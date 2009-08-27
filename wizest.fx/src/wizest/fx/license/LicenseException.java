package wizest.fx.license;

public class LicenseException extends Exception
{
    public LicenseException(String message) {
        super(message);
    }

    public LicenseException(String message,Throwable cause) {
        super(message,cause);
    }
}
