package wizest.fx.configuration;

import java.util.StringTokenizer;

import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

/**
 * �⺻ Configuration Ŭ����<br>
 *
 *
 * <pre>
 * XML �� Tag Element �� DefaultConfiguration Ŭ�����ϳ��� ���εȴ�.
 * ex)
 *  <tag attributeName=attributeValue>value</tag>
 *
 * xml �� attribute�� getAttribute(...) �� value�� getValue()�� �����Ѵ�.
 *
 * ex)
 *      ConfigurationFactory factory = ConfigurationFactory.newinstance();
 *      factory.build( "ȯ�漳�� XML ����" );
 *      // <config><sys type="unix">Solaris</sys></config> �϶�
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
     * @param element ���� Configuration �� ������ Element
     * @param elementPathString ���� Element�� ��ġ�� ��Ÿ���� path ���ڿ�
     * @param elementIndexString ���� Element�� ��ġ�� ��Ÿ���� index ���ڿ� path ���ڿ���
     * <br>aaa.bbb.ccc ��� aaa �� ���° ����, bbb�� ���° ������ ��Ÿ����. 0.0.0 �̶�� aaa ù��°, bbb ù��°, ccc ù��°�� ��Ÿ����.
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
     * ���� Configuration �� Element �� �����´�.
     *
     * @return
     */
    public Element getElement()
    {
        return element;
    }

    /**
     * ���� Configuration �� ��ġ�� ��Ÿ���� path ���ڿ�
     *
     * @return
     */
    public String getElementPathString()
    {
        return elementPathString;
    }

    /**
     * ���� Configuration �� ��ġ�� ��Ÿ���� index ���ڿ�
     *
     * @return
     */
    public String getElementIndexString()
    {
        return elementIndexString;
    }

    /**
     * ���� Configuration ��ġ�� ����
     *
     * elementPathString �� aaaa.bbb ��� elementDepth �� 2
     *
     * @return
     */
    public int getElementDepth()
    {
        return elementDepth;
    }

}