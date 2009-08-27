package wizest.fx.configuration;

/**
 * 환경변수들을 가져오고 저장하는 기본적인 접근 메소드들을 정의하는 Interface
 */

public interface Configuration
{
    /**
     * 현재 Configuration 에서 childElementName이름의 자식 Element 를 하나가져온다.
     * childElementName을 가지는 자식이 하나이상일경우에는 가장처음에 발견되는 자식만을 가져온다.
     *
     * @param childElementName - 자식 Element 의 이름
     * @return childElementName 에 해당하는 자식 Element 를 찾아 Configuration 인스턴스를 생성해 return 한다.
     * @throws NotFoundXmlTagException
     * @throws NotBuildException
     * @throws ConfigurationException
     */
    Configuration getChild( String childElementName ) throws NotFoundXmlTagException, NotBuildException, ConfigurationException;

    /**
     * 현재 Configuration 에서 childElementName이름의 자식 Element 를 모두 가져온다.
     *
     * @param childElementName - 자식 Element 의 이름
     * @return childElementName 에 해당하는 자식 Element들을 찾아 Configuration 인스턴스 배열을 생성해 return 한다.
     * @throws NotFoundXmlTagException
     * @throws NotBuildException
     * @throws ConfigurationException
     */
    Configuration[] getChildren( String childElementName ) throws NotFoundXmlTagException, NotBuildException, ConfigurationException;

//    Configuration[] getChildren();


//    String getLocation();

    /**
     * 현재 Configuration 의 Tag 이름을 가져온다.
     *
     * @return 현재 Tag 의 이름
     */
    String getTagName();

    /**
     * attributeName 에 해당하는 속성의 값을 return 한다.
     *
     * @param attributeName - 속성이름
     * @return attributeName 에 해당하는 값
     */
    String getAttribute( String attributeName );

    String getAttribute( String attributeName, String defaultValue );

    /**
     * attributeName 에 해당하는 속성의 값을 int 형으로 return 한다.
     *
     * @param attributeName - 속성이름
     * @return attributeName 에 해당하는 값의 int형
     */
    int getAttributeAsInt( String attributeName ) throws ConfigurationException;

    int getAttributeAsInt( String attributeName, int defaultValue );

    /**
     * attributeName 에 해당하는 속성의 값을 float 형으로 return 한다.
     *
     * @param attributeName - 속성이름
     * @return attributeName 에 해당하는 값의 float형
     */
    float getAttributeAsFloat( String attributeName ) throws ConfigurationException;

    float getAttributeAsFloat( String attributeName, float defaultValue );

    /**
     * attributeName 에 해당하는 속성의 값을 boolean 형으로 return 한다.
     *
     * @param attributeName - 속성이름
     * @return attributeName 에 해당하는 값의 boolean
     */
    boolean getAttributeAsBoolean( String attributeName ) throws ConfigurationException;

    boolean getAttributeAsBoolean( String attributeName, boolean defalutValue );

    /**
     * 현재 Tag 에 둘러싸여있는 값을 return 한다.
     *
     * @return Tag 로 둘러쌓인 값
     *
     */
    String getValue();

    String getValue( String defaultValue );

    /**
     * 현재 Tag 에 둘러싸여있는 값을 int 형으로 return 한다.
     *
     * @return Tag 로 둘러쌓인 값의 int형
     */
    int getValueAsInt() throws ConfigurationException;

    int getValueAsInt( int defaultValue );

    /**
     * 현재 Tag 에 둘러싸여있는 값을 float 형으로 return 한다.
     *
     * @return Tag 로 둘러쌓인 값의 float형
     */
    float getValueAsFloat() throws ConfigurationException;

    float getValueAsFloat( float defaultValue );

    /**
     * 현재 Tag 에 둘러싸여있는 값을 boolean 형으로 return 한다.
     * @return
     */
    boolean getValueAsBoolean() throws ConfigurationException;

    boolean getValueAsBoolean( boolean defaultValue );

    /**
     * 현재 Tag 에 attributeName을 가지는 attribute가 존재하면 값을 attributeValue 로 설정하고
     * 존재하지 않으면 attribute 를 추가한다.
     *
     * @param attributeName
     * @param attributeValue
     * @throws ConfigurationException attributeName 에 illegal character가 포함되어있을때
     */
    void setAttribute( String attributeName, String attributeValue ) throws ConfigurationException;

//    void setValue( String value );

}