/*
 * Created on 2004. 9. 7
 */
package wizest.fx.serv;

/**
 * server�� �����ϴ� service ��ü. �� ���� instance�� �������� thread�� �����ϹǷ� field�� ���� ����
 * 
 * @author wizest
 */
public interface Service {
	Class<? extends ServiceContext> getServiceContextClass();

	void doService(ServiceContext request) throws ServiceException;
}