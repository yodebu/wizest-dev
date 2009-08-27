package wizest.fx.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.Hashtable;

import org.xml.sax.InputSource;

/**
 * <pre>
 * Configuration 을 생성한다.
 *
 * ConfigurationFactory 는 Configuration 인스턴스를 생성한다.
 * ConfiguartionFactory 는 newInstance( String instanceKeyName )에 의해 생성되며 KeyName 을 가지고 있고 생성된 factory 는
 * 내부적으로 저장된다. 한번생성된 factory 는 다시 생성되지 않고 이미생성되어있던 인스턴스를 돌려준다.
 *
 * ConfigurationFactory 생성하기
 *
 *      ConfigurationFactory factory = ConfigurationFactory.newInstance( keyname );
 *      factory.build( configurationFileName );  // xml 파일 읽어들이기
 *
 *
 *
 * </pre>
 */

public abstract class ConfigurationFactory
{
    // 생성된 ConfigurationFactory 인스턴스를 저장하는 hashtable
    private static Hashtable configurationFactoryCache = new Hashtable();

    // factory의 고유이름
    protected String instanceKeyName = null;

//    protected ConfigurationType configurationType = null;

    // 현재 사용되고 있는 concrete ConfigurationFactory 클래스 플 패키지명
    private static ConfigurationFactoryType configurationFactoryType = ConfigurationFactoryType.DEFAULT;

    /**
     * ConfiguratonFactory의 인스턴스를 생성한다.
     *
     *
     * @param instanceKeyName - 생성될 인스턴스의 고유이름
     * @return 생성된 ConfigurationFactory 인스턴스
     * @throws ConfigurationException
     */
    public static ConfigurationFactory getInstance( String instanceKeyName ) throws ConfigurationException
    {
        // 이미 생성되어있는 인스턴스가 있는지 찾아본다.
        ConfigurationFactory instance = ( ConfigurationFactory )configurationFactoryCache.get( instanceKeyName );

        if( instance == null )
        {
            // 찾지 못하였으면 새로 생성한다.
            try
            {
                Constructor constructor = Class.forName( configurationFactoryType.getClassName() ).getConstructor( new Class[]
                        {String.class} );
                instance = ( ConfigurationFactory ) ( constructor.newInstance( new Object[]
                        {instanceKeyName} ) );
            }
            catch( Exception ex )
            {
                throw new ConfigurationException( "ConfigurationFactory instance not Create : " + ex.getLocalizedMessage(), ex );
            }

            // 새로 생성한 인스턴스를 보관한다.
            configurationFactoryCache.put( instanceKeyName, instance );
        }

        return instance;
    }

    /**
     * filename 에 해당하는 xml config 정보를 읽어들인다.
     *
     * @param filename - 환경설정에 사용될 XML 파일이름
     * @throws ConfigurationBuildException
     */
    public void build( String filename ) throws ConfigurationBuildException
    {
        build( new File( filename ) );
    }

    /**
     * File 을 통해서 xml config 정보를 읽어들인다.
     *
     * @param confiFile
     * @throws ConfigurationBuildException
     */
    public void build( File configFile ) throws ConfigurationBuildException
    {
        try
        {
            File cFile = null;
            cFile = configFile.getCanonicalFile() ;
            FileInputStream fis = new FileInputStream( cFile );
            build( fis );
        }
        catch( IOException ex )
        {
            throw new ConfigurationBuildException( "file not found : " + configFile.getName(), ex );
        }
    }

    /**
     * inputstream 을 통해서 xml config 정보를 읽들인다.
     *
     * @param inputstream - 환경설정에 사용될 inputstream
     * @throws ConfigurationBuildException
     */
    public void build( InputStream inputstream ) throws ConfigurationBuildException
    {
        build( new InputSource( inputstream ) );
    }

    /**
     * inputsource 를 통해서 xml config 정보를 읽어들인다.
     * @param inputsource
     * @throws ConfigurationBuildException
     */
    public abstract void build( InputSource inputsource ) throws ConfigurationBuildException;

    /**
     * Configuration 을 가져온다.
     *
     * @param elementPathString - 가져올 Configuration 인스턴스의 path 문자열
     * @return elementPathString 에 해당하는 element 에 대한 Configuration 인스턴스
     * @throws NotFoundXmlTagException
     * @throws NotBuildException
     * @throws ConfigurationException
     */
    public abstract Configuration getConfiguration( String elementPathString ) throws NotFoundXmlTagException, NotBuildException, ConfigurationException;

    /**
     * Configuration 들을 가져온다.
     *
     * @param elementPathString - 가져올 Configuration 인스턴스의 path 문자열
     * @return elementPathString 에 해당하는 element 에 대한 Configuration 인스턴스 배열
     * @throws NotFoundXmlTagException
     * @throws NotBuildException
     * @throws ConfigurationException
     */
    public abstract Configuration[] getConfigurations( String elementPathString ) throws NotFoundXmlTagException, NotBuildException, ConfigurationException;

//    /**
//     * 현재 Factory 의 클래스이름 설정하기
//     *
//     * @param className - Factory 클래스 명
//     */
//    public static void setClassName( String className )
//    {
//        ConfigurationFactory.className = className;
//    }
//
//    /**
//     * 현재 Factory 의 클래스이름 가져오기
//     *
//     * @return 현재 Factory 의 클래스이름
//     */
//    public static String getClassName()
//    {
//        return className;
//    }

    /**
     * 현재 Factory에서 생성하는 Configuration 의 Type 설정하기
     *
     * @param configType
     */
//    public void setConfigurationType( ConfigurationType configType )
//    {
//        this.configurationType = configType;
//    }

    /**
     * 현재 Factory에서 생성하는 Configuration 의 Type 가져오기
     *
     * @return ConfiguraitonType. 기본값은 Configuration.DEFAULT
     */
//    public ConfigurationType getConfigurationType()
//    {
//        return configurationType;
//    }

    /**
     * Factory 의 고유이름을 가져온다.
     *
     * @return factory의 고유이름
     */
    public String getInstanceKeyName()
    {
        return instanceKeyName;
    }

    public ConfigurationFactoryType getConfigurationFactoryType()
    {
        return configurationFactoryType;
    }

    public void setConfigurationFactoryType( ConfigurationFactoryType configurationFactoryType )
    {
        //this.configurationFactoryType = configurationFactoryType;
        // by wizest
        ConfigurationFactory.configurationFactoryType = configurationFactoryType;
    }


}
