/*
 * Created on 2004. 8. 11
 */
package wizest.fx.mesh;

/**
 * @author user
 */
public class PartDescriptor {
    private final String partId;
    private final String partClassString;
    private final String description;
    private Class partClass;

    public PartDescriptor(String partId, String partClassString, String description) {
        this.partId = partId;
        this.partClassString = partClassString;
        this.description = description;
    }

    public String getPartId() {
        return this.partId;
    }

    public String getPartClassString() {
        return this.partClassString;
    }

    public String getDescription() {
        return this.description;
    }

    public Class getPartClass(ClassLoader loader) throws OperationException, ClassNotFoundException {
        if (partClass == null) {
            Class cls = loader.loadClass(getPartClassString());

            if (cls.isAssignableFrom(Part.class))
                partClass = cls;
            else
                throw new OperationException("incorrect part class, should implement Part interface.");
        }
        return partClass;
    }

    public boolean equals(Object obj) {
        if (obj instanceof PartDescriptor)
            return this.getPartId().equals(((PartDescriptor) obj).getPartId());
        else
            return false;
    }

    public int hashCode() {
        return getPartId().hashCode();
    }
}