/*
 * Created on 2004. 9. 8
 */
package wizest.fx.serv.http;

import wizest.fx.serv.Service;
import wizest.fx.serv.ServiceContext;
import wizest.fx.serv.ServiceException;

/**
 * @author wizest
 */
public abstract class HttpService implements Service {
	public final Class<HttpServiceContext> getServiceContextClass() {
		return HttpServiceContext.class;
	}

	
	public final void doService(ServiceContext context) throws ServiceException {
		// try {
		doService((HttpServiceContext) context);
		// } catch (ServiceException e) {
		// HttpServiceContext ctx = (HttpServiceContext) context;
		// HttpResponse resp = ctx.getHttpResponse();
		// try {
		// resp.send(HttpResponse.Code.ERR, new
		// StringContent(StackTrace.trace(e), "UTF-8", false));
		// } catch (IOException e1) {
		// }
		// throw e;
		// }
	}

	public abstract void doService(HttpServiceContext context) throws ServiceException;
}