package wizest.fx.configuration;

/**
 * ȯ�漳���� �ʿ��� class �� implement �Ѵ�.
 */

public interface Configurable
{
    /**
     * Configuration �� �Ѱ��ش�. �� �޼ҵ�� �����ڰ� ȣ����Ŀ�
     * �ٸ��޼ҵ尡 ȣ��Ǳ����� �ݵ�� ����Ǿ���Ѵ�.
     *
     * @param configuration ������ Configuration class
     * @throws ConfigurationException configure �����߿� ������ ��������
     */
    void configure( Configuration configuration ) throws ConfigurationException;
}