package wizest.fx.configuration;

/**
 * Configuration 시 발생되는 모든 에러
 */

public class ConfigurationException extends Exception
{
    public ConfigurationException()
    {
        super();
    }

    public ConfigurationException( String message )
    {
        super( message );
    }

    public ConfigurationException( Throwable t )
    {
        super( t );
    }

    public ConfigurationException( String message, Throwable t )
    {
        super( message, t );
    }

}