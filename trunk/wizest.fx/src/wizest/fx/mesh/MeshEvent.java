/*
 * Created on 2004. 8. 17
 */
package wizest.fx.mesh;

import java.util.EventObject;

/**
 * @author user
 */
public class MeshEvent extends EventObject {

    private final PartPack part1;
    private final PartPack part2;

    /**
     * @param mesh
     * @param partPack1
     * @param partPack2
     */
    public MeshEvent(Object source, PartPack partPack1, PartPack partPack2) {
        super(source);
        this.part1 = partPack1;
        this.part2 = partPack2;
    }

    public MeshEvent(Object source, PartPack part) {
        this(source, part, null);
    }

    /**
     * @return Returns the from.
     */
    public PartPack getPartPack1() {
        return part1;
    }

    /**
     * @return Returns the to.
     */
    public PartPack getPartPack2() {
        return part2;
    }

    /**
     * @return Returns the from.
     */
    public PartPack getPartPack() {
        return part1;
    }
}