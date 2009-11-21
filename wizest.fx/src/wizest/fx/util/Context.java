package wizest.fx.util;

import java.util.Set;

public interface Context {
	/**
	 * <pre>
	 * context ���� ���� ������ attribute�� �߰��� ������ ã�´�.
	 * pageContext &gt; templateContext &gt; publishingContext
	 * </pre>
	 * 
	 * @param name
	 * @return
	 */
	Object findAttribute(String name);

	void setAttribute(String name, Object obj);

	Object getAttribute(String name);

	void removeAttribute(String name);

	Set getAttributeNames();

	/**
	 * �θ� ������ null
	 * 
	 * @return
	 */
	Context getParent();
}