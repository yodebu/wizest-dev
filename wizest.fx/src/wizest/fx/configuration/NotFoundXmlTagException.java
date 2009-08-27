package wizest.fx.configuration;

/**
 * Configuration 을 얻어올때 해당 xml tag 가 존재 하지 않을때 발생되는 에러
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