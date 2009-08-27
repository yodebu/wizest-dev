package wizest.fx.configuration;

public class ConfigurationFactoryType
{
    private String className = null;

    public static final ConfigurationFactoryType DEFAULT = new ConfigurationFactoryType( "wizest.fx.configuration.DefaultConfigurationFactory" );

    public ConfigurationFactoryType( String className )
    {
        this.className = className;
    }

    /**
     * 타입명을 가져온다.
     *
     * @return 타입명
     */
    public String getClassName()
    {
        return className;
    }

}