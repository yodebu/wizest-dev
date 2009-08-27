package wizest.fx.configuration;

/**
 * Configuration �� ���ö� �ش� xml tag �� ���� ���� ������ �߻��Ǵ� ����
 */

public class NotFoundXmlTagException extends ConfigurationException
{
    public NotFoundXmlTagException()
    {
        super();
    }

    public NotFoundXmlTagException( String message )
    {
        super( "Not Found XML tag : " + message );
    }

    public NotFoundXmlTagException( Throwable t )
    {
        super( t );
    }

    public NotFoundXmlTagException( String message, Throwable t )
    {
        super( "Not Found XML tag : " + message, t );
    }
}