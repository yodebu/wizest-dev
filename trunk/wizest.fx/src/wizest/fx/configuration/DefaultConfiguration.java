package wizest.fx.configuration;

import java.util.StringTokenizer;

import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

/**
 * 기본 Configuration 클래스<br>
 *
 *
 * <pre>
 * XML 의 Tag Element 와 DefaultConfiguration 클래스하나가 매핑된다.
 * ex)
 *  <tag attributeName=attributeValue>value</tag>
 *
 * xml 의 attribute는 getAttribute(...) 로 value는 getValue()로 접근한다.
 *
 * ex)
 *      ConfigurationFactory factory = ConfigurationFactory.newinstance();
 *      factory.build( "환경설정 XML 파일" );
 *      // <config><sys type="unix">Solaris</sys></config> 일때
 *      DefaultConfiguration config = factory.getConfiguration( "config.sys" );
 *      String attribute =  config.getAttribute( "type" );
 *      String value = config.getValue();
 *
 * </pre>
 *
 */

public class DefaultConfiguration extends AbstractConfiguration
{
    private Element element = null;
    private DefaultConfigurationFactory factory = null;
    private String elementPathString = null;
    private String elementIndexString = null;
    private int elementDepth = 0;

    /**
     *
     * @param element 현재 Configuration 이 가지는 Element
     * @param elementPathString 현재 Element의 위치를 나타내는 path 문자열
     * @param elementIndexString 현재 Element의 위치를 나타내는 index 문자열 path 문자열이
     * <br>aaa.bbb.ccc 라면 aaa 가 몇번째 인지, bbb가 면번째 인지를 나타낸다. 0.0.0 이라면 aaa 첫번째, bbb 첫번째, ccc 첫번째를 나타낸다.
     * @param factoryInstanceKeyName
     * @throws ConfigurationException
     */
    public DefaultConfiguration( Element element, String elementPathString, String elementIndexString, String factoryInstanceKeyName ) throws ConfigurationException
    {
//        System.out.println( "Configuration created : " + elementPathString );
        this.element = element;
        this.elementPathString = elementPathString;
        this.elementIndexString = elementIndexString;
        this.elementDepth = ( new StringTokenizer( elementPathString, "." ) ).countTokens();
        this.factory = ( DefaultConfigurationFactory )ConfigurationFactory.getInstance( factoryInstanceKeyName );
        this.tagName = element.getTagName();
    }

    public Configuration getChild( String childElementName ) throws NotFoundXmlTagException, NotBuildException, ConfigurationException
    {
        return factory.getConfiguration( childElementName, this );
    }

    public Configuration[] getChildren( String childElementName ) throws NotFoundXmlTagException, NotBuildException, ConfigurationException
    {
        return factory.getConfigurations( childElementName, this );
    }

    public String getValue()
    {
        if( element.hasChildNodes() )
        {

            if( element.getFirstChild().getNodeType() == NODETYPE.TEXT || element.getFirstChild().getNodeType() == NODETYPE.CDATA)
            {
                return( ( ( Text )element.getFirstChild() ).getData() ).trim();
            }
            else
            {
                return new String( "" );
            }
        }
        else
        {
            return new String( "" );
        }
    }

    public String getAttribute( String attributeName )
    {
        return element.getAttribute( attributeName ).trim();
    }

    public void setAttribute( String attributeName, String attributeValue ) throws ConfigurationException
    {
        try
        {
            element.setAttribute( attributeName, attributeValue );
        }
        catch( DOMException ex )
        {
            throw new ConfigurationException( ex.getLocalizedMessage(), ex );
        }
    }

    /**
     * 현재 Configuration 의 Element 를 가져온다.
     *
     * @return
     */
    public Element getElement()
    {
        return element;
    }

    /**
     * 현재 Configuration 의 위치를 나타내는 path 문자열
     *
     * @return
     */
    public String getElementPathString()
    {
        return elementPathString;
    }

    /**
     * 현재 Configuration 의 위치를 나타내는 index 문자열
     *
     * @return
     */
    public String getElementIndexString()
    {
        return elementIndexString;
    }

    /**
     * 현재 Configuration 위치의 깊이
     *
     * elementPathString 이 aaaa.bbb 라면 elementDepth 는 2
     *
     * @return
     */
    public int getElementDepth()
    {
        return elementDepth;
    }

}