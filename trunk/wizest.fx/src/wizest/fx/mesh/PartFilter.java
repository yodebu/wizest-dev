/*
 * Created on 2004. 8. 11
 */
package wizest.fx.mesh;

/**
 * @author user
 */
public interface PartFilter {
    /**
     * @param discriptor
     * @return 선택할 경우 true / 제외할 경우 false
     */
    boolean filterPart(PartDescriptor discriptor);

}