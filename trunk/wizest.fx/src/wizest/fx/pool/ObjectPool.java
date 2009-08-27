package wizest.fx.pool;

public interface ObjectPool
{
    /**
     * pool�� object�� �ִ´�.
     * @param o
     */
    public void addObject(Object o);

    /**
     * pool���� object�� �����´�.
     * @return
     */
    public Object getObject();

    /**
     * ����� object�� �ݳ��Ѵ�.
     * @param o
     */
    public void releaseObject(Object o);

    /**
     * pool�� ��� object�� �����Ѵ�.
     */
    public void clear();

    /**
     * pool�� �Ҹ��Ų��.
     */
    public void destroy();
}