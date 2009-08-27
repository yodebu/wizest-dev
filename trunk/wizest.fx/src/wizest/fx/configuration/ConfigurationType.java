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
     * 타입명을 가져온다.
     *
     * @return 타입명
     */
    public String getClassName()
    {
        return className;
    }
}