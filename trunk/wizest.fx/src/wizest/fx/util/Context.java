package wizest.fx.util;

import java.util.Set;

public interface Context {
	/**
	 * <pre>
	 * context 계층 구조 순서로 attribute를 발견할 때까지 찾는다.
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
	 * 부모가 없으면 null
	 * 
	 * @return
	 */
	Context getParent();
}