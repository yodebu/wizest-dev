package wizest.fx.configuration;


/**
 * Configuration 의 Abstract 클래스
 *
 * <pre>
 * getAttribute( ), getValue( ), getChild( ), getChildren( ) 매소드는 concrete 클래스에서 구현하고
 * 나머지 Configuration Interface에서 정의한 매소드들을 모드 구현한다.
 *
 * </pre>
 */

public abstract class AbstractConfiguration implements Configuration
{
    protected String tagName = null;

    public String getTagName()
    {
        return tagName;
    }

    final public String getAttribute( String attributeName, String defaultValue )
    {
        if( getAttribute( attributeName ) == "" ) return defaultValue;
        else return getAttribute( attributeName );
    }

    final public int getAttributeAsInt( String attributeName ) throws ConfigurationException
    {
        try
        {
            return Integer.parseInt( getAttribute( attributeName ) );
        }
        catch( NumberFormatException ex )
        {
            throw new ConfigurationException( "Attribute value is not set OR does not exist : " + attributeName , ex );
        }
    }

    final public int getAttributeAsInt( String attributeName, int defaultValue )
    {
        try
        {
            return getAttributeAsInt( attributeName );
        }
        catch( ConfigurationException ex )
        {
            return defaultValue;
        }
    }

    final public float getAttributeAsFloat( String attributeName ) throws ConfigurationException
    {
        try
        {
            return Float.parseFloat( getAttribute( attributeName ) );
        }
        catch( NumberFormatException ex )
        {
            throw new ConfigurationException( "Attribute value is not set OR does not exist : " + attributeName , ex );
        }
    }

    final public float getAttributeAsFloat( String attributeName, float defaultValue )
    {
        try
        {
            return getAttributeAsFloat( attributeName );
        }
        catch( ConfigurationException ex )
        {
            return defaultValue;
        }
    }

    final public boolean getAttributeAsBoolean( String attributeName ) throws ConfigurationException
    {
        if( getAttribute( attributeName ) == "" )
            throw new ConfigurationException( "Attribute value is not set OR does not exist : " + attributeName );
        else
            return Boolean.valueOf( getAttribute( attributeName ) ).booleanValue();
    }

    final public boolean getAttributeAsBoolean( String attributeName, boolean defaultValue )
    {
        try
        {
            return getAttributeAsBoolean( attributeName );
        }
        catch( ConfigurationException ex )
        {
            return defaultValue;
        }
    }


    final public String getValue( String defaultValue )
    {
        if( getValue().length() == 0 )
        {
            return defaultValue;
        }
        else
        {
            try {
                return getValue();
            }
            catch(Exception ex) {
                return defaultValue;
            }
        }
    }

    final public int getValueAsInt() throws ConfigurationException
    {
        try
        {
            return Integer.parseInt( getValue() );
        }
        catch( NumberFormatException ex )
        {
            throw new ConfigurationException( "Tag value is not set : " + this.getTagName() );
        }
    }

    final public int getValueAsInt( int defaultValue )
    {
        try
        {
            return getValueAsInt();
        }
        catch( ConfigurationException ex )
        {
            return defaultValue;
        }
    }

    final public float getValueAsFloat() throws ConfigurationException
    {
        try
        {
            return Float.parseFloat( getValue() );
        }
        catch( NumberFormatException ex )
        {
            throw new ConfigurationException( "Tag value is not set : " + this.getTagName() );
        }
    }

    final public float getValueAsFloat( float defaultValue )
    {
        try
        {
            return getValueAsFloat();
        }
        catch( ConfigurationException ex )
        {
            return defaultValue;
        }

    }

    final public boolean getValueAsBoolean() throws ConfigurationException
    {
        if( getValue().length() == 0 )
        {
            throw new ConfigurationException( "Tag value is not set : " + this.getTagName() );
        }
        return Boolean.valueOf( getValue() ).booleanValue();
    }

    final public boolean getValueAsBoolean( boolean defaultValue )
    {
        try
        {
            return getValueAsBoolean();
        }
        catch( ConfigurationException ex )
        {
            return defaultValue;
        }
    }
}
