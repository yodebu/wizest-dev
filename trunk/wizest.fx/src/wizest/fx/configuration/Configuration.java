package wizest.fx.configuration;

/**
 * ȯ�溯������ �������� �����ϴ� �⺻���� ���� �޼ҵ���� �����ϴ� Interface
 */

public interface Configuration
{
    /**
     * ���� Configuration ���� childElementName�̸��� �ڽ� Element �� �ϳ������´�.
     * childElementName�� ������ �ڽ��� �ϳ��̻��ϰ�쿡�� ����ó���� �߰ߵǴ� �ڽĸ��� �����´�.
     *
     * @param childElementName - �ڽ� Element �� �̸�
     * @return childElementName �� �ش��ϴ� �ڽ� Element �� ã�� Configuration �ν��Ͻ��� ������ return �Ѵ�.
     * @throws NotFoundXmlTagException
     * @throws NotBuildException
     * @throws ConfigurationException
     */
    Configuration getChild( String childElementName ) throws NotFoundXmlTagException, NotBuildException, ConfigurationException;

    /**
     * ���� Configuration ���� childElementName�̸��� �ڽ� Element �� ��� �����´�.
     *
     * @param childElementName - �ڽ� Element �� �̸�
     * @return childElementName �� �ش��ϴ� �ڽ� Element���� ã�� Configuration �ν��Ͻ� �迭�� ������ return �Ѵ�.
     * @throws NotFoundXmlTagException
     * @throws NotBuildException
     * @throws ConfigurationException
     */
    Configuration[] getChildren( String childElementName ) throws NotFoundXmlTagException, NotBuildException, ConfigurationException;

//    Configuration[] getChildren();


//    String getLocation();

    /**
     * ���� Configuration �� Tag �̸��� �����´�.
     *
     * @return ���� Tag �� �̸�
     */
    String getTagName();

    /**
     * attributeName �� �ش��ϴ� �Ӽ��� ���� return �Ѵ�.
     *
     * @param attributeName - �Ӽ��̸�
     * @return attributeName �� �ش��ϴ� ��
     */
    String getAttribute( String attributeName );

    String getAttribute( String attributeName, String defaultValue );

    /**
     * attributeName �� �ش��ϴ� �Ӽ��� ���� int ������ return �Ѵ�.
     *
     * @param attributeName - �Ӽ��̸�
     * @return attributeName �� �ش��ϴ� ���� int��
     */
    int getAttributeAsInt( String attributeName ) throws ConfigurationException;

    int getAttributeAsInt( String attributeName, int defaultValue );

    /**
     * attributeName �� �ش��ϴ� �Ӽ��� ���� float ������ return �Ѵ�.
     *
     * @param attributeName - �Ӽ��̸�
     * @return attributeName �� �ش��ϴ� ���� float��
     */
    float getAttributeAsFloat( String attributeName ) throws ConfigurationException;

    float getAttributeAsFloat( String attributeName, float defaultValue );

    /**
     * attributeName �� �ش��ϴ� �Ӽ��� ���� boolean ������ return �Ѵ�.
     *
     * @param attributeName - �Ӽ��̸�
     * @return attributeName �� �ش��ϴ� ���� boolean
     */
    boolean getAttributeAsBoolean( String attributeName ) throws ConfigurationException;

    boolean getAttributeAsBoolean( String attributeName, boolean defalutValue );

    /**
     * ���� Tag �� �ѷ��ο��ִ� ���� return �Ѵ�.
     *
     * @return Tag �� �ѷ����� ��
     *
     */
    String getValue();

    String getValue( String defaultValue );

    /**
     * ���� Tag �� �ѷ��ο��ִ� ���� int ������ return �Ѵ�.
     *
     * @return Tag �� �ѷ����� ���� int��
     */
    int getValueAsInt() throws ConfigurationException;

    int getValueAsInt( int defaultValue );

    /**
     * ���� Tag �� �ѷ��ο��ִ� ���� float ������ return �Ѵ�.
     *
     * @return Tag �� �ѷ����� ���� float��
     */
    float getValueAsFloat() throws ConfigurationException;

    float getValueAsFloat( float defaultValue );

    /**
     * ���� Tag �� �ѷ��ο��ִ� ���� boolean ������ return �Ѵ�.
     * @return
     */
    boolean getValueAsBoolean() throws ConfigurationException;

    boolean getValueAsBoolean( boolean defaultValue );

    /**
     * ���� Tag �� attributeName�� ������ attribute�� �����ϸ� ���� attributeValue �� �����ϰ�
     * �������� ������ attribute �� �߰��Ѵ�.
     *
     * @param attributeName
     * @param attributeValue
     * @throws ConfigurationException attributeName �� illegal character�� ���ԵǾ�������
     */
    void setAttribute( String attributeName, String attributeValue ) throws ConfigurationException;

//    void setValue( String value );

}