package wizest.fx.configuration;

/**
 * ConfigurationFactory ���� build ���� ���� ���¿��� getConfiguration �Ҷ� �߻��Ǵ� ����
 */

public class NotBuildException extends ConfigurationException
{
    public NotBuildException( )
    {
        super();
    }

    public NotBuildException( String message )
    {
        super( message );
    }

    public NotBuildException( Throwable t )
    {
        super( t );
    }

    public NotBuildException( String message, Throwable t )
    {
        super( message, t );
    }
}