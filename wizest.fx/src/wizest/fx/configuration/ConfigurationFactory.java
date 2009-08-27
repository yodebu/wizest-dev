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
 * Configuration �� �����Ѵ�.
 *
 * ConfigurationFactory �� Configuration �ν��Ͻ��� �����Ѵ�.
 * ConfiguartionFactory �� newInstance( String instanceKeyName )�� ���� �����Ǹ� KeyName �� ������ �ְ� ������ factory ��
 * ���������� ����ȴ�. �ѹ������� factory �� �ٽ� �������� �ʰ� �̹̻����Ǿ��ִ� �ν��Ͻ��� �����ش�.
 *
 * ConfigurationFactory �����ϱ�
 *
 *      ConfigurationFactory factory = ConfigurationFactory.newInstance( keyname );
 *      factory.build( configurationFileName );  // xml ���� �о���̱�
 *
 *
 *
 * </pre>
 */

public abstract class ConfigurationFactory
{
    // ������ ConfigurationFactory �ν��Ͻ��� �����ϴ� hashtable
    private static Hashtable configurationFactoryCache = new Hashtable();

    // factory�� �����̸�
    protected String instanceKeyName = null;

//    protected ConfigurationType configurationType = null;

    // ���� ���ǰ� �ִ� concrete ConfigurationFactory Ŭ���� �� ��Ű����
    private static ConfigurationFactoryType configurationFactoryType = ConfigurationFactoryType.DEFAULT;

    /**
     * ConfiguratonFactory�� �ν��Ͻ��� �����Ѵ�.
     *
     *
     * @param instanceKeyName - ������ �ν��Ͻ��� �����̸�
     * @return ������ ConfigurationFactory �ν��Ͻ�
     * @throws ConfigurationException
     */
    public static ConfigurationFactory getInstance( String instanceKeyName ) throws ConfigurationException
    {
        // �̹� �����Ǿ��ִ� �ν��Ͻ��� �ִ��� ã�ƺ���.
        ConfigurationFactory instance = ( ConfigurationFactory )configurationFactoryCache.get( instanceKeyName );

        if( instance == null )
        {
            // ã�� ���Ͽ����� ���� �����Ѵ�.
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

            // ���� ������ �ν��Ͻ��� �����Ѵ�.
            configurationFactoryCache.put( instanceKeyName, instance );
        }

        return instance;
    }

    /**
     * filename �� �ش��ϴ� xml config ������ �о���δ�.
     *
     * @param filename - ȯ�漳���� ���� XML �����̸�
     * @throws ConfigurationBuildException
     */
    public void build( String filename ) throws ConfigurationBuildException
    {
        build( new File( filename ) );
    }

    /**
     * File �� ���ؼ� xml config ������ �о���δ�.
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
     * inputstream �� ���ؼ� xml config ������ �е��δ�.
     *
     * @param inputstream - ȯ�漳���� ���� inputstream
     * @throws ConfigurationBuildException
     */
    public void build( InputStream inputstream ) throws ConfigurationBuildException
    {
        build( new InputSource( inputstream ) );
    }

    /**
     * inputsource �� ���ؼ� xml config ������ �о���δ�.
     * @param inputsource
     * @throws ConfigurationBuildException
     */
    public abstract void build( InputSource inputsource ) throws ConfigurationBuildException;

    /**
     * Configuration �� �����´�.
     *
     * @param elementPathString - ������ Configuration �ν��Ͻ��� path ���ڿ�
     * @return elementPathString �� �ش��ϴ� element �� ���� Configuration �ν��Ͻ�
     * @throws NotFoundXmlTagException
     * @throws NotBuildException
     * @throws ConfigurationException
     */
    public abstract Configuration getConfiguration( String elementPathString ) throws NotFoundXmlTagException, NotBuildException, ConfigurationException;

    /**
     * Configuration ���� �����´�.
     *
     * @param elementPathString - ������ Configuration �ν��Ͻ��� path ���ڿ�
     * @return elementPathString �� �ش��ϴ� element �� ���� Configuration �ν��Ͻ� �迭
     * @throws NotFoundXmlTagException
     * @throws NotBuildException
     * @throws ConfigurationException
     */
    public abstract Configuration[] getConfigurations( String elementPathString ) throws NotFoundXmlTagException, NotBuildException, ConfigurationException;

//    /**
//     * ���� Factory �� Ŭ�����̸� �����ϱ�
//     *
//     * @param className - Factory Ŭ���� ��
//     */
//    public static void setClassName( String className )
//    {
//        ConfigurationFactory.className = className;
//    }
//
//    /**
//     * ���� Factory �� Ŭ�����̸� ��������
//     *
//     * @return ���� Factory �� Ŭ�����̸�
//     */
//    public static String getClassName()
//    {
//        return className;
//    }

    /**
     * ���� Factory���� �����ϴ� Configuration �� Type �����ϱ�
     *
     * @param configType
     */
//    public void setConfigurationType( ConfigurationType configType )
//    {
//        this.configurationType = configType;
//    }

    /**
     * ���� Factory���� �����ϴ� Configuration �� Type ��������
     *
     * @return ConfiguraitonType. �⺻���� Configuration.DEFAULT
     */
//    public ConfigurationType getConfigurationType()
//    {
//        return configurationType;
//    }

    /**
     * Factory �� �����̸��� �����´�.
     *
     * @return factory�� �����̸�
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
