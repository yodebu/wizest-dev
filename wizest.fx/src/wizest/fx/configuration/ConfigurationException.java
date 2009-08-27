package wizest.fx.configuration;

/**
 * Configuration �� �߻��Ǵ� ��� ����
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