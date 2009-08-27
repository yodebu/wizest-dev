package wizest.fx.configuration;


/**
 * ConfigurationFactory ���� build �� �߻��Ǵ� ����
 */

public class ConfigurationBuildException extends ConfigurationException
{
    public ConfigurationBuildException()
    {
        super();
    }

    public ConfigurationBuildException( String message )
    {
        super( message );
    }

    public ConfigurationBuildException( Throwable t )
    {
        super( t );
    }

    public ConfigurationBuildException( String message , Throwable t )
    {
        super( message, t );
    }


}