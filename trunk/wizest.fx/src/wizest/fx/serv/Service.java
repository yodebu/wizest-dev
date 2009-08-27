/*
 * Created on 2004. 9. 7
 */
package wizest.fx.serv;

/**
 * server가 제공하는 service 객체. 한 개의 instance를 여러개의 thread가 접근하므로 field를 사용시 주의
 * 
 * @author wizest
 */
public interface Service {
	Class<? extends ServiceContext> getServiceContextClass();

	void doService(ServiceContext request) throws ServiceException;
}