package wizest.fx.configuration;

/**
 * 환경설정이 필요한 class 에 implement 한다.
 */

public interface Configurable
{
    /**
     * Configuration 을 넘겨준다. 이 메소드는 생성자가 호출된후에
     * 다른메소드가 호출되기전에 반드시 실행되어야한다.
     *
     * @param configuration 설정할 Configuration class
     * @throws ConfigurationException configure 실행중에 문제가 생겼을때
     */
    void configure( Configuration configuration ) throws ConfigurationException;
}