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
     * Ÿ�Ը��� �����´�.
     *
     * @return Ÿ�Ը�
     */
    public String getClassName()
    {
        return className;
    }

}