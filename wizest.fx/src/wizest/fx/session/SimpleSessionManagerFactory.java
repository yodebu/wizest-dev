package wizest.fx.session;

import wizest.fx.configuration.Configuration;

public class SimpleSessionManagerFactory extends SessionManagerFactory
{
    /**
     * config�� ���� ���� manager�� �����ϹǷ� null�� �Է��ϸ�ȴ�.-_-;;
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