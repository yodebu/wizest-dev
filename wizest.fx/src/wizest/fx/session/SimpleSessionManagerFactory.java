package wizest.fx.session;

import wizest.fx.configuration.Configuration;

public class SimpleSessionManagerFactory extends SessionManagerFactory
{
    /**
     * config에 관계 없이 manager를 생성하므로 null을 입력하면된다.-_-;;
     *
     * @param config
     * @return
     */
    public SessionManager createManager(Configuration config)
    {
        return new SimpleSessionManager("Simple Session Manager");
    }

    public SessionManager createManager(String name)
    {
        return new SimpleSessionManager(name);
    }
}