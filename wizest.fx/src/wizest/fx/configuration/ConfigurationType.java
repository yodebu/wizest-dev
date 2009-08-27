package wizest.fx.configuration;

public class ConfigurationType
{
    private String className = null;

    public static final ConfigurationType DEFAULT = new ConfigurationType( "wizest.fx.configuration.DefaultConfiguration" );

    public ConfigurationType( String className )
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